package study.project.whereareyou.NavigationDrawerItemActivity.Friend;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 03/01/2016.
 */
public class GetAllFriendAsyncTask extends AsyncTask<String,Void,ArrayList<String>> {
    private Context context;
    private ProgressDialog dialog;
    private getResponse delegate;

    private final String URL = "http://whereareyou.somee.com/WebService.asmx";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD =  "FRIEND_getAllFriendByUserName";

    ArrayList<String> arr;


    public GetAllFriendAsyncTask(Context context,getResponse delegate) {
        this.context = context;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Get All Friend...");
        arr = new ArrayList<>();
        this.delegate = delegate;

    }

    public interface getResponse
    {
        void ProcessResponse(ArrayList<String> arr);
    }


    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if(params[0]==null || params[0].toString().trim().length()==0)
        {
            return null;
        }

        SoapObject request = new SoapObject(NAMESPACE,METHOD);
        request.addProperty("name",params[0]);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call(NAMESPACE+METHOD,envelope);
            SoapObject arrSoap = (SoapObject) envelope.getResponse();
            if(arrSoap!=null)
            {
                for(int i = 0;i<arrSoap.getPropertyCount();i++)
                {
                    SoapObject item = (SoapObject) arrSoap.getProperty(i);
                    String name =  item.getPropertyAsString("FriendId");
                    arr.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return arr;
    }

    @Override
    protected void onPostExecute(ArrayList<String> arr) {
        super.onPostExecute(arr);
        delegate.ProcessResponse(arr);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
