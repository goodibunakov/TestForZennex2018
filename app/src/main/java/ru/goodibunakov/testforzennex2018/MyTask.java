package ru.goodibunakov.testforzennex2018;

import android.os.AsyncTask;

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

public class MyTask extends AsyncTask<String, Void, ArrayList> {

    public OnTaskComplete OnTaskComplete;

    public MyTask(OnTaskComplete callback){
        this.OnTaskComplete = callback;
    }

    public interface OnTaskComplete {
        void onTaskComplete(ArrayList<String> arrayList);
    }


    @Override
    protected void onPostExecute(ArrayList result) {
        OnTaskComplete.onTaskComplete(result);
    }

    @Override
    protected ArrayList doInBackground(String... params) {

        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                httpURLConnection.disconnect();
                if(inputStream != null) inputStream.close();
                if(reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return parseJSON(result.toString());
    }

    private ArrayList<String> parseJSON(String strJSON){

        ArrayList<String> arrayList=new ArrayList<>();
        try {
            if (strJSON != null) {

                JSONObject jsonObject = new JSONObject(strJSON);
                JSONArray jsonArray = jsonObject.getJSONArray("quotes");
                arrayList.add("Total: "+jsonObject.getString("total"));
                arrayList.add("Last: "+jsonObject.getString("last"));

                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add("ID: " + jsonArray.getJSONObject(i).getString("id")
                            + "\nDescription: " + jsonArray.getJSONObject(i).getString("description")
                            + "\nTime: " + jsonArray.getJSONObject(i).getString("time")
                            + "\nRating: " + jsonArray.getJSONObject(i).getString("rating"));
                }

            } else
                return null;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return arrayList;
    }
}