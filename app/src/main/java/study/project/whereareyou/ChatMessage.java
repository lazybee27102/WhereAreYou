package study.project.whereareyou;

import java.util.Date;

/**
 * Created by DinhPhuc on 15/12/2015.
 */
public class ChatMessage {
    private String username;
    private String message;
    private boolean isOutGoing;
    Date dateTime;
    private long time;

    public ChatMessage(String name, String msg, long time){
        this.username = name;
        this.message = msg;
        this.time = time;
    }

    public void setMessage(String msg){
        message = msg;
    }

    public void setOutGoing(boolean isSent){
        isOutGoing = isSent;
    }

    public void setDateTime(Date date) {
        this.dateTime = date;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getUsername(){return username;}
    public String getMessage() {
        return message;
    }

    public boolean getIsOutgoing() { return isOutGoing; }

    public long getTime() {return time;}
}
