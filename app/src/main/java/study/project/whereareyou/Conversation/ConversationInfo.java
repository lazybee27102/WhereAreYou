package study.project.whereareyou.Conversation;

import java.util.ArrayList;

import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 26/10/2015.
 */
public class ConversationInfo {
    ArrayList<User> allGuessuser;
    User keyUser;
    String dateCreated;
    ArrayList<String> allMessages;

    public ConversationInfo() {
    }

    public ConversationInfo(ArrayList<User> allGuessuser, User keyUser, ArrayList<String> allMessages, String dateCreated) {
        this.allGuessuser = allGuessuser;
        this.keyUser = keyUser;
        this.allMessages = allMessages;
        this.dateCreated = dateCreated;
    }

    public ArrayList<User> getAllGuessuser() {
        return allGuessuser;
    }

    public void setAllGuessuser(ArrayList<User> allGuessuser) {
        this.allGuessuser = allGuessuser;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(User keyUser) {
        this.keyUser = keyUser;
    }

    public ArrayList<String> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(ArrayList<String> allMessages) {
        this.allMessages = allMessages;
    }
}
