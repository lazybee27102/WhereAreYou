package study.project.whereareyou.Conversation.Chanel;

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

/**
 * Created by Administrator on 12/01/2016.
 */
public class AddChanelAsyncTask extends AsyncTask<ArrayList<String>,Void,Boolean> {
    private static final String URL = "http://whereareyou.somee.com/WebService.asmx";
    private static final String METHOD_ADDCHANEL = "CHANEL_AddChanel";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_GETLASTCHANELID = "CHANEL_GetLastChanelId";

    private ProgressDialog dialog;
    private Context context;
    private getResponse delegate;
    private String ChanelName;
    public interface getResponse
    {
        void processResponse(Boolean aBoolean);
    }

    public AddChanelAsyncTask(Context context,String ChanelName,getResponse delegate) {
        this.ChanelName = ChanelName;
        this.context = context;
        this.delegate = delegate;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Creating...");
        dialog.show();
    }


    @Override
    protected Boolean doInBackground(ArrayList<String>... params) {
        ArrayList<String> allUsers = params[0];
        SoapObject request = new SoapObject(NAMESPACE,METHOD_GETLASTCHANELID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call(NAMESPACE+METHOD_GETLASTCHANELID,envelope);
            SoapObject id = (SoapObject) envelope.getResponse();

            Integer lastId =  1;
            if(id != null && id.getPropertyAsString("ChanelId")!=null && id.getPropertyAsString("ChanelId").length()!=0)
            {
                String[] words = id.getPropertyAsString("ChanelId").split("L");
                lastId = Integer.parseInt(words[1])+1;
            }
            for(int i = 0;i<allUsers.size();i++)
            {
                SoapObject request1 = new SoapObject(NAMESPACE,METHOD_ADDCHANEL);
                SoapObject parameter = new SoapObject(NAMESPACE,"chan");
                parameter.addProperty("ChanelId","CHANEL"+(lastId));
                parameter.addProperty("ChanelName",ChanelName);
                parameter.addProperty("ChannelUserName", allUsers.get(i));
                request1.addSoapObject(parameter);

                envelope.setOutputSoapObject(request1);
                httpTransportSE.call(NAMESPACE + METHOD_ADDCHANEL, envelope);
                SoapPrimitive boo = (SoapPrimitive) envelope.getResponse();
                if(Boolean.parseBoolean(boo.toString())==false)
                    return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (dialog.isShowing())
            dialog.dismiss();
        delegate.processResponse(aBoolean);

    }
}
