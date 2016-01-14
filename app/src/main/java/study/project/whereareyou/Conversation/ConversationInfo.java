package study.project.whereareyou.Conversation;

import java.io.Serializable;
import java.util.ArrayList;

import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 26/10/2015.
 */
public class ConversationInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    ArrayList<String> allGuessuser;
    String name;

    public ConversationInfo(String name, ArrayList<String> allGuessuser) {
        this.name = name;
        this.allGuessuser = allGuessuser;
    }

    public ConversationInfo() {
        allGuessuser = new ArrayList<>();
    }

    public ArrayList<String> getAllGuessuser() {
        return allGuessuser;
    }

    public void setAllGuessuser(ArrayList<String> allGuessuser) {
        this.allGuessuser = allGuessuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
