package study.project.whereareyou.NavigationDrawer;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import study.project.whereareyou.NavigationDrawerItemActivity.Friend.FriendsActivity;
import study.project.whereareyou.NavigationDrawerItemActivity.Profile.ProfileActivity;
import study.project.whereareyou.NavigationDrawerItemActivity.SettingActivity;
import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.RecyclerViewTouchListener;
import study.project.whereareyou.R;
import study.project.whereareyou.SignIn.SignInActivity;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    ImageView imageView_userIcon;
    TextView textView_navigationdrawer_name;
    TextView getTextView_navigationdrawer_email;
    MySqlOpenHelper helper;
    User user;


    private RecyclerView recyclerView_navi;
    private NavigationRecycleViewAdapter adapter;


    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new MySqlOpenHelper(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //topview
        imageView_userIcon = (ImageView) view.findViewById(R.id.imageView_navigationdrawer_image);
        textView_navigationdrawer_name = (TextView) view.findViewById(R.id.textView_navigationdrawer_name);
        getTextView_navigationdrawer_email = (TextView) view.findViewById(R.id.textView_navigationdrawer_gmail);


        //Set top of navigation drawew
//        Log.d("USERRRRRRRRRRRRRRR", name);

        //textView_navigationdrawer_name.setText(currentUser.getName());
        //getTextView_navigationdrawer_email.setText(currentUser.getEmail());





        // Inflate the layout for this fragment
        recyclerView_navi = (android.support.v7.widget.RecyclerView)view.findViewById(R.id.recyclerview_item_navigation);
        adapter = new NavigationRecycleViewAdapter(getActivity(),getData());
        recyclerView_navi.setAdapter(adapter);
        recyclerView_navi.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_navi.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView_navi, new study.project.whereareyou.OtherUsefullClass.ClickListener() {
            @Override
            public void onClick(final RecyclerView.ViewHolder view, int position) {
                final RelativeLayout relativeLayout = (RelativeLayout) view.itemView.findViewById(R.id.background_item_nav);
                final ImageView imageView = (ImageView) view.itemView.findViewById(R.id.imageView_image_item);
                final TextView textView = (TextView) view.itemView.findViewById(R.id.textView_name_item);
                if (position<5) {

                    relativeLayout.setBackgroundResource(R.color.colorPrimaryLight_pink);
                    textView.setTextColor(Color.WHITE);
                    imageView.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary_pink), PorterDuff.Mode.SRC_IN);
                }

                switch (position) {
                    case 0: {
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
                    }
                    break;
                    case 1: {
                        startActivity(new Intent(getActivity(), SettingActivity.class));
                    }
                    break;
                    case 2: {
                        startActivity(new Intent(getActivity(), FriendsActivity.class));
                    }
                    break;
                    case 3: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("Log out Google Account")
                                .setMessage("Do you really want to log out your Account ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getActivity(), SignInActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        getActivity().finish();

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        relativeLayout.setBackgroundColor(Color.WHITE);
                                        textView.setTextColor(Color.BLACK);
                                        imageView.setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_IN);

                                    }
                                });
                        builder.create().show();
                    }
                    break;
                }
            }
        }));

        return view;
    }

    public static List<NavigationDrawerItem> getData()
    {
        List<NavigationDrawerItem> arrayList_items = new ArrayList<>();
            NavigationDrawerItem profile = new NavigationDrawerItem("Profile",R.mipmap.ic_account_box_black_48dp);
            NavigationDrawerItem setting = new NavigationDrawerItem("Setting",R.mipmap.ic_settings_black_48dp);
            NavigationDrawerItem friend = new NavigationDrawerItem("Friends",R.mipmap.ic_group_black_48dp);
            NavigationDrawerItem signout = new NavigationDrawerItem("Sign Out",R.mipmap.ic_reply_black_48dp);

            arrayList_items.add(profile);
            arrayList_items.add(setting);
            arrayList_items.add(friend);
            arrayList_items.add(signout);
        return arrayList_items;
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar,User user) {
        this.user= user;

        textView_navigationdrawer_name.setText(user.getUserName());
        getTextView_navigationdrawer_email.setText(user.getEmail());

        mDrawerLayout = drawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView_navi!=null && adapter!=null)
        {
            recyclerView_navi.setAdapter(adapter);
        }
    }
}
