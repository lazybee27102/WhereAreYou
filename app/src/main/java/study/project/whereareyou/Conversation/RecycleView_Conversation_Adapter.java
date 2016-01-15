package study.project.whereareyou.Conversation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.typer.Font;
import io.github.typer.Typer;
import study.project.whereareyou.R;



/**
 * Created by Administrator on 26/10/2015.
 */
public class RecycleView_Conversation_Adapter extends RecyclerView.Adapter<RecycleView_Conversation_Adapter.ConversationViewHolder> {
    private ArrayList<ConversationInfo> listConversation;
    Context context;
    public RecycleView_Conversation_Adapter(Context context,ArrayList<ConversationInfo> list)
    {
        listConversation = list;
        this.context = context;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupchat,parent,false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        ConversationInfo ci = listConversation.get(position);
        holder.textView_name_key_user.setText(ci.getName());
        holder.textView_name_key_user.setTypeface(Typer.set(context).getFont(Font.ROBOTO_BOLD));

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<ci.getAllGuessuser().size();i++)
        {
            if(i == ci.getAllGuessuser().size()-1)
                stringBuilder.append(ci.getAllGuessuser().get(i) + "");
            else
                stringBuilder.append(ci.getAllGuessuser().get(i)+" - ");
        }


        holder.textView_detail.setText(stringBuilder);
        holder.textView_detail.setTypeface(Typer.set(context).getFont(Font.ROBOTO_LIGHT));
        holder.textView_number_user.setText(ci.getAllGuessuser().size()+"");



    }

    @Override
    public int getItemCount() {
        return listConversation.size();
    }


    public class ConversationViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView_name_key_user;
        protected TextView textView_detail;
        protected TextView textView_number_user;




        public ConversationViewHolder(View itemView) {
            super(itemView);
            textView_name_key_user = (TextView)itemView.findViewById(R.id.textView_friendchanel_name);
            textView_detail = (TextView)itemView.findViewById(R.id.textView_detail);
            textView_number_user = (TextView) itemView.findViewById(R.id.textView_number_user);
        }
    }
}
