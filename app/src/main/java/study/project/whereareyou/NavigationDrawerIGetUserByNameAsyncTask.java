package study.project.whereareyou.NavigationDrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 16/12/2015.
 */
public class GetUserByNameAsyncTask extends AsyncTask<String,Void,User> {
    final String URL = "http://whereareyou.somee.com/WebService.asmx?op=DeleteUserByName";
    final String NAMESPACE ="http://tempuri.org/";
    final String METHOD_GETUSER = "GetUserByName";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;



    public interface GetUserByNameAsyncTaskResponse
    {
        void processResponse(User user);
    }

    GetUserByNameAsyncTaskResponse delegate;
    Context context;
    ProgressDialog progressDialog;

    public GetUserByNameAsyncTask(Context context,GetUserByNameAsyncTaskResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting Your Information....");
        progressDialog.show();
    }

    @Override
    protected User doInBackground(String... params) {
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

            User currentUser = new User();
            currentUser.setId(object.getPropertyAsString("UserId"));
            currentUser.setEmail(object.getPropertyAsString("UserEmail"));
            currentUser.setName(object.getPropertyAsString("UserName"));
            currentUser.setLastLocation(object.getPropertyAsString("UserLasLocation"));
            //currentUser.setBirthDate("");

            return currentUser;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        delegate.processResponse(user);
    }
}
