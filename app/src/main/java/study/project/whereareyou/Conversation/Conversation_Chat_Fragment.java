package study.project.whereareyou.Conversation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pubnub.api.Callback;
import com.pubnub.api.PnGcmMessage;
import com.pubnub.api.PnMessage;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import study.project.whereareyou.BasicCallback;
import study.project.whereareyou.ChatMessage;
import study.project.whereareyou.Constants;
import study.project.whereareyou.MessageAdapter;
import study.project.whereareyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Conversation_Chat_Fragment extends android.support.v4.app.Fragment {
    private Pubnub mPubNub;
    private EditText mMessageET;
    private ListView mListView;
    private MessageAdapter mMessageAdapter;
    private MenuItem mHereNow;
    private SharedPreferences mSharedPrefs;

    private String username = "tdp";
    private String channel = "test";

    private GoogleCloudMessaging gcm;
    private String gcmRegId;

    ConversationMain main;

    public Conversation_Chat_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //initialize user name and chanel
        View v = inflater.inflate(R.layout.content_chat, container, false);

        this.mListView = (ListView) v.findViewById(R.id.msgList);
        this.mMessageAdapter = new MessageAdapter(getContext(), new ArrayList<ChatMessage>());
        this.mMessageAdapter.userPresence(this.username, "join");
        autoSroll();
        this.mListView.setAdapter(mMessageAdapter);
        setupListView();

        mMessageET = (EditText) v.findViewById(R.id.etMessage);
        initPubnub();


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.main = (ConversationMain)context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            if(main.getUpdate()!=null && main.getUpdate().isCancelled()==true)
            {
                main.startAsyntask();
            }
        }
    }

    private void autoSroll(){
        this.mMessageAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mMessageAdapter.getCount() - 1);
            }
        });
    }

    private void setupListView(){
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatMessage chatMsg = mMessageAdapter.getItem(position);
                sendNotification(chatMsg.getUsername());
            }
        });
    }
    //gcm gửi thông báo khi có người bấm vào tên mình
    public void sendNotification(String toUser) {
        PnGcmMessage gcmMessage = new PnGcmMessage();
        JSONObject json = new JSONObject();
        try {
            json.put(Constants.GCM_POKE_FROM, this.username);
            json.put(Constants.GCM_CHAT_ROOM, this.channel);
            gcmMessage.setData(json);

            PnMessage message = new PnMessage(
                    this.mPubNub,
                    toUser,
                    new BasicCallback(),
                    gcmMessage);
            message.put("pn_debug",true); // Subscribe to yourchannel-pndebug on console for reports
            message.publish();
        }
        catch (JSONException e) { e.printStackTrace(); }
        catch (PubnubException e) { e.printStackTrace(); }
    }
    private void initPubnub(){
        this.mPubNub = new Pubnub(Constants.PUBLISH_KEY, Constants.SUBSCRIBE_KEY);
        this.mPubNub.setUUID(this.username);
        subscribeWithPresence();
        history();
        gcmRegister();
    }
    //subdcribe lên server, bao gồm cả trạng thái login
    public void subscribeWithPresence(){
        Callback subscribeCallback = new Callback() {
            @Override
            public void successCallback(String s, Object message) {
                if(message instanceof JSONObject){
                    try {
                        JSONObject jsonObject = (JSONObject)message;
                        JSONObject json = jsonObject.getJSONObject("data");
                        String name = json.getString("chatUser");
                        String msg = json.getString("chatMsg");
                        long time = json.getLong("chatTime");
                        if(name.equals(mPubNub.getUUID())) return;
                        final ChatMessage chatMessage = new ChatMessage(name, msg, time);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMessageAdapter.addMessage(chatMessage);
                            }
                        });
                    } catch (JSONException e){ e.printStackTrace(); }
                }
            }
            @Override
            public void connectCallback(String channel, Object message){
                hereNow(false);
                setStateLogin();
            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.getErrorString());
            }
        };

        try{
            mPubNub.subscribe(this.channel, subscribeCallback);
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    //lấy lịch sử tn đưa vào messageList, thay 100 bằng số tùy thích
    public void history(){
        this.mPubNub.history(this.channel, 100, true, new Callback() {
            @Override
            public void successCallback(String channel, final Object message) {
                try {
                    JSONArray json = (JSONArray) message;
                    final JSONArray messages = json.getJSONArray(0);
                    final List<ChatMessage> chatMsgs = new ArrayList<ChatMessage>();
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
                        String name = jsonMsg.getString(Constants.JSON_USER);
                        String msg = jsonMsg.getString(Constants.JSON_MSG);
                        long time = jsonMsg.getLong(Constants.JSON_TIME);
                        ChatMessage chatMsg = new ChatMessage(name, msg, time);
                        if(name.equals(mPubNub.getUUID()))
                            chatMsg.setOutGoing(true);
                        chatMsgs.add(chatMsg);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMessageAdapter.setChatMessageList(chatMsgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.d("History", error.toString());
            }
        });
    }

    private void gcmRegister() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getActivity());
            try {
                gcmRegId = getRegistrationId();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (gcmRegId.isEmpty()) {
                registerInBackground();
            } else {
                Toast.makeText(getContext(), "Registration ID already exists: " + gcmRegId, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("GCM-register", "No valid Google Play Services APK found.");
        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("GCM-check", "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
    private String getRegistrationId() {
        SharedPreferences prefs =getActivity().getSharedPreferences(Constants.CHAT_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(Constants.GCM_REG_ID, "");
    }

    private void registerInBackground() {
        new RegisterTask().execute();
    }

    private class RegisterTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String msg="";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getActivity());
                }
                gcmRegId = gcm.register(Constants.GCM_SENDER_ID);
                msg = "Device registered, registration ID: " + gcmRegId;

                sendRegistrationId(gcmRegId);

                storeRegistrationId(gcmRegId);
                Log.i("GCM-register", msg);
            } catch (IOException e){
                e.printStackTrace();
            }
            return msg;
        }
    }
    private void sendRegistrationId(String regId) {
        this.mPubNub.enablePushNotificationsOnChannel(this.username, regId, new BasicCallback());
    }

    private void storeRegistrationId(String regId) {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.CHAT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.GCM_REG_ID, regId);
        editor.apply();
    }

    //hiện số người đang online trên góc, khi bấm vào số thì hiện lên dialog cho biết danh sách những người đang online
    public void hereNow(final boolean displayUsers) {
        this.mPubNub.hereNow(this.channel, new Callback() {//hàm hereNow trả về danh sách những ng đang online, thay đổi trong callback tùy theo ý mình
            @Override
            public void successCallback(String channel, Object response) {
                try {
                    JSONObject json = (JSONObject) response;
                    final int occ = json.getInt("occupancy");
                    final JSONArray hereNowJSON = json.getJSONArray("uuids");
                    final Set<String> usersOnline = new HashSet<String>();
                    usersOnline.add(username);
                    for (int i = 0; i < hereNowJSON.length(); i++) {
                        usersOnline.add(hereNowJSON.getString(i));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mHereNow.setTitle(String.valueOf(occ));
                            mMessageAdapter.setOnlineNow(usersOnline);
                            if (displayUsers)
                                alertHereNow(usersOnline);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void alertHereNow(Set<String> userSet){
        List<String> users = new ArrayList<String>(userSet);
        LayoutInflater li = LayoutInflater.from(getActivity());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Online now");
        alertDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final ArrayAdapter<String> hnAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users);
        alertDialog.setAdapter(hnAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = hnAdapter.getItem(which);
                getStateLogin(user);
            }
        });
        alertDialog.show();
    }

    //lấy trạng thái login của 1 user
    public void getStateLogin(final String user){
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                if (!(response instanceof JSONObject)) return; // Ignore if not JSON
                try {
                    JSONObject state = (JSONObject) response;
                    final boolean online = state.has(Constants.STATE_LOGIN);
                    final long loginTime = online ? state.getLong(Constants.STATE_LOGIN) : 0;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!online)
                                Toast.makeText(getActivity(), user + " is not online.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), user + " logged in since " + MessageAdapter.formatTimeStamp(loginTime), Toast.LENGTH_SHORT).show();

                        }
                    });

                    Log.d("PUBNUB", "State: " + response.toString());
                } catch (JSONException e){ e.printStackTrace(); }
            }
        };
        this.mPubNub.getState(this.channel, user, callback);
    }

    public void setStateLogin(){
        Callback callback = new Callback() {
            @Override
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", "State: " + response.toString());
            }
        };
        try {
            JSONObject state = new JSONObject();
            state.put("loginTime", System.currentTimeMillis());
            this.mPubNub.setState(this.channel, this.mPubNub.getUUID(), state, callback);
        }
        catch (JSONException e) { e.printStackTrace(); }
    }
}
