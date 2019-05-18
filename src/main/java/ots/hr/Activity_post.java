package ots.hr;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class Activity_post extends AppCompatActivity {

    EditText leaveID;
    Spinner spinner1;
    EditText duration;
    TextView tvIsConnected;
    TextView tvResult;
    CalendarView CalendarView1;
    CalendarView CalendarView2;
    CheckBox cb1, cb2;
    Calendar fromDate1, toDate1;
    String fromDate2, toDate2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        leaveID = findViewById(R.id.leaveID);
        duration = findViewById(R.id.duration);
        tvIsConnected = findViewById(R.id.tvIsConnected);
        tvResult = findViewById(R.id.tvResult);
        addListenerOnSpinnerItemSelection();
        CalendarView1 = findViewById(R.id.CalendarView1); // get the reference of CalendarView
        CalendarView1.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of focused month
        CalendarView1.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        CalendarView1.setSelectedWeekBackgroundColor(Color.RED); // red color for the selected week's background
        CalendarView1.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line

        // perform setOnDateChangeListener event on CalendarView
        CalendarView1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_LONG).show();
                fromDate1=Calendar.getInstance();
                fromDate1.set(year,month,dayOfMonth);
                Date dt1 = fromDate1.getTime();
                SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
                fromDate2 = d1.format(dt1);
            }
        });


        CalendarView2 = findViewById(R.id.CalendarView2); // get the reference of CalendarView
        CalendarView2.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of focused month
        CalendarView2.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        CalendarView2.setSelectedWeekBackgroundColor(Color.RED); // red color for the selected week's background
        CalendarView2.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line
        CalendarView2.setVisibility(View.GONE);
        // perform setOnDateChangeListener event on CalendarView
        CalendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_LONG).show();
                toDate1=Calendar.getInstance();
                toDate1.set(year,month,dayOfMonth);
                Date dt2 = toDate1.getTime();
                SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
                toDate2 = d2.format(dt2);
            }
        });

        CalendarView1.setVisibility(View.GONE);
        CalendarView2.setVisibility(View.GONE);
        addListenerOnCb1();
        addListenerOnCb2();


        checkNetworkConnection();
    }



    public void addListenerOnCb1() {

        cb1 = findViewById(R.id.checkbox1);

        cb1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CalendarView1.setVisibility(View.VISIBLE);
                }
                else {
                    CalendarView1.setVisibility(View.GONE);
                }
            }
        });




    }

    public void addListenerOnCb2() {

        cb2 = findViewById(R.id.checkbox2);

        cb2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CalendarView2.setVisibility(View.VISIBLE);
                }
                else {
                    CalendarView2.setVisibility(View.GONE);
                }
            }
        });

    }








    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"

            tvIsConnected.setText("Connected "+networkInfo.getTypeName());
            // change background color to red
            tvIsConnected.setBackgroundColor(0xFF7CCC26);


        } else {
            // show "Not Connected"
            TextView tvIsConnected = findViewById(R.id.tvIsConnected);
            tvIsConnected.setText("Not Connected");
            // change background color to green
            tvIsConnected.setBackgroundColor(0xFFFF0000);
        }

        return isConnected;
    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            tvResult.setText(result);
        }
    }


    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buildJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }


    public void send(View view) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        // perform HTTP POST request
        if(checkNetworkConnection())
            new HTTPAsyncTask().execute("http://demo.ots.gr/openonehr/api/1/leave/save/6");
        else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

    }



    private JSONObject buildJsonObject() throws JSONException {

        String lc = String.valueOf(spinner1.getSelectedItem());
        String leaveCategoryID = "";
        if (lc.equals("Κανονική")) {
            leaveCategoryID = "1";
        } else if (lc.equals("Ασθενείας") )  {
            leaveCategoryID = "2";
        }
         else if (lc.equals("Ωριαία") )  {
            leaveCategoryID = "3";
        }
        else if (lc.equals("Τιμητική") )  {
            leaveCategoryID = "4";
        }
        else if (lc.equals("Γάμου") )  {
            leaveCategoryID = "5";
        }
        else if (lc.equals("Αιμοδοτική"))  {
            leaveCategoryID = "6";
        }
        else if (lc.equals("Συνδικαλιστική"))  {
            leaveCategoryID = "7";
        }
        else if (lc.equals("Γονική (σε ώρες)"))  {
            leaveCategoryID = "8";
        }
        else if (lc.equals("Άνευ αποδοχών"))  {
            leaveCategoryID = "9";
        }
        else if (lc.equals("Εκλογική"))  {
            leaveCategoryID = "10";
        }
        else if (lc.equals("Άδεια λόγω θανάτου"))  {
            leaveCategoryID = "11";
        }
        else if (lc.equals("Γέννησης παιδιού"))  {
            leaveCategoryID = "12";
        }
        else if (lc.equals("Μειωμένο ωράριο"))  {
            leaveCategoryID = "13";
        }
        else if (lc.equals("Ειδική γονική"))  {
            leaveCategoryID = "14";
        }

        String fromDate3=fromDate2+"T00:00:00";
        String toDate3=toDate2+"T00:00:00";




        JSONObject json1= new JSONObject();
        json1.put("id",  leaveCategoryID);
        json1.put("description", "Κανονική");
        json1.put("unit", "DAY");
        json1.put("kind", "REGULAR");
        json1.put("organization", 1);
        //JSONArray jsonArray = new JSONArray();
        //jsonArray.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("id", leaveID.getText().toString());
        json2.put("leaveCategory",json1);
        json2.put("organization",  1);
        json2.put("created",  "2019-03-01T00:00:00");
        json2.put("fromDate",  fromDate3);
        json2.put("toDate",  toDate3);
        json2.put("duration",  duration.getText().toString());
        json2.put("status",  "SUBMITTED");


        return json2;
    }


    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }








}
