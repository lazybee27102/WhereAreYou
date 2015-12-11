package study.project.whereareyou;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.FileReader;
import java.util.ArrayList;

import io.github.typer.Font;
import io.github.typer.Typer;
import study.project.whereareyou.OOP.Friend;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SignInActivity extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {
    private static final int ACTIVITY_SIGNIN_NETWORK = 1;
    private static final int RC_SIGN_IN = 0;

    private SignInButton googleSignInButton;
    private Button button_signOut;
    private TextView textView_sign_state;



    private static GoogleApiClient mGoogleApiClient;

    private MySqlOpenHelper helper;

    //widged here
    TextView mainText;
    TextView secondText;
    String UserName;

    //

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        helper = new MySqlOpenHelper(SignInActivity.this);

        runUIforUserConnectedNetWork();


        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        googleSignInButton = (SignInButton)findViewById(R.id.button_sign_in);
        googleSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        googleSignInButton.setOnClickListener(this);

        button_signOut = (Button) findViewById(R.id.button_signOut);
        button_signOut.setOnClickListener(this);

        textView_sign_state = (TextView) findViewById(R.id.textView_sign_flag);

        mainText = (TextView) findViewById(R.id.textView_main_text);
        secondText = (TextView) findViewById(R.id.textView_second_text);

        mainText.setTypeface(Typer.set(SignInActivity.this).getFont(Font.ROBOTO_BOLD));
        secondText.setTypeface(Typer.set(SignInActivity.this).getFont(Font.ROBOTO_BOLD));


    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void runUIforUserConnectedNetWork()
    {
        if(!haveNetworkConnection())
        {
            Intent i = new Intent(SignInActivity.this,NoNetworkAvailableAcitivy.class);
            startActivityForResult(i,ACTIVITY_SIGNIN_NETWORK);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }else if(requestCode == ACTIVITY_SIGNIN_NETWORK)
        {
            if(resultCode == RESULT_CANCELED)
            {
                finish();
                System.exit(0);
            }else
            {
                runUIforUserConnectedNetWork();
            }

        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        Boolean boo = Boolean.parseBoolean(SharedPreference.ReadFromSharedPreference(this,"LOGIN","true"));
        getUserInformation();
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(SignInActivity.this);
        if(mGoogleApiClient.isConnected() && boo == true)
        {
            updateUI(true);
        }

    }

    private void getUserInformation() {
        try
        {
            if(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)!=null)
            {
                Person currentPerson  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String personUrl = currentPerson.getImage().getUrl();
                String personBirthDate = currentPerson.getBirthday();
                String personLocation = currentPerson.getCurrentLocation();
                personUrl = personUrl.substring(0,personUrl.length()-2)+"100";

                UserName = personName;

                String lastUserId = helper.getLastUserId();
                String UserId = "";
                if(lastUserId==null)
                {
                    UserId = "USER1";
                }else
                {
                    String words[] = lastUserId.split("R");
                    int number = Integer.parseInt(words[1])+1;
                    UserId = "USER" + String.valueOf(number);
                }


                if(helper.getUserByName(personName)==null)
                {
                    helper.insertUser(new User(UserId,personEmail,personName,personUrl,personBirthDate,personLocation));
                }

                SharedPreference.WritetoSharePreference(getApplicationContext(),"LOGIN","true");
                SharedPreference.WritetoSharePreference(getApplicationContext(),"USER",UserName);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(SignInActivity.this,"There are some errors when connecting to your Google Account,Plase try again later",Toast.LENGTH_LONG).show();
            }
        } else {
            // Show the signed-out UI
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if(b == true)
        {
            startActivity(new Intent(this, MainActivity.class));
            googleSignInButton.setVisibility(View.GONE);
            textView_sign_state.setText("<Sign in with another Google Account>");
            button_signOut.setVisibility(View.VISIBLE);
        }else if(b == false)
        {
            googleSignInButton.setVisibility(View.VISIBLE);
            textView_sign_state.setText("<<Sign in to your Google Account>>");
            button_signOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_sign_in)
        {
            mShouldResolve = true;
            mGoogleApiClient.connect();
            SharedPreference.WritetoSharePreference(getApplicationContext(), "LOGIN", "true");

        }
        if(v.getId() == R.id.button_signOut)
        {
            if(mGoogleApiClient.isConnected())
            {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                updateUI(false);
                SharedPreference.WritetoSharePreference(getApplicationContext(),"LOGIN","false");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    String FriendEmail = "";
                    String FriendName = personBuffer.get(i).getDisplayName();
                    String FriendPhotoURL = personBuffer.get(i).getImage().getUrl();
                    String FriendBirthDate = personBuffer.get(i).getBirthday();
                    String FriendLocation = personBuffer.get(i).getCurrentLocation();

                    String lastFriendID = helper.getLastFriendId();
                    String FriendId = "";
                    if(lastFriendID==null)
                    {
                        FriendId = "FRIEND1";
                    }else
                    {
                        String words[] = lastFriendID.split("D");
                        int number = Integer.parseInt(words[1])+1;
                        FriendId = "FRIEND" + String.valueOf(number);
                    }


                    if(helper.getFriendByName(FriendName)==null)
                    {
                        Friend a = new Friend(FriendId,FriendEmail,FriendName,FriendPhotoURL,FriendBirthDate,FriendLocation,helper.getUserByName(UserName).getId());
                        helper.insertFriend(a);

                    }


                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else {
            Message.printMessage(this,"Can't get Your Friends's Information,Please feedback to us to repare");
        }
    }
}
