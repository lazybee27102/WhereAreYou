package study.project.whereareyou.Conversation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 16/12/2015.
 */
public class GetUserLocationByName extends AsyncTask<ArrayList<String>,Void,HashMap<String,String>> {
    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String NAMESPACE ="http://tempuri.org/";
    final String METHOD_GETUSER = "USER_GetUserByName";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;


    ProgressDialog progressDialog;
    Context context;
    getResponse delegate;

    public interface getResponse
    {
        void ProcessHashMap(HashMap<String,String> hashMap);
    }

    public GetUserLocationByName(Context context,getResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Location...");
    }

    @Override
    protected HashMap<String,String> doInBackground(ArrayList<String>... params) {
        ArrayList<String> userNames  = params[0];
        HashMap<String,String> hashMap = new HashMap<>();




        for(String userName : userNames)
        {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_GETUSER);
            request.addProperty("name",userName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            try {
                httpTransportSE.call(SOAP_ACTION,envelope);
                SoapObject object = (SoapObject) envelope.getResponse();
                SoapPrimitive lastLocation = (SoapPrimitive) object.getPropertySafely("UserLastLocation", null);
                if(lastLocation!=null)
                {
                    hashMap.put(userName,lastLocation.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return hashMap;
    }

    @Override
    protected void onPostExecute(HashMap<String,String> hashMap) {
        super.onPostExecute(hashMap);
        delegate.ProcessHashMap(hashMap);
        if(progressDialog.isShowing())
            progressDialog.dismiss();

    }
}
