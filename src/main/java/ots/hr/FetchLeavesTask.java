package ots.hr;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class FetchLeavesTask extends AsyncTask<String,Void,ArrayList<Leave>> {

    private final String LOG_TAG = FetchLeavesTask.class.getSimpleName();
    private LeavesAdapter leavesAdapter;
    public static final String SERVICE_BASE_URL = "http://demo.ots.gr/openonehr/api/1/leave/2019/6";

    public FetchLeavesTask(LeavesAdapter leavesAdapter){
        this.leavesAdapter = leavesAdapter;

    }

    private ArrayList<Leave> getMerchantsFromJson(String responseJsonStr) throws JSONException {
        ArrayList<Leave> leaves = new ArrayList<>();
        try{
            JSONArray leavesArray = new JSONArray(responseJsonStr);
            //JSONArray leavesArray = response.getJSONArray("");
            for(int i=0; i<leavesArray.length(); i++){
                JSONObject leavesObject = leavesArray.getJSONObject(i);
                String id = leavesObject.getString("id");
                String description = leavesObject.getJSONObject("leaveCategory").getString("description");
                String fromDate = leavesObject.getString("fromDate");
                String toDate = leavesObject.getString("toDate");
                String duration = leavesObject.getString("duration");
                String status = leavesObject.getString("status");
                leaves.add(new Leave(id, description, fromDate, toDate, duration, status));
            }
            Log.d(LOG_TAG, "Leaves Fetching Complete. " + leaves.size() + "leaves inserted");
            return leaves;
        }catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return leaves;
        }
    }

    @Override
    protected ArrayList<Leave> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String leavesJsonString = null;

        try {

            Uri builtUri = Uri.parse(SERVICE_BASE_URL);

            URL url = new URL(builtUri.toString());

            // Create the request to Yummy Wallet server, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            leavesJsonString = buffer.toString();
            return  getMerchantsFromJson(leavesJsonString);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the leaves data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Leave> leaves) {
        if(leaves.size() > 0){
            this.leavesAdapter.clear();
            for(Leave l : leaves){
                this.leavesAdapter.add(l);
            }
        }
    }
}
