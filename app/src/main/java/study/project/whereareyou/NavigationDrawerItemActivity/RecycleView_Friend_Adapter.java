package study.project.whereareyou.NavigationDrawerItemActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import study.project.whereareyou.OOP.Friend;
import study.project.whereareyou.R;

/**
 * Created by Administrator on 10/12/2015.
 */
public class RecycleView_Friend_Adapter extends RecyclerView.Adapter<RecycleView_Friend_Adapter.FriendViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Friend> friens;

    public RecycleView_Friend_Adapter(Context context, ArrayList<Friend> friens) {
        this.context = context;
        this.friens = friens;
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
        Friend friend = friens.get(position);
        if(friend!=null)
        {
            new LoadProfileImage(holder.imageView).execute(friend.getPhotoUrl());
            holder.textView.setText(friend.getName());
        }

    }

    @Override
    public int getItemCount() {
        return friens.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_friend_profile);
            textView = (TextView) itemView.findViewById(R.id.textView_friend_name);
        }
    }
}
