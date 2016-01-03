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

import study.project.whereareyou.Conversation.ConversationInfo;
import study.project.whereareyou.Conversation.ConversationMain;
import study.project.whereareyou.Conversation.CreateConversationActivity;
import study.project.whereareyou.Conversation.RecycleView_Conversation_Adapter;
import study.project.whereareyou.NavigationDrawerItemActivity.Profile.GetUserByNameAsyncTask;
import study.project.whereareyou.NavigationDrawer.NavigationDrawerFragment;
import study.project.whereareyou.NavigationDrawer.NavigationDrawerItem;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.ClickListener;
import study.project.whereareyou.OtherUsefullClass.NoNetworkAvailableAcitivy;
import study.project.whereareyou.OtherUsefullClass.RecyclerViewTouchListener;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationDrawerFragment navigationDrawerFragment;
    android.support.v7.widget.Toolbar toolbar;
    android.support.v7.widget.RecyclerView recyclerView;
    private static final int ACTIVITY_SIGNIN_NETWORK = 1;
    private static final ArrayList<NavigationDrawerItem> items = new ArrayList<>();
    private User currentUser = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runUIforUserConnectedNetWork();
        LoadCurrentUser();


        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //ReycycleView of Chat Groups
        recyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.recycleview_chatgroup_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        //fakeUsers
        User user1 = new User("Nguyen Hoang Phat","27","Male","lazybee27102@gmail.com","hihi");
        User user2 = new User("Nguyen Hoang Phat","27","Male","lazybee27102@gmail.com","hihi");
        User user3 = new User("Nguyen Hoang Phat","27","Male","lazybee27102@gmail.com","hihi");
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        String s1 = "Hello";
        String s2 = "I am fine";
        String s3 = "How are you today?";
        ArrayList<String> mess = new ArrayList<>();
        mess.add(s1);
        mess.add(s2);
        mess.add(s3);
        ConversationInfo ci = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci1 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci2 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci3 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci4 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci5 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci6 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci7 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci8 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci9 = new ConversationInfo(users,user1,mess,"21/10/2015");
        ConversationInfo ci10 = new ConversationInfo(users,user1,mess,"21/10/2015");


        ArrayList<ConversationInfo> cis = new ArrayList<>();
        cis.add(ci);
        cis.add(ci1);
        cis.add(ci2);
        cis.add(ci3);
        cis.add(ci4);
        cis.add(ci5);
        cis.add(ci6);
        cis.add(ci7);
        cis.add(ci8);
        cis.add(ci9);
        cis.add(ci10);
        cis.add(ci10);
        cis.add(ci10);
        cis.add(ci10);
        cis.add(ci10);



        //end fake
        RecycleView_Conversation_Adapter adapter = new RecycleView_Conversation_Adapter(this,cis);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(MainActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(RecyclerView.ViewHolder view, int position) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    startActivity(new Intent(MainActivity.this,ConversationMain.class));
                }
            }
        }));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView); // or attachToRecyclerView
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateConversationActivity.class));
            }
        });
    }

    private void LoadCurrentUser() {
        new GetUserByNameAsyncTask(this,"Getting your Information...", new GetUserByNameAsyncTask.GetUserByNameAsyncTaskResponse() {
            @Override
            public void processResponse(User user) {
                if(user!=null)
                    currentUser = user;
                navigationDrawerFragment =(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
                navigationDrawerFragment.setUp(drawerLayout, toolbar, currentUser);

            }
        }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER",""));
    }

    public User getCurrentUser()
    {
        return currentUser;
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


}
