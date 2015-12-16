package study.project.whereareyou.SignIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import study.project.whereareyou.MainActivity;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.MyEncoder;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class SignInActivity extends Activity{
    EditText editText_userName;
    EditText editText_passWord;
    Button button_SignIn,button_SignOut;
    CheckBox checkBox_rememberMe;
    TextView textView_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        registerWidget();
        DetectCheckBox();
        setEvent();
    }

    private void registerWidget() {
        editText_passWord = (EditText) findViewById(R.id.editText_SignIn_PassWord);
        editText_userName = (EditText) findViewById(R.id.editText_SignIn_UserName);
        button_SignIn = (Button) findViewById(R.id.button_SignIn);
        checkBox_rememberMe = (CheckBox) findViewById(R.id.checkBox_SignIn);
        textView_register = (TextView) findViewById(R.id.textView_SignIn_Register);
        button_SignOut = (Button) findViewById(R.id.button_SignOut);
    }

    private void DetectCheckBox() {
        Boolean boo =  Boolean.parseBoolean(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"REMEMBER","false"));
        checkBox_rememberMe.setChecked(boo);
        if(checkBox_rememberMe.isChecked())
        {
            editText_userName.setText(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"USER",""));
            editText_passWord.setText(SharedPreference.ReadFromSharedPreference(getApplicationContext(),"PASSWORD",""));
        }
    }

    private void setEvent() {

        //LOGIN
        button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (editText_userName.getText().length() == 0 || editText_passWord.getText().length() == 0) {
                        Message.printMessage(SignInActivity.this, "You must type your UserName and Password");
                    } else {
                        String userName = editText_userName.getText().toString().trim();
                        String passWord = editText_passWord.getText().toString().trim();

                        byte[] fileByte = new byte[0];
                        try {
                            String resultPasswordEncoded = MyEncoder.encodeFile(passWord);


                            new SignInAsyncTask(SignInActivity.this, new SignInAsyncTask.AsyncResponse() {
                                @Override
                                public void processFinish(Boolean boo) {
                                    if (boo)
                                        UpdateUI(true);
                                    else
                                        UpdateUI(false);
                                }
                            }).execute(new String[]{userName, resultPasswordEncoded});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            }
        });

        button_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_passWord.setText("");
                editText_userName.setText("");
                SharedPreference.WritetoSharePreference(getApplicationContext(), "USER", "");
                SharedPreference.WritetoSharePreference(getApplicationContext(),"PASSWORD","");
                Message.printMessage(SignInActivity.this, "You have logged out!");
            }
        });

        //REGISTER
        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        //CHECKBOX
        checkBox_rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreference.WritetoSharePreference(getApplicationContext(), "REMEMBER", "true");
                } else {
                    SharedPreference.WritetoSharePreference(getApplicationContext(), "REMEMBER", "false");
                }
            }
        });
    }





    private void UpdateUI(boolean b) {
        if(b)
        {

            SharedPreference.WritetoSharePreference(this,"USER",editText_userName.getText().toString().trim());
            SharedPreference.WritetoSharePreference(this, "PASSWORD", editText_passWord.getText().toString().trim());
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }else
        {
            Message.printMessage(this,"Your UserName or PassWord wrong");
        }
    }




}
