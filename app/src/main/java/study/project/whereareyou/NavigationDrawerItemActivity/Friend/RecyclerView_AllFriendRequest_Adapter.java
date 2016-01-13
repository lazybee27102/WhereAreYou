package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;

/**
 * Created by Administrator on 29/12/2015.
 */
public class RecyclerView_AllFriendRequest_Adapter extends RecyclerView.Adapter<RecyclerView_AllFriendRequest_Adapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> arrListFriendRequest;

    public RecyclerView_AllFriendRequest_Adapter(Context context, ArrayList<String> arrListFriendRequest) {
        this.context = context;
        this.arrListFriendRequest = arrListFriendRequest;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.friend_request_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String s = arrListFriendRequest.get(position);
        holder.textView_name.setText(s);

    }

    @Override
    public int getItemCount() {
        return arrListFriendRequest.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageButton button_add,button_clear;
        private TextView textView_name;
        private LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            button_add = (ImageButton) itemView.findViewById(R.id.imageButton_friendrequest_add);
            button_clear = (ImageButton) itemView.findViewById(R.id.imageButton_friendrequest_clear);
            textView_name = (TextView) itemView.findViewById(R.id.textView_friendchanel_name);
            layout = (LinearLayout) itemView.findViewById(R.id.linearLayout_friendrq_layout);

            button_add.setOnClickListener(this);
            button_clear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position  = this.getLayoutPosition();

            switch (v.getId())
            {
                case R.id.imageButton_friendrequest_add:
                {
                    new AddFriendAsyncTask(context, new AddFriendAsyncTask.getResponse() {
                        @Override
                        public void getResponse(boolean a) {
                            if (a)
                            {
                                String to  = SharedPreference.ReadFromSharedPreference(context, "USER", "");
                                Message.printMessage(context,arrListFriendRequest.get(position) + " becomes your friend");
                                new DeleteFriendRequest().execute(new String[]{arrListFriendRequest.get(position), to});
                                MyViewHolder.this.layout.setVisibility(View.GONE);
                            }else
                                Message.printMessage(context,"Add friend failed");
                        }
                    }).execute(arrListFriendRequest.get(position));


                }break;
                case R.id.imageButton_friendrequest_clear:
                {
                    String to  = SharedPreference.ReadFromSharedPreference(context, "USER", "");
                    new DeleteFriendRequest().execute(new String[]{arrListFriendRequest.get(position), to});
                }break;
            }

        }
    }
}
