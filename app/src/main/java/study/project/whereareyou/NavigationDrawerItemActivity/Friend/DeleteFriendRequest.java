package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 30/12/2015.
 */
public class DeleteFriendRequest extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... params) {
        String from = params[0];
        String to = params[1];
        SoapObject request = new SoapObject("http://tempuri.org/","FRIENDREQUEST_DeleteFriendRequestfrom_FromAndTo");
        request.addProperty("from",from);
        request.addProperty("to", to);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE("http://whereareyou.somee.com/WebService.asmx");
        try {
            httpTransportSE.call("http://tempuri.org/" + "FRIENDREQUEST_DeleteFriendRequestfrom_FromAndTo",envelope);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }
}
