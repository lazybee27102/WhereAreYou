package study.project.whereareyou.NavigationDrawerItemActivity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import study.project.whereareyou.R;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linearLayout_alert_title;
    Button button_save,button_cancel;
    Switch switch_sound;
    Switch switch_vibrate;

    ImageButton pink,blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
