package study.project.whereareyou.Conversation;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Conversation_Map_Fragment extends android.support.v4.app.Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private ConversationMain main;
    private Button button;

    public Conversation_Map_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_conversation__map_, container, false);
        button = (Button) v.findViewById(R.id.button_map_refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new GetUserLocationByName(getContext(), new GetUserLocationByName.getResponse() {
                        @Override
                        public void ProcessHashMap(HashMap<String, String> hashMap) {
                            if(hashMap.size()!=0)
                                AddMakerToGoogleMap(googleMap,hashMap);
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,main.getUserNames());
                } else {
                    main.getUpdate().cancel(true);
                    new GetUserLocationByName(getContext(), new GetUserLocationByName.getResponse() {
                        @Override
                        public void ProcessHashMap(HashMap<String, String> hashMap) {
                            if(hashMap.size()!=0)
                                AddMakerToGoogleMap(googleMap,hashMap);
                        }
                    }).execute(main.getUserNames());
                }
            }
        });


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately*/

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        setRetainInstance(true);


        return v;
    }

    /*@Override
    public void onStart() {
        super.onStart();

    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            main.getButton_HereNow().setEnabled(false);
            String UserName = SharedPreference.ReadFromSharedPreference(getContext(), "USER", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetUserLocationByName(getContext(), new GetUserLocationByName.getResponse() {
                    @Override
                    public void ProcessHashMap(HashMap<String, String> hashMap) {
                        if(hashMap.size()!=0)
                            AddMakerToGoogleMap(googleMap,hashMap);
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,main.getUserNames());
            } else {
                main.getUpdate().cancel(true);
                new GetUserLocationByName(getContext(), new GetUserLocationByName.getResponse() {
                    @Override
                    public void ProcessHashMap(HashMap<String, String> hashMap) {
                        if(hashMap.size()!=0)
                            AddMakerToGoogleMap(googleMap,hashMap);
                    }
                }).execute(main.getUserNames());
            }
        }
    }

    public void AddMakerToGoogleMap(GoogleMap map,HashMap<String,String> hashMap)
    {
        map.clear();
        for (HashMap.Entry<String, String> entry : hashMap.entrySet())
        {
            String UserName = entry.getKey();
            String[] Location = entry.getValue().split("/");
            Double latitude = Double.parseDouble(Location[0]);
            Double longtitude = Double.parseDouble(Location[1]);

            LatLng latLng = new LatLng(latitude,longtitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(UserName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            markerOptions.title(UserName);
            map.addMarker(markerOptions);


            if(UserName.equals(SharedPreference.ReadFromSharedPreference(getContext(),"USER","")))
            {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();
                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.main = (ConversationMain) context;
    }
}
