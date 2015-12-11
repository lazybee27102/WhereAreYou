package study.project.whereareyou.NavigationDrawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.typer.Font;
import io.github.typer.Typer;
import study.project.whereareyou.R;

/**
 * Created by Administrator on 26/10/2015.
 */
public class NavigationRecycleViewAdapter extends RecyclerView.Adapter<NavigationRecycleViewAdapter.MyViewHolder> {
    private List<NavigationDrawerItem> items = Collections.emptyList();
    Context context;
    private LayoutInflater layoutInflater;

    public NavigationRecycleViewAdapter(Context context,List<NavigationDrawerItem> items) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.navigation_drawer_item, parent, false);
        NavigationRecycleViewAdapter.MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        NavigationDrawerItem item = items.get(position);
        holder.imageView.setImageResource(item.getImageResourceId());
        holder.textView_name.setText(item.getTitle());
        holder.textView_name.setTypeface(Typer.set(context).getFont(Font.ROBOTO_BLACK));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView_name;
        TextView textView_detail;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_image_item);
            textView_name = (TextView) itemView.findViewById(R.id.textView_name_item);
        }
    }
}
