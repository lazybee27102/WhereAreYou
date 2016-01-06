package study.project.whereareyou.Conversation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 05/01/2016.
 */
public class UpdateLocationAsyncTask extends AsyncTask<String, Void, Void> {
    private Context context;

    private final String URL = "http://whereareyou.somee.com/WebService.asmx";
    private final String METHOD = "USER_UpdateUserLocation";
    private final String NAMESPACE  = "http://tempuri.org/";
    private String userName;

    public UpdateLocationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        userName = params[0];
        for (int i = 0; i >= 0; i++) {
            if (!this.isCancelled()) {
                Log.d("HELLOO","LOOEELL");
                LatLng location = study.project.whereareyou.Location.getMyLocation(context);
                SoapObject request = new SoapObject(NAMESPACE,METHOD);
                request.addProperty("name",userName);
                request.addProperty("lastLocation",location.latitude + "-"+location.longitude);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
                try {
                    httpTransportSE.call(NAMESPACE+METHOD,envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }else
            {
                break;
            }

            SystemClock.sleep(10000);
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void aBoolean) {
        super.onPostExecute(aBoolean);
    }


}
