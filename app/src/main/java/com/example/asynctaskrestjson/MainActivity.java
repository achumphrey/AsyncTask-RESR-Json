package com.example.asynctaskrestjson;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.asynctaskrestjson.data.AsyncTaskData;
import com.example.asynctaskrestjson.data.DataObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Use AsyncTask in order to make an HTTP GET request (JSON response)
 */


public class MainActivity extends AppCompatActivity {

    AsyncTaskData asyncTaskData;
    String user = "";
    String endPoint = "https://api.tvmaze.com/singlesearch/shows?q=";
    String jsonData;
    String url;
    DataObject dataObject;
    SharedPreferences sharedPref;
    String MyPreference = "myPref";
    EditText edName;
    TextView tvName, tvError, tvNumber, tvDays, tvNetworkError;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
        Button btSearch = findViewById(R.id.btSearch);
        edName = findViewById(R.id.edName);
        edName.getFocusable();
        tvDays = findViewById(R.id.tvDays);
        tvName = findViewById(R.id.tvName);
        tvNumber = findViewById(R.id.tvNumber);
        tvError = findViewById(R.id.tvError);
        tvNetworkError = findViewById(R.id.tvNetworkError);
        imgView = findViewById(R.id.imgView);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                user = edName.getText().toString();

                Log.i("User", user);

                if (!(user.isEmpty())){

                    if (sharedPref.contains(user)) {
                        useCachedData(user);
                    }else {
                        try {
                            url = endPoint + user;
                            asyncTaskData = new AsyncTaskData();
                            jsonData = asyncTaskData.execute(url).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        Log.i("JSON DATA", jsonData);

                        dataObject = new Gson().fromJson(jsonData, DataObject.class);

                        Log.i("JSON OBJECT", dataObject.toString());

                        write(user, dataObject);
                        displayData(dataObject);
                    }
                }else {
                    removeViews();
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Hide the keyboard
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    //Display data
    private void displayData(DataObject data){

        removeErrorMsgs();

        tvDays.setText(getString(R.string.num_of_days));

        tvName.setText(data.name);

        String date = calculateNumOfDays(data.premiered);
        tvNumber.setText(date);

        Picasso.get()
            .load(data.image.original)
            .resize(400,500)
            .error(R.drawable.ic_launcher_background)
            .into(imgView);

        drawViews();
    }

    //Remove error messages
    private void removeErrorMsgs(){
        tvError.setVisibility(View.GONE);
        tvNetworkError.setVisibility(View.GONE);
    }

    private void removeViews(){
        tvName.setVisibility(View.GONE);
        tvNumber.setVisibility(View.GONE);
        tvDays.setVisibility(View.GONE);
        imgView.setVisibility(View.GONE);
    }

    //Draw views
    private void drawViews(){
        tvName.setVisibility(View.VISIBLE);
        tvNumber.setVisibility(View.VISIBLE);
        tvDays.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.VISIBLE);
    }

    //Calculate number of days
    private String calculateNumOfDays(String date){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.UK);

        ZoneId z = ZoneId.of("Europe/London");
        LocalDate today = LocalDate.now(z);
        Date currentDateValue = null;
        try {
            currentDateValue = sdf.parse(String.valueOf(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date startDateValue = null;

        if (date.isEmpty() && date.equals("")){
            startDateValue = currentDateValue;
        }else {
            try {
                startDateValue = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        assert currentDateValue != null;
        assert startDateValue != null;
        long diff = currentDateValue.getTime() - (startDateValue.getTime());
        long numDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return Long.toString(numDays);
    }

    private void write(String user, DataObject data){
        SharedPreferences.Editor editor  = sharedPref.edit();
        Gson gson = new Gson();
        String sData = gson.toJson(data);
        editor.putString(user, sData);
        editor.apply();
    }

    private DataObject read(String user){
        String json  = null;
        if (sharedPref.contains(user))
            json = sharedPref.getString(user, "");

        return new Gson().fromJson(json, DataObject.class);
    }

    //Use cached version of data
    private void useCachedData(String user){
        DataObject data = read(user);

        displayData(data);
    }




}