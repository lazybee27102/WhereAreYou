package study.project.whereareyou.NavigationDrawerItemActivity.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import study.project.whereareyou.OtherUsefullClass.Message;

/**
 * Created by Administrator on 05/01/2016.
 */
public class UpdateInformationAsyncTask extends AsyncTask<String,Void,Boolean>
{
    final String URL = "http://whereareyou.somee.com/WebService.asmx";
    final String NAMESPACE = "http://tempuri.org/";
    final String METHOD_UPDATEINFORMATION = "USER_UpdateUserInformation";

    ProgressDialog progressDialog;
    Context context;

    public UpdateInformationAsyncTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating your Information");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        //Name,Fname,Lname,last,Birthday
        String nameInsert = params[0];
        String fname = params[1];
        String lname = params[2];
        String lastLocation = "Don't know";


        if(params[3].toString().trim().length()!=0)
        {
            lastLocation = params[3];
        }
        String birthdate = DateTime.parse(params[4]).toString();




        try {

            SoapObject request = new SoapObject(NAMESPACE,METHOD_UPDATEINFORMATION);
            request.addProperty("name",nameInsert);
            request.addProperty("fName",fname);
            request.addProperty("lName",lname);
            request.addProperty("birthDate",birthdate);
            request.addProperty("lastLocation", lastLocation);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            MarshalFloat marshal = new MarshalFloat();
            marshal.register(envelope);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);


            httpTransportSE.call(NAMESPACE +METHOD_UPDATEINFORMATION,envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(soapPrimitive.toString());

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
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        if(aBoolean)
            Message.printMessage(context, "Update Information Successfully");
        else
            Message.printMessage(context,"Update Failed");

    }

}
