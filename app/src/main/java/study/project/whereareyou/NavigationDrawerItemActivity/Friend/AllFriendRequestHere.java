package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

public class AllFriendRequestHere extends AppCompatActivity {
    ArrayList<String> arrList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView_AllFriendRequest_Adapter adapter;
    TextView textView_hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_friend_request_here);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_allFriendRequest);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        textView_hint = (TextView) findViewById(R.id.textView_hintText);


        new getAlllFriendRequestAsyncTask(this, new getAlllFriendRequestAsyncTask.getResponse() {
            @Override
            public void getResponse(ArrayList<String> s) {


                if(s.size()==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    textView_hint.setVisibility(View.VISIBLE);
                }

                else
                {
                    adapter  = new RecyclerView_AllFriendRequest_Adapter(AllFriendRequestHere.this,s);
                    recyclerView.setAdapter(adapter);
                    textView_hint.setVisibility(View.GONE);
                }




            }
        }).execute();



    }

    public static class getAlllFriendRequestAsyncTask extends AsyncTask<Void,Void,ArrayList<String>>
    {
        private Context context;

        private final String URL = "http://whereareyou.somee.com/WebService.asmx";
        private final String METHOD ="FRIENDREQUEST_GetAllFriendRequest";
        private final String NAMESPACE = "http://tempuri.org/";
        private final String SOAPACTION = NAMESPACE+METHOD;

        public interface getResponse
        {
            void getResponse(ArrayList<String> s);
        }
        private getResponse delegate;

        public getAlllFriendRequestAsyncTask(Context context, getResponse delegate) {
            this.context = context;
            this.delegate = delegate;
            arrResult = new ArrayList<>();
        }

        private ArrayList<String> arrResult;

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            SoapObject request = new SoapObject(NAMESPACE,METHOD);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            try {
                httpTransportSE.call(SOAPACTION,envelope);
                SoapObject array = (SoapObject) envelope.getResponse();
                if(array!=null)
                {
                    for(int i = 0;i<array.getPropertyCount();i++)
                    {
                        SoapObject soapItem = (SoapObject) array.getProperty(i);
                        String requestTo = soapItem.getPropertyAsString("RequestTo");
                        String requestFrom = soapItem.getPropertyAsString("RequestFrom");
                        int status = Integer.parseInt(soapItem.getProperty("RequestStatus").toString());
                        if(requestTo.equals(SharedPreference.ReadFromSharedPreference(context,"USER",""))&& requestFrom.length()!=0 && status==0)
                            arrResult.add(requestFrom);
                    }
                    return arrResult;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            delegate.getResponse(strings);
        }
    }
}
