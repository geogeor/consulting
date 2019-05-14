package ots.hr;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;


public class Activity_get extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_get, new LeaveFragment()).commit();

    }



}
