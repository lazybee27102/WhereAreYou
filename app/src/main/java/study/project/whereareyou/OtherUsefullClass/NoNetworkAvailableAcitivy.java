package study.project.whereareyou.OtherUsefullClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import study.project.whereareyou.R;

public class NoNetworkAvailableAcitivy extends AppCompatActivity {
    Button button_reload,button_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network_available);

        button_exit = (Button)findViewById(R.id.button_exit);
        button_reload = (Button) findViewById(R.id.button_reload_net_work);

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        button_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }


}
