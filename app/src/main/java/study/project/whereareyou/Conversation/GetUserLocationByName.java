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

import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 16/12/2015.
 */
public class GetUserLocationByName extends AsyncTask<String,Void,String> {
    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String NAMESPACE ="http://tempuri.org/";
    final String METHOD_GETUSER = "USER_GetUserByName";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;

    ProgressDialog progressDialog;

    public interface GetUserByNameAsyncTaskResponse
    {
        void processResponse(String string);
    }

    GetUserByNameAsyncTaskResponse delegate;
    Context context;


    public GetUserLocationByName(Context context, String textInProgressDialog, GetUserByNameAsyncTaskResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(textInProgressDialog);
        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        String userName = params[0];
        SoapObject request = new SoapObject(NAMESPACE,METHOD_GETUSER);
        request.addProperty("name",userName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call(SOAP_ACTION,envelope);
            SoapObject object = (SoapObject) envelope.getResponse();

            String UserlastLocation = "";
            if(object!=null)
            {
                SoapPrimitive lastLocation = (SoapPrimitive) object.getPropertySafely("UserLastLocation",null);
                if(lastLocation!=null)
                    UserlastLocation = lastLocation.toString();
            }

            return UserlastLocation;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        delegate.processResponse(string);
    }
}
