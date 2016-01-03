package study.project.whereareyou.OtherUsefullClass;

import android.content.Context;
import android.widget.Toast;

import study.project.whereareyou.NavigationDrawerItemActivity.Friend.FriendsActivity;

/**
 * Created by Administrator on 23/11/2015.
 */
public class Message {
    private Context context;
    public static void printMessage(Context context,String mess)
    {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }


}
