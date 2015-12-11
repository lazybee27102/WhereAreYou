package study.project.whereareyou.NavigationDrawerItemActivity;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import study.project.whereareyou.OOP.Friend;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class FriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    MySqlOpenHelper helper;
    ArrayList<Friend> allFriends = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helper = new MySqlOpenHelper(this);
        User user = helper.getUserByName(SharedPreference.ReadFromSharedPreference(this, "USER", null));
        if(user != null)
        {
            allFriends = helper.getAllFriendOfCurrentUser(user);
        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecycleView_Friend_Adapter adapter = new RecycleView_Friend_Adapter(this,allFriends);
        recyclerView.setAdapter(adapter);


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
}
