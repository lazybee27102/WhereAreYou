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

/**
 * Created by Administrator on 28/12/2015.
 */
public class AddFriendRequestAsyncTask extends AsyncTask<String,Void,Boolean> {
    private Context context;
    private ProgressDialog progressDialog;
    private final String URL = "http://whereareyou.somee.com/WebService.asmx";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_ADDFRIENDREQUEST = "FRIENDREQUEST_AddFriendRequest";
    private final String METHOD_GETLASTFRQ = "FRIENDREQUEST_GetLastFriendRequest";
    private final String METHOD_GETALLFRIEND= "FRIEND_getAllFriendByUserName";

    Boolean isAlReadyHaveFriend = false;

    public AddFriendRequestAsyncTask(Context context, getResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Add Friend...");
        progressDialog.show();
    }

    private getResponse delegate;

    public interface getResponse
    {
        void getResponse(Boolean a);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String from = params[0];
        String to = params[1];
        //Check If AllReadyHave Friend


        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            SoapObject getallFriend = new SoapObject(NAMESPACE,METHOD_GETALLFRIEND);
            getallFriend.addProperty("name",from);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(getallFriend);
            httpTransportSE.call(NAMESPACE + METHOD_GETALLFRIEND, envelope);
            SoapObject soapArray  = (SoapObject) envelope.getResponse();


            if(soapArray.getPropertyCount()!=0)
            {
                for(int i = 0;i<soapArray.getPropertyCount();i++)
                {
                    SoapObject item  = (SoapObject) soapArray.getProperty(i);
                    String friendName = item.getPropertyAsString("FriendId");
                    if(friendName.equals(to))
                    {
                        isAlReadyHaveFriend = true;
                        return false;
                    }
                }
            }

            if(!isAlReadyHaveFriend)
            {
                SoapObject getLastFriendRequest = new SoapObject(NAMESPACE,METHOD_GETLASTFRQ);

                envelope.setOutputSoapObject(getLastFriendRequest);

                httpTransportSE.call(NAMESPACE + METHOD_GETLASTFRQ, envelope);
                SoapObject lastFrq = (SoapObject) envelope.getResponse();


                SoapObject request = new SoapObject(NAMESPACE,METHOD_ADDFRIENDREQUEST);
                SoapObject newFriendRequest = new SoapObject(NAMESPACE,"friendrq");


                if(lastFrq!=null)
                {
                    String lastId = lastFrq.getPropertyAsString("RequestId");
                    String[] word = lastId.split("T");
                    newFriendRequest.addProperty("RequestId","REQUEST"+(Integer.parseInt(word[1].toString())+1)+"");
                }else
                {
                    newFriendRequest.addProperty("RequestId","REQUEST1");
                }
                newFriendRequest.addProperty("RequestFrom",from);
                newFriendRequest.addProperty("RequestTo",to);
                newFriendRequest.addProperty("RequestStatus", 0);

                request.addSoapObject(newFriendRequest);

                envelope.setOutputSoapObject(request);

                httpTransportSE.call(NAMESPACE+METHOD_ADDFRIENDREQUEST,envelope);

                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                Boolean isOk = Boolean.parseBoolean(soapPrimitive.toString());
                return isOk;
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
