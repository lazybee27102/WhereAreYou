package study.project.whereareyou.NavigationDrawerItemActivity.Profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;

import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;
import study.project.whereareyou.OtherUsefullClass.SharedPreference;
import study.project.whereareyou.R;
import study.project.whereareyou.SqlHelper.MySqlOpenHelper;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView textView_profile_email;
    TextView textView_profile_name;
    TextView textView_profile_birthdate;
    EditText editText_fName,editText_lName;
    Button button_datePicker,button_updateInformation;
    TextView textView_lasLocation;


    User currentUser;

    MySqlOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        helper= new MySqlOpenHelper(this);
        setWidget();
        setEvent();
        new GetUserByNameAsyncTask(this,"Getting your information", new GetUserByNameAsyncTask.GetUserByNameAsyncTaskResponse() {
            @Override
            public void processResponse(User user) {
                currentUser = user;
                if(currentUser.getName()!=null)
                    textView_profile_name.setText(currentUser.getName());
                if(currentUser.getBirthDate()!=null)
                    textView_profile_birthdate.setText(currentUser.getBirthDate());
                if(currentUser.getEmail()!=null)
                    textView_profile_email.setText(currentUser.getEmail());
                if (currentUser.getFirstName()!=null)
                    editText_fName.setText(currentUser.getFirstName());
                if(currentUser.getLastName()!=null)
                    editText_lName.setText(currentUser.getLastName());
                if(currentUser.getLastLocation()!=null)
                    textView_lasLocation.setText(currentUser.getLastLocation());

            }
        }).execute(SharedPreference.ReadFromSharedPreference(this, "USER", ""));


    }

    private void setEvent() {
        button_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFormat();
                datePickerFragment.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        button_updateInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_fName.getText().toString().trim().length()==0 || editText_lName.getText().toString().length()==0)
                    Message.printMessage(ProfileActivity.this,"Please fill all the blank and Click again!");
                else
                {
                    //Name,Fname,Lname,Long,Lat,Birthday
                    if(textView_profile_birthdate.getText().length()!=0)
                        new UpdateInformationAsyncTask(ProfileActivity.this).execute(currentUser.getName(),editText_fName.getText().toString().trim(),
                                editText_lName.getText().toString().trim(),"",textView_profile_birthdate.getText().toString());
                    else
                        Message.printMessage(ProfileActivity.this,"Please set your birthdate by CLick on Calendar!");

                }

            }
        });
    }

    private void setWidget() {

        //Widget
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.ImageView_overlapImage);
        textView_profile_email = (TextView) findViewById(R.id.textView_profile_gmail);
        textView_profile_name = (TextView) findViewById(R.id.textView_profile_name);
        textView_profile_birthdate = (TextView) findViewById(R.id.textView_profile_birthdate);

        editText_fName = (EditText) findViewById(R.id.editText_profile_FirstName);
        editText_lName = (EditText) findViewById(R.id.editText_profile_LastName);
        button_datePicker = (Button) findViewById(R.id.button_profile_datePicker);
        button_updateInformation= (Button) findViewById(R.id.button_profile_UpdateInformation);
        textView_lasLocation = (TextView) findViewById(R.id.textView_profile_lastLocation);

        //Calendar

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFormat extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        TextView birthdate;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal =Calendar.getInstance();
            int year  = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            birthdate = (TextView) getActivity().findViewById(R.id.textView_profile_birthdate);
            return new DatePickerDialog(getContext(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            birthdate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }
    }

    public class UpdateInformationAsyncTask extends AsyncTask<String,Void,Boolean>
    {
        final String URL = "http://whereareyou.somee.com/WebService.asmx?op=USER_UpdateUserInformation";
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
            if(params[3].toString().length()!=0)
            {
                lastLocation = params[3];
            }
            String birthdate = params[4];

            try {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_UPDATEINFORMATION);
                request.addProperty("name",nameInsert);
                request.addProperty("fName",fname);
                request.addProperty("lName",lname);
                request.addProperty("birthDate",birthdate);
                request.addProperty("lastLocation",lastLocation);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransportSE = new HttpTransportSE(URL);


                httpTransportSE.call(NAMESPACE +METHOD_UPDATEINFORMATION,envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                if(Boolean.parseBoolean(soapPrimitive.toString()))
                    return true;

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
                Message.printMessage(context,"Update Information Successfully");
            else
                Message.printMessage(context,"Update Failed");

        }

    }

}
