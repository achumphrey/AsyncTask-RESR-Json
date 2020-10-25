package com.example.asynctaskrestjson;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.asynctaskrestjson.data.AsyncTaskData;
import com.example.asynctaskrestjson.data.DataObject;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Use AsyncTask in order to make an HTTP GET request (JSON response)
 */


public class MainActivity extends AppCompatActivity {

    AsyncTaskData asyncTaskData = new AsyncTaskData();
    String url = "https://api.tvmaze.com/singlesearch/shows?q=girls";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            data = asyncTaskData.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("JSON DATA", data);

        DataObject dataObject = new Gson().fromJson(data, DataObject.class);

        Log.i("JSON OBJECT", dataObject.toString());
    }
}