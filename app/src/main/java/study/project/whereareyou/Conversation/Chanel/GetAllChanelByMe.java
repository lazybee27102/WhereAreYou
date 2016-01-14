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

import study.project.whereareyou.Conversation.ConversationInfo;
import study.project.whereareyou.OOP.User;

/**
 * Created by Administrator on 16/12/2015.
 */
public class GetAllChanelByMe extends AsyncTask<String,Void,ArrayList<ConversationInfo>> {
    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String NAMESPACE ="http://tempuri.org/";
    final String METHOD_GETALLCHANELHAVEME = "CHANEL_GetAllChanelHaveMe";
    final String SOAP_ACTION = NAMESPACE+METHOD_GETALLCHANELHAVEME;
    final String METHOD_GETALLUSERNAMEFROMCHANEL = "CHANEL_GetAllUserNammFromChanelName";
    final String SOAP_ACTION1 = NAMESPACE+ METHOD_GETALLUSERNAMEFROMCHANEL;


    public interface GetResponse
    {
        void processResponse(ArrayList<ConversationInfo> user);
    }

    GetResponse delegate;
    Context context;
    ProgressDialog progressDialog;

    public GetAllChanelByMe(Context context, GetResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting All Conversations...");
        progressDialog.show();


    }

    @Override
    protected ArrayList<ConversationInfo> doInBackground(String... params) {
        ArrayList<String> allChanels = new ArrayList<>();
        ArrayList<ConversationInfo> result = new ArrayList<>();
        String userName = params[0];
        SoapObject request = new SoapObject(NAMESPACE,METHOD_GETALLCHANELHAVEME);
        request.addProperty("userName",userName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call(SOAP_ACTION,envelope);
            SoapObject arr = (SoapObject) envelope.getResponse();
            if(arr!=null)
            {
                for (int i = 0;i<arr.getPropertyCount();i++)
                {
                    SoapObject item = (SoapObject) arr.getProperty(i);
                    String ChanelName = item.getPropertyAsString("ChanelName");
                    allChanels.add(ChanelName);
                }
            }

            if(allChanels.size()==0)
                return null;
            else
            {
                for (int i = 0;i<allChanels.size();i++)
                {
                    ConversationInfo info = new ConversationInfo();
                    info.setName(allChanels.get(i));
                    SoapObject request1 = new SoapObject(NAMESPACE,METHOD_GETALLUSERNAMEFROMCHANEL);
                    request1.addProperty("chanelName",info.getName());
                    envelope.setOutputSoapObject(request1);

                    HttpTransportSE httpTransportSE1 = new HttpTransportSE(URL);
                    httpTransportSE1.call(SOAP_ACTION1,envelope);
                    SoapObject arrString = (SoapObject) envelope.getResponse();
                    if(arrString!=null)
                    {
                        for (int y = 0;y<arrString.getPropertyCount();y++)
                        {
                            SoapPrimitive item = (SoapPrimitive) arrString.getProperty(y);
                            String UserInSide = item.toString();
                            info.getAllGuessuser().add(UserInSide);
                        }
                        result.add(info);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<ConversationInfo> user) {
        super.onPostExecute(user);
        delegate.processResponse(user);
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
