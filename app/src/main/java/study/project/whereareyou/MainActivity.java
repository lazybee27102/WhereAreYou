package study.project.whereareyou;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import study.project.whereareyou.Conversation.Chanel.CreateChanel;
import study.project.whereareyou.Conversation.Chanel.GetAllChanelByMe;
import study.project.whereareyou.Conversation.ConversationInfo;
import study.project.whereareyou.Conversation.ConversationMain;
import study.project.whereareyou.Conversation.RecycleView_Conversation_Adapter;
import study.project.whereareyou.NavigationDrawerItemActivity.Profile.GetUserByNameAsyncTask;
import study.project.whereareyou.NavigationDrawer.NavigationDrawerFragment;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.ClickListener;
import study.project.whereareyou.OtherUsefullClass.NoNetworkAvailableAcitivy;
import study.project.whereareyou.OtherUsefullClass.RecyclerViewTouchListener;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class MainActivity extends AppCompatActivity {


    MySqlOpenHelper helper = new MySqlOpenHelper(this);

    DrawerLayout drawerLayout;
    NavigationDrawerFragment navigationDrawerFragment;
    android.support.v7.widget.Toolbar toolbar;
    android.support.v7.widget.RecyclerView recyclerView;
    public static final int ACTIVITY_SIGNIN_NETWORK = 1;

    private ArrayList<ConversationInfo> conversationInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runUIforUserConnectedNetWork();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerFragment =(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        User user =helper.getUserByName(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER",""));
        if(user==null)
        {
           LoadCurrentUser();
        }
        else
            navigationDrawerFragment.setUp(drawerLayout, toolbar, user);

        //ReycycleView of Chat Groups
        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recycleview_chatgroup_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        //LoadAllChanel


        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(MainActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder view, int position) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    ConversationInfo info = conversationInfos.get(position);
                    Bundle b = new Bundle();
                    b.putString("CHANEL_NAME", info.getName());
                    b.putString("USER_NAME", SharedPreference.ReadFromSharedPreference(getApplication(), "USER", ""));
                    b.putStringArrayList("ALL_USERS", info.getAllGuessuser());
                    Intent i = new Intent(MainActivity.this, ConversationMain.class);
                    i.putExtra("DATA",b);
                    startActivity(i);
                }
            }
        }));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView); // or attachToRecyclerView
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, CreateChanel.class));
            }
        });
    }



    private void LoadCurrentUser() {
            new GetUserByNameAsyncTask(this, new GetUserByNameAsyncTask.GetUserByNameAsyncTaskResponse() {
                @Override
                public void processResponse(User user) {
                    if(user!=null)
                    {
                        helper.insertUser(user);
                        navigationDrawerFragment.setUp(drawerLayout, toolbar, user);
                    }
                }
            }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER",""));

    }

    private void LoadConversation()
    {

            new GetAllChanelByMe(this, new GetAllChanelByMe.GetResponse() {
                @Override
                public void processResponse(ArrayList<ConversationInfo> user) {
                    if(user!=null && user.size()!=0)
                    {
                        conversationInfos = user;
                        RecycleView_Conversation_Adapter adapter = new RecycleView_Conversation_Adapter(MainActivity.this,user);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", ""));
    }

    private void LoadAllInformation()
    {
        LoadCurrentUser();
        LoadConversation();
    }

    public void runUIforUserConnectedNetWork()
    {
        if(!haveNetworkConnection())
        {
            startActivityForResult(new Intent(this, NoNetworkAvailableAcitivy.class), ACTIVITY_SIGNIN_NETWORK);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_SIGNIN_NETWORK)
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadConversation();
    }
}
