package study.project.whereareyou.SignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import study.project.whereareyou.MainActivity;
import study.project.whereareyou.OtherUsefullClass.Message;

/**
 * Created by Administrator on 16/12/2015.
 */
class SignInAsyncTask extends AsyncTask<String,Void,Boolean> {

    ProgressDialog progressDialog;
    Context context;
    Boolean isCorrect;

    public interface AsyncResponse
    {
        void processFinish(Boolean boo);
    }

    AsyncResponse delegate = null;
    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String NAMESPACE = "http://tempuri.org/";
    final String METHOD_GETUSER  = "USER_GetUserByName";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;


    public SignInAsyncTask(Context context,AsyncResponse delegate) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String user = params[0];
        String pass = params[1];

        SoapObject request = new SoapObject(NAMESPACE,METHOD_GETUSER);
        request.addProperty("name",user);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            SoapObject object = (SoapObject) envelope.getResponse();

            String passwordfromSoapObject = object.getPropertyAsString("UserPassWord");

            if(pass.equals(passwordfromSoapObject))
                isCorrect= true;
            else
                isCorrect = false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return isCorrect;
    }


    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Boolean boo) {
        super.onPostExecute(boo);
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        delegate.processFinish(boo);
    }
}