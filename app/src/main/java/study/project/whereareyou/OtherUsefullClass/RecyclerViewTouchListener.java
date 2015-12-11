package study.project.whereareyou.OtherUsefullClass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 29/10/2015.
 */
public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    ClickListener clickListener;

    public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView,ClickListener clickListener)
    {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener()
        {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(android.support.v7.widget.RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        RecyclerView.ViewHolder finalChild = rv.findViewHolderForAdapterPosition(rv.getChildAdapterPosition(child));
        if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)==true)
        {
            clickListener.onClick(finalChild,rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(android.support.v7.widget.RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
