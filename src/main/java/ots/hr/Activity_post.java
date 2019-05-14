package ots.hr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_post extends AppCompatActivity {

    EditText leaveID;
    EditText leaveCategoryID;
    EditText fromDate;
    EditText toDate;
    EditText duration;
    TextView tvIsConnected;
    TextView tvResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        leaveID = findViewById(R.id.leaveID);
        leaveCategoryID = findViewById(R.id.leaveCategoryID);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        duration = findViewById(R.id.duration);
        tvIsConnected = findViewById(R.id.tvIsConnected);
        tvResult = findViewById(R.id.tvResult);
        checkNetworkConnection();
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

        JSONObject json1= new JSONObject();
        json1.put("id",  leaveCategoryID.getText().toString());
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
        json2.put("fromDate",  fromDate.getText().toString());
        json2.put("toDate",  toDate.getText().toString());
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
