package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import study.project.whereareyou.NavigationDrawerItemActivity.Profile.GetUserByNameAsyncTask;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class FriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    MySqlOpenHelper helper;
ArrayList<String> arrayList_Friends = new ArrayList<String>();
    Button button_search_friend,button_seeFriendRequest;
    EditText editText_friendName;
    RecycleView_Friend_Adapter adapter;

    User CurrentUser = new User();
    Button button_refresh;
    //friend Show
    LinearLayout friendLayout;
    TextView textView_friendName;
    ImageButton imageButton_add,imageButton_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        new GetAllFriendAsyncTask(this, new GetAllFriendAsyncTask.getResponse() {
            @Override
            public void ProcessResponse(ArrayList<String> arr) {
                if(arr.size()!=0)
                {

                    arrayList_Friends = arr;
                    adapter = new RecycleView_Friend_Adapter(FriendsActivity.this,arr);
                    recyclerView.setAdapter(adapter);
                }
            }
        }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", ""));


        setWidget();
        setEvent();



    }

    private void setEvent() {
        button_search_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = SharedPreference.ReadFromSharedPreference(FriendsActivity.this,"USER","");
                if(editText_friendName.getText().length()!=0 && !editText_friendName.getText().toString().trim().equals(UserName))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(FriendsActivity.this);
                    progressDialog.setMessage("Searching friend...");
                    progressDialog.show();
                    new GetUserByNameAsyncTask(FriendsActivity.this, new GetUserByNameAsyncTask.GetUserByNameAsyncTaskResponse() {
                        @Override
                        public void processResponse(User user) {
                            if(user!=null)
                            {
                                CurrentUser = user;
                                friendLayout.setVisibility(View.VISIBLE);
                                textView_friendName.setText(user.getUserName());
                            }else
                                Message.printMessage(FriendsActivity.this,"Can't find that UserName");

                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }).execute(editText_friendName.getText().toString().trim());
                }else
                {
                    Message.printMessage(FriendsActivity.this,"Please insert Name of Your Friend (Not your Name)");
                }
            }
        });

        button_seeFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this,AllFriendRequestHere.class);
                startActivity(i);
            }
        });

        imageButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER","").equals(editText_friendName.getText().toString().trim()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this)
                            .setTitle("Add friend")
                            .setMessage("Do you want add this friend?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new AddFriendRequestAsyncTask(FriendsActivity.this, new AddFriendRequestAsyncTask.getResponse() {
                                        @Override
                                        public void getResponse(Boolean a) {
                                            if (a) {
                                                Message.printMessage(FriendsActivity.this, "Sending Friend Request...");
                                                friendLayout.setVisibility(View.GONE);
                                            } else
                                                Message.printMessage(FriendsActivity.this, "Add Friend Failed");
                                        }
                                    }).execute(new String[]{SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", "").toString(), CurrentUser.getUserName()});


                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            }
        });

        imageButton_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendLayout.setVisibility(View.GONE);
            }
        });

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new GetAllFriendAsyncTask(FriendsActivity.this, new GetAllFriendAsyncTask.getResponse() {
                        @Override
                        public void ProcessResponse(ArrayList<String> arr) {
                            if(arr.size()!=0)
                            {

                                adapter = new RecycleView_Friend_Adapter(FriendsActivity.this,arr);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", ""));
            }
        });
    }

    private void setWidget() {
        button_search_friend = (Button) findViewById(R.id.button_friend_SearchFriend);
        button_seeFriendRequest = (Button) findViewById(R.id.button_friend_FriendRequest);
        editText_friendName = (EditText) findViewById(R.id.editText_friend_friendName);


        helper = new MySqlOpenHelper(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        friendLayout = (LinearLayout) findViewById(R.id.linearLayout_friend_layout);
        textView_friendName = (TextView) findViewById(R.id.textView_friendchanel_name);
        imageButton_add = (ImageButton) findViewById(R.id.imageButton_friend_add);
        imageButton_clear = (ImageButton) findViewById(R.id.imageButton_friendrequest_clear);

        button_refresh = (Button) findViewById(R.id.button_refresh);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
        {
            new GetAllFriendAsyncTask(this, new GetAllFriendAsyncTask.getResponse() {
                @Override
                public void ProcessResponse(ArrayList<String> arr) {
                    if(arr.size()!=0)
                    {

                        arrayList_Friends = arr;
                        adapter = new RecycleView_Friend_Adapter(FriendsActivity.this,arr);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }).execute(SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", ""));
        }
    }
}
