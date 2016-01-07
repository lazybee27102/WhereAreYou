package study.project.whereareyou.Conversation;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.project.whereareyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Conversation_Chat_Fragment extends android.support.v4.app.Fragment {

    ConversationMain main;

    public Conversation_Chat_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation__chat_, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.main = (ConversationMain)context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            if(main.getUpdate()!=null && main.getUpdate().isCancelled()==true)
            {
                main.startAsyntask();
            }
        }
    }
}
