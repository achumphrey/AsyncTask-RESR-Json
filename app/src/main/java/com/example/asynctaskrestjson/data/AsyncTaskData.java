package com.example.asynctaskrestjson.data;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskData extends AsyncTask <String, Void , String>  {

    @Override
    protected String doInBackground(String... url) {

        String result;

        try {

            URL urls = new URL(url[0]);
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setReadTimeout(150000); //milliseconds
            conn.setConnectTimeout(15000); // milliseconds
            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream, "iso-8859-1");

                BufferedReader reader = new BufferedReader(inputStreamReader, 8);

                StringBuilder sb = new StringBuilder();
                String line ;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                result = sb.toString();

            } else {
                return "error";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
