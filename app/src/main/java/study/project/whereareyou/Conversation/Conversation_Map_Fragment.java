package study.project.whereareyou.Conversation;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.project.whereareyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Conversation_Map_Fragment extends android.support.v4.app.Fragment {


    public Conversation_Map_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation__map_, container, false);
    }


}
