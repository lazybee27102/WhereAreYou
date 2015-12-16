package study.project.whereareyou;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.MyEncoder;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;

public class SignInActivity extends Activity{
    EditText editText_userName;
    EditText editText_passWord;
    Button button_SignIn;
    CheckBox checkBox_rememberMe;
    TextView textView_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        registerWidget();
        setEvent();
    }

    private void setEvent() {
        button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_userName.getText().length()==0 || editText_passWord.getText().length()==0)
                {
                    Message.printMessage(SignInActivity.this,"You must type your UserName and Password");
                }else
                {
                    String userName = editText_userName.getText().toString().trim();
                    String passWord = editText_passWord.getText().toString().trim();

                    byte[] fileByte = new byte[0];
                    try {
                        String resultPasswordEncoded = MyEncoder.encodeFile(passWord);



                        new SingInAsyncTast(SignInActivity.this).execute(new String[]{userName,resultPasswordEncoded});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        checkBox_rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    SharedPreference.WritetoSharePreference(getApplicationContext(),"REMEMBER","true");
                }else {
                    SharedPreference.WritetoSharePreference(getApplicationContext(),"REMEMBER","false");
                }
            }
        });
    }

    private void registerWidget() {
        editText_passWord = (EditText) findViewById(R.id.editText_SignIn_PassWord);
        editText_userName = (EditText) findViewById(R.id.editText_SignIn_UserName);
        button_SignIn = (Button) findViewById(R.id.button_SignIn);
        checkBox_rememberMe = (CheckBox) findViewById(R.id.checkBox_SignIn);
        textView_register = (TextView) findViewById(R.id.textView_SignIn_Register);
    }


    private class SingInAsyncTast extends AsyncTask<String,Void,Boolean>{
        ProgressDialog progressDialog;
        Context context;
        Boolean isCorrect;

        final String URL = "http://whereareyou.somee.com/WebService.asmx";
        final String NAMESPACE = "http://tempuri.org/";
        final String METHOD_GETUSER  = "GetUserByName";
        final String SOAP_ACTION = NAMESPACE+METHOD_GETUSER;


        public SingInAsyncTast(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Signing In...");
            progressDialog.show();
            this.context = context;
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
                Log.d("PASSTYPE",pass);
                Log.d("PASSSQL",passwordfromSoapObject);
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

            if(boo)
            {
                Intent i = new Intent(context,MainActivity.class);
                startActivity(i);
            }else{
                Message.printMessage(context,"Wrong UserName or Password");
            }
        }
    }
}
