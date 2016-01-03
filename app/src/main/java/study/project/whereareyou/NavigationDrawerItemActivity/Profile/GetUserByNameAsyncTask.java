package study.project.whereareyou.NavigationDrawerItemActivity.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
public class GetUserByNameAsyncTask extends AsyncTask<String,Void,User> {
    final String URL = "http://whereareyou.somee.com/WebService.asmx?op=DeleteUserByName";
    final String NAMESPACE ="http://tempuri.org/";
    final String METHOD_GETUSER = "USER_GetUserByName";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;

    ProgressDialog progressDialog;

    public interface GetUserByNameAsyncTaskResponse
    {
        void processResponse(User user);
    }

    GetUserByNameAsyncTaskResponse delegate;
    Context context;


    public GetUserByNameAsyncTask(Context context,String textInProgressDialog,GetUserByNameAsyncTaskResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(textInProgressDialog);
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

            User currentUser = null;
            if(object!=null)
            {

                currentUser = new User();
                SoapPrimitive id = (SoapPrimitive) object.getPropertySafely("UserId", null);
                SoapPrimitive email = (SoapPrimitive) object.getPropertySafely("UserEmail",null);
                SoapPrimitive name = (SoapPrimitive) object.getPropertySafely("UserName",null);
                SoapPrimitive fname = (SoapPrimitive) object.getPropertySafely("UserFirstName",null);
                SoapPrimitive lname = (SoapPrimitive) object.getPropertySafely("UserLastName",null);
                SoapPrimitive lastLocation = (SoapPrimitive) object.getPropertySafely("UserLastLocation",null);

                SoapPrimitive birthdate = (SoapPrimitive) object.getPropertySafely("UserBirthdate",null);

                if(id!=null)
                    currentUser.setId(id.toString());
                if(email!=null)
                    currentUser.setEmail(email.toString());
                if(name!=null)
                    currentUser.setName(name.toString());
                if(fname!=null)
                    currentUser.setFirstName(fname.toString());
                if(lname!=null)
                    currentUser.setLastName(lname.toString());
                if(lastLocation!=null)
                    currentUser.setLastLocation(lastLocation.toString());
                if(birthdate!=null)
                    currentUser.setBirthDate(birthdate.toString().substring(0,10));
            }

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
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        delegate.processResponse(user);

    }
}
