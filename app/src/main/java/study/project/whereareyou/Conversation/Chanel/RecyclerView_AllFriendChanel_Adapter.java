package study.project.whereareyou.Conversation.Chanel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import study.project.whereareyou.NavigationDrawerItemActivity.Friend.AddFriendAsyncTask;
import study.project.whereareyou.NavigationDrawerItemActivity.Friend.DeleteFriendRequest;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

/**
 * Created by Administrator on 29/12/2015.
 */
public class RecyclerView_AllFriendChanel_Adapter extends RecyclerView.Adapter<RecyclerView_AllFriendChanel_Adapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private HashMap<String,Boolean> arrayListFriendNames;

    public RecyclerView_AllFriendChanel_Adapter(Context context,HashMap<String,Boolean> arrayListFriendNames) {
        this.context = context;
        this.arrayListFriendNames = arrayListFriendNames;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.friend_chanel_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArrayList<String> arrPositon = new ArrayList<>(arrayListFriendNames.keySet());
        holder.textView_name.setText(arrPositon.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayListFriendNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private TextView textView_name;
        private CheckBox checkBox_invite;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView_name = (TextView) itemView.findViewById(R.id.textView_friendchanel_name);
            checkBox_invite = (CheckBox) itemView.findViewById(R.id.checkBox_friendchanel_invite);

            checkBox_invite.setOnCheckedChangeListener(this);

        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String name = textView_name.getText().toString().trim();
            arrayListFriendNames.put(name,isChecked);
        }
    }
}
