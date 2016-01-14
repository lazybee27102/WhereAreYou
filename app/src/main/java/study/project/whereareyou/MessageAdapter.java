package study.project.whereareyou;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DinhPhuc on 15/12/2015.
 */
public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private final Context context;
    private List<ChatMessage> chatMessageList;
    private LayoutInflater inflater;
    private Set<String> onlineNow = new HashSet<String>();


    public MessageAdapter (Context context, List<ChatMessage> chatMessageList) {
        super(context, R.layout.single_message_container, android.R.id.text1, chatMessageList);
        this.context = context;
        this.chatMessageList = chatMessageList;
        inflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        ChatMessage chatMessage;
        TextView user;
        TextView message;
        TextView time;
        View userPresence;
        RelativeLayout layoutMessage;
        RelativeLayout singleMessageContainer;
        TextView msgContent;
    }

    public void addMessage(ChatMessage message) {
        chatMessageList.add(message);
        notifyDataSetChanged();
    }

    public int getCount(){
        return  chatMessageList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public ChatMessage getItem(int position) {
        if(chatMessageList != null) {
            return chatMessageList.get(position);
        } else {
            return null;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ChatMessage chatMessage = chatMessageList.get(position);
        //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.single_message_container, parent, false);

            viewHolder.user = (TextView)convertView.findViewById(R.id.tvUser);
            viewHolder.message = (TextView) convertView.findViewById(R.id.msgContent);
            viewHolder.time = (TextView) convertView.findViewById(R.id.msgTime);
            viewHolder.userPresence = convertView.findViewById(R.id.userPresence);
            viewHolder.layoutMessage = (RelativeLayout)convertView.findViewById(R.id.msgLayout);
            viewHolder.singleMessageContainer = (RelativeLayout)convertView.findViewById(R.id.msgBackground);
            viewHolder.msgContent = (TextView)convertView.findViewById(R.id.msgContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setAlignment(viewHolder, chatMessage.getIsOutgoing());
        viewHolder.user.setText(chatMessage.getUsername());
        viewHolder.message.setText(chatMessage.getMessage());
        viewHolder.time.setText(formatTimeStamp(chatMessage.getTime()));
        viewHolder.chatMessage = chatMessage;
        viewHolder.userPresence.setBackgroundDrawable(
                this.onlineNow.contains(chatMessage.getUsername())
                        ? context.getResources().getDrawable(R.drawable.online_circle)
                        : null);
        return convertView;
    }

    private void setAlignment(ViewHolder holder, boolean isOutGoing) {
        holder.layoutMessage.setGravity(isOutGoing ? Gravity.RIGHT : Gravity.LEFT);
        holder.singleMessageContainer.setBackgroundResource(isOutGoing ? R.drawable.bubble_outgoing : R.drawable.bubble_incoming);
        holder.msgContent.setTextColor(isOutGoing ? Color.BLACK : Color.WHITE);
    }

    /*private static class ViewHolder {
        public TextView message;
        public TextView time;
        public LinearLayout singleMessageContainter;
        public RelativeLayout layoutMessage;
    }*/

    public void userPresence(String user, String action){
        boolean isOnline = action.equals("join") || action.equals("state-change");
        if (!isOnline && this.onlineNow.contains(user))
            this.onlineNow.remove(user);
        else if (isOnline && !this.onlineNow.contains(user))
            this.onlineNow.add(user);

        notifyDataSetChanged();
    }

    public void setChatMessageList(List<ChatMessage> list) {
        this.chatMessageList.clear();
        this.chatMessageList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearMessages()
    {
        this.chatMessageList.clear();
        notifyDataSetChanged();
    }

    public static String formatTimeStamp(long time) {
        SimpleDateFormat dateFormatLastYear = new SimpleDateFormat("d/M/y H:mm");
        SimpleDateFormat dateFormatCurrentYear = new SimpleDateFormat("MMM, d H:mm");
        SimpleDateFormat dateFormatCurrentWeek = new SimpleDateFormat("E H:mm");
        SimpleDateFormat dateFormatToDay = new SimpleDateFormat("H:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormatToDay.format(calendar.getTime());
    }

    public void setOnlineNow(Set<String> onlineNow){
        this.onlineNow = onlineNow;
        notifyDataSetChanged();
    }
}
