package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

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

import study.project.whereareyou.OtherUsefullClass.SharedPreference;

/**
 * Created by Administrator on 29/12/2015.
 */
public class AddFriendAsyncTask extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private ProgressDialog progressDialog;
    private getResponse delegate;
    public AddFriendAsyncTask(Context context,getResponse delegate) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Add this Friend...");
        progressDialog.show();
        this.delegate = delegate;

    }

    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String METHOD_ADDFRIEND = "FRIEND_AddFriend";
    final String METHOD_FIXSTATUSFRIEND_REQUEST = "FRIENDREQUEST_FixStatusFriendRequest";
    final String NAME_SPACE = "http://tempuri.org/";

    public interface getResponse
    {
        void getResponse(boolean a);
    }
    @Override
    protected Boolean doInBackground(String... params) {
        String from = params[0];
        String to  = SharedPreference.ReadFromSharedPreference(context,"USER","");
        SoapObject request = new SoapObject(NAME_SPACE,METHOD_ADDFRIEND);
        SoapObject friend = new SoapObject(NAME_SPACE,"friend");
        friend.addProperty("UserId",from);
        friend.addProperty("FriendId",to);
        request.addSoapObject(friend);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call(NAME_SPACE + METHOD_ADDFRIEND, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            if(Boolean.parseBoolean(response.toString()))
            {
                SoapObject request1 = new SoapObject(NAME_SPACE,METHOD_ADDFRIEND);
                SoapObject friend1 = new SoapObject(NAME_SPACE,"friend");
                friend1.addProperty("UserId",to);
                friend1.addProperty("FriendId",from);

                request1.addSoapObject(friend1);
                envelope.setOutputSoapObject(request1);
                httpTransportSE.call(NAME_SPACE + METHOD_ADDFRIEND, envelope);
                SoapPrimitive response1 = (SoapPrimitive) envelope.getResponse();

                return Boolean.parseBoolean(response1.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.getResponse(aBoolean);
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
