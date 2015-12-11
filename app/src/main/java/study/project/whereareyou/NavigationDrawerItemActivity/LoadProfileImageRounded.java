package study.project.whereareyou.NavigationDrawerItemActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

import study.project.whereareyou.OtherUsefullClass.RoundImage;

/**
 * Created by Administrator on 24/11/2015.
 */
public class LoadProfileImageRounded extends AsyncTask<String,Void,Bitmap>{
    ImageView view;
    public LoadProfileImageRounded(ImageView imageView) {
        this.view = imageView;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String urlDisplay = params[0];
        Bitmap Icon = null;
        try
        {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            Icon = BitmapFactory.decodeStream(in);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return Icon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        RoundImage roundImage = new RoundImage(bitmap);
        view.setImageDrawable(roundImage);
    }
}
