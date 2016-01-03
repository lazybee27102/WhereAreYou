package study.project.whereareyou.SignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.RegisterSectionInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.MyEncoder;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class RegisterActivity extends AppCompatActivity {
    EditText editText_userName,editText_email,editText_password,editText_retypePassword;
    Button button_Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerWidget();
        setEvent();

    }

    private void setEvent() {
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            StringBuilder allFouls = new StringBuilder("");
            if(editText_userName.getText().toString().trim().length()==0)
                allFouls.append("- Please Insert Your UserName \n");
            else
            {
                if(editText_userName.getText().toString().trim().length()<6 || editText_userName.getText().toString().trim().length()>12)
                allFouls.append("The number of characters of UserName is 6-12 \n");
            }


            if(editText_email.getText().toString().trim().length()==0)
                allFouls.append("- Please Insert Your Email \n");
            else
            {
                if(!isEmailValid(editText_email.getText().toString().trim()))
                    allFouls.append("- Wrong Email Address \n");
            }

            if(editText_password.getText().toString().trim().length()==0)
                allFouls.append("- Please Insert Your PassWord \n");
            else
            {
                if(editText_password.getText().toString().trim().length()<6 || editText_password.getText().toString().trim().length()>12)
                    allFouls.append("The number of characters of Password is 6-12 \n");
            }


            if(editText_retypePassword.getText().toString().trim().length()==0)
                allFouls.append("- Please retype Your PassWord \n");

            if(editText_password.getText().toString().trim().length()!=0 && editText_retypePassword.getText().toString().trim().length()!=0 && !editText_password.getText().toString().trim().equals(editText_retypePassword.getText().toString().trim()) )
                allFouls.append("- Your passwords isnot matched \n");

            if(allFouls.toString().equals(""))
            {
                new RegisterAsyncTask(RegisterActivity.this).execute(new String[]{String.valueOf(editText_userName.getText().toString().trim()),
                        String.valueOf(editText_email.getText().toString().trim()),
                        String.valueOf(editText_password.getText().toString().trim())});
            }else
            {
                Toast.makeText(RegisterActivity.this,allFouls.toString(), Toast.LENGTH_LONG).show();
            }







            }
        });
    }

    private void registerWidget() {
        editText_userName = (EditText) findViewById(R.id.editText_Register_UserName);
        editText_email = (EditText) findViewById(R.id.editText_Register_Email);
        editText_password = (EditText) findViewById(R.id.editText_Register_Password);
        editText_retypePassword = (EditText) findViewById(R.id.editText_Register_PasswordRetype);
        button_Register = (Button) findViewById(R.id.button_Register);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private class RegisterAsyncTask extends AsyncTask<String, Boolean, String> {
        MySqlOpenHelper helper;
        ProgressDialog progressDialog;
        boolean isOK;

        Context context;
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_ADDUSER ="USER_AddUserWithId";
        final String METHOD_GETLASTUSER = "USER_GetLastUser";
        final String URL="http://whereareyou.somee.com/WebService.asmx";
        final String SOAP_ACTION_GETLASTUSER  = NAMESPACE+METHOD_GETLASTUSER;
        final String SOAP_ACTION_ADDUSER = NAMESPACE+METHOD_ADDUSER;

        public RegisterAsyncTask(Context context) {
            helper = new MySqlOpenHelper(context);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_GETLASTUSER);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            SoapObject lastUser = null;
            try {
                httpTransportSE.call(SOAP_ACTION_GETLASTUSER,envelope);
                lastUser = (SoapObject) envelope.getResponse();

                if(lastUser!=null)
                {
                    SoapObject request1 = new SoapObject(NAMESPACE,METHOD_ADDUSER);
                    SoapObject newUser = new SoapObject(NAMESPACE,"user");

                    String idOfLastUser = lastUser.getProperty("UserId").toString();
                    String[] idDevider = idOfLastUser.split("R");
                    int nextNumberId = Integer.parseInt(idDevider[1])+1;
                    String newUserId = "USER" + nextNumberId;
                    newUser.addProperty("UserId",newUserId);
                    newUser.addProperty("UserEmail",params[1]);
                    newUser.addProperty("UserName",params[0]);

                    //MAHOA
                    String resultPasswordEncoded = MyEncoder.encodeFile(params[2]);

                    newUser.addProperty("UserPassWord",resultPasswordEncoded);
                    //newUser.addProperty("UserLasLocation", "");

                    request1.addSoapObject(newUser);

                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope1.dotNet = true;
                    envelope1.setOutputSoapObject(request1);

                    httpTransportSE.call(SOAP_ACTION_ADDUSER, envelope1);

                    SoapPrimitive soapPrimitive = (SoapPrimitive) envelope1.getResponse();
                    isOK = Boolean.parseBoolean(soapPrimitive.toString());
                    publishProgress(isOK);

                }else {
                    SoapObject request1 = new SoapObject(NAMESPACE,METHOD_ADDUSER);
                    SoapObject newUser = new SoapObject(NAMESPACE,"user");

                    String id = "USER1";

                    newUser.addProperty("UserId",id);
                    newUser.addProperty("UserEmail", params[1]);
                    newUser.addProperty("UserName",params[0]);

                    //MAHOA

                    String resultPasswordEncoded = MyEncoder.encodeFile(params[2]);


                    newUser.addProperty("UserPassWord",resultPasswordEncoded);
                    //newUser.addProperty("UserLasLocation", "");

                    request1.addSoapObject(newUser);

                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope1.dotNet = true;
                    envelope1.setOutputSoapObject(request1);

                    httpTransportSE.call(SOAP_ACTION_ADDUSER, envelope1);

                    SoapPrimitive soapPrimitive = (SoapPrimitive) envelope1.getResponse();
                    isOK = Boolean.parseBoolean(soapPrimitive.toString());
                    publishProgress(isOK);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }



        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if(isOK)
            {
                Message.printMessage(context,"Register Sucessfully");

                finish();
            }
            else
                Message.printMessage(context,"Register Failed");
        }



        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }


    }


}
