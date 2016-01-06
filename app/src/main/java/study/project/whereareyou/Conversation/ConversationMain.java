package study.project.whereareyou.Conversation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.typer.Font;
import io.github.typer.Typer;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

public class ConversationMain extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    UpdateLocationAsyncTask update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcon(tabLayout);

         update= (UpdateLocationAsyncTask) new UpdateLocationAsyncTask(ConversationMain.this).execute(new String[]{SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", "")});


    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Conversation_Chat_Fragment());
        adapter.addFragment(new Conversation_Map_Fragment());
        viewPager.setAdapter(adapter);
    }

    private void setTabIcon(TabLayout tabLayout) {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item,null);
        tabOne.setText("CHAT");
        tabOne.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BLACK));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_mail_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item,null);
        tabTwo.setText("MAP");
        tabTwo.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BLACK));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_map_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
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

    class ViewPagerAdapter extends FragmentPagerAdapter{
        FragmentManager manager;
        private final ArrayList<Fragment> fragmentsList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.manager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        public void addFragment(Fragment fragment)
        {
            fragmentsList.add(fragment);
        }

        public void ClearFragment()
        {
            fragmentsList.clear();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUGGG DESTROY", " RUNNNN");
        update.cancel(true);
    }


}
