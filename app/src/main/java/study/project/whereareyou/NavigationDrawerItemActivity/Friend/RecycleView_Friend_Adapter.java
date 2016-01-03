package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import study.project.whereareyou.NavigationDrawerItemActivity.Profile.LoadProfileImage;
import study.project.whereareyou.OOP.Friend;
import study.project.whereareyou.R;

/**
 * Created by Administrator on 10/12/2015.
 */
public class RecycleView_Friend_Adapter extends RecyclerView.Adapter<RecycleView_Friend_Adapter.FriendViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> friends;

    public RecycleView_Friend_Adapter(Context context, ArrayList<String> friends) {
        this.context = context;
        this.friends = friends;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.friend_item,parent,false);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        String friend = friends.get(position);
        if(friend!=null)
        {
            holder.textView.setText(friend);
        }

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_friendrequest_name);
        }
    }
}
