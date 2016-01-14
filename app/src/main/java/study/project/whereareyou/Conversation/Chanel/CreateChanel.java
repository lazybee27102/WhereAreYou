package study.project.whereareyou.Conversation.Chanel;

import android.app.ApplicationErrorReport;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import study.project.whereareyou.NavigationDrawerItemActivity.Friend.GetAllFriendAsyncTask;
import study.project.whereareyou.NavigationDrawerItemActivity.Friend.RecyclerView_AllFriendRequest_Adapter;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.MyLinearLayoutManager;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

public class CreateChanel extends AppCompatActivity {
    private EditText editText_chanel_name;
    private RecyclerView recyclerView;
    private HashMap<String,Boolean> friends = new HashMap<>();
    private RecyclerView_AllFriendChanel_Adapter adapter;
    private Button button_Create,button_Cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chanel);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView_friendchanel);
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(myLinearLayoutManager);
        recyclerView.setHasFixedSize(true);

        new GetAllFriendAsyncTask(this, new GetAllFriendAsyncTask.getResponse() {
            @Override
            public void ProcessResponse(ArrayList<String> arr) {
                if(arr!=null)
                {
                    for (String s : arr)
                    {
                        friends.put(s,false);
                    }
                    adapter = new RecyclerView_AllFriendChanel_Adapter(CreateChanel.this,friends);
                    recyclerView.setAdapter(adapter);
                }

            }
        }).execute(SharedPreference.ReadFromSharedPreference(CreateChanel.this,"USER",""));
        getAllWidget();
        setEvent();
    }

    private void setEvent() {
        button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> users =  new ArrayList<String>();
                if(friends!=null)
                {
                    Integer temp = 0;
                    for (HashMap.Entry<String, Boolean> entry : friends.entrySet()) {
                        String key = entry.getKey();
                        Boolean value = entry.getValue();

                        if (value)
                        {
                            temp++;
                            users.add(key);
                        }
                    }
                    int lenght = editText_chanel_name.getText().toString().trim().length();
                    if(lenght==0)
                        Message.printMessage(CreateChanel.this,"Please insert Chanel Name");
                    if(temp==0)
                        Message.printMessage(CreateChanel.this,"Please check friend Name to Chat");

                    if(lenght!=0 && temp!=0)
                    {
                        users.add(SharedPreference.ReadFromSharedPreference(CreateChanel.this, "USER", ""));

                        new AddChanelAsyncTask(CreateChanel.this, editText_chanel_name.getText().toString().trim(),new AddChanelAsyncTask.getResponse() {
                            @Override
                            public void processResponse(Boolean aBoolean) {
                                if(aBoolean)
                                {
                                    Message.printMessage(CreateChanel.this,"Create Group Chat Successfully");
                                    CreateChanel.this.finish();
                                }
                                else
                                    Message.printMessage(CreateChanel.this,"There are some errors when creating a new Groupchat");
                            }
                        }).execute(users);
                    }
                }
            }
        });
    }

    private void getAllWidget() {
        editText_chanel_name = (EditText) findViewById(R.id.editText_createchanel_name);
        button_Cancel = (Button) findViewById(R.id.button_createchanel_canel);
        button_Create = (Button) findViewById(R.id.button_createchanel_create);
    }
}
