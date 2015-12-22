package study.project.whereareyou.NavigationDrawerItemActivity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;
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

        setWidget();
        setEvent();

    }

    private void setEvent() {
        switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreference.WritetoSharePreference(SettingActivity.this,"SOUND",String.valueOf(isChecked));
            }
        });
        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreference.WritetoSharePreference(SettingActivity.this,"VIBRATION",String.valueOf(isChecked));
            }
        });
    }

    private void setWidget() {
        switch_sound = (Switch) findViewById(R.id.switch_setting_Sound);
        switch_vibrate = (Switch) findViewById(R.id.switch_setting_vibration);
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
