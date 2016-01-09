package study.project.whereareyou.Conversation;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import io.github.typer.Font;
import io.github.typer.Typer;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

public class ConversationMain extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    private ArrayList<String> userNames;
    UpdateLocationAsyncTask update;

    //
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;


    public ArrayList<String> getUserNames() {
        return userNames;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //googleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000)// 1 second, in milliseconds
                .setSmallestDisplacement(10);

        //fake User
        userNames = new ArrayList<>();
        userNames.add("lazybee272");
        userNames.add("lazybee273");
        //

        update = new UpdateLocationAsyncTask(ConversationMain.this);



    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Conversation_Chat_Fragment());
        adapter.addFragment(new Conversation_Map_Fragment());
        viewPager.setAdapter(adapter);
    }

    private void setTabIcon(TabLayout tabLayout) {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabOne.setText("CHAT");
        tabOne.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BLACK));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_mail_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        tabTwo.setText("MAP");
        tabTwo.setTypeface(Typer.set(this).getFont(Font.ROBOTO_BLACK));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_map_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null)
        {
            startAsyntask();
        }
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location result = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Message.printMessage(this,"Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Message.printMessage(this,"There are some errors with Location Services,Please Reconnect");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Message.printMessage(this, currentLatitude + "");
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

    public void startAsyntask()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            update.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", "")});
        }else
        {
            update.execute(new String[]{SharedPreference.ReadFromSharedPreference(getApplicationContext(), "USER", "")});
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        update.cancel(true);
    }


    public UpdateLocationAsyncTask getUpdate() {
        return update;
    }
}
