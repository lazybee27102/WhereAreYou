package study.project.whereareyou.NavigationDrawerItemActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.RoundImage;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView textView_profile_email;
    TextView textView_profile_name;
    TextView textView_profile_birthdate;

    MySqlOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        helper= new MySqlOpenHelper(this);

        setWidget();



        String userName = SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER",null);

        User user = helper.getUserByName(userName);
        String personPhotoUrl = user.getPhotoUrl();
        if(personPhotoUrl.trim().length()!=0)
        {
            new LoadProfileImageRounded(imageView).execute(personPhotoUrl);
        }

        textView_profile_name.setText(user.getName());
        textView_profile_email.setText(user.getEmail());
        textView_profile_birthdate.setText(user.getBirthDate());
    }

    private void setWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.ImageView_overlapImage);
        textView_profile_email = (TextView) findViewById(R.id.textView_profile_gmail);
        textView_profile_name = (TextView) findViewById(R.id.textView_profile_name);
        textView_profile_birthdate = (TextView) findViewById(R.id.textView_profile_birthdate);
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
