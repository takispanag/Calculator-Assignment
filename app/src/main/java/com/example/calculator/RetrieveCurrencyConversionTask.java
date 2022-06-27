package com.example.calculator;

import android.os.AsyncTask;
import android.widget.Toast;

import com.skydoves.powerspinner.IconSpinnerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class RetrieveCurrencyConversionTask extends AsyncTask<String, Void, String> {

    //params = [oldSpinnerText, newSpinnerText, amount]
    protected String doInBackground(String[] params) {
        OkHttpClient client = new OkHttpClient();

        String amount  = params[2];
        Double responseResult = null;
        String to = params[1];
        String from = params[0];

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/fixer/convert?to=" + to + "&from=" + from + "&amount=" + amount)
                .addHeader("apikey", "Y1uEv5CUiV9EzUSo3J9oCADDd1J7iZ4V")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            responseResult = jsonObject.getDouble("result");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(responseResult);
    }
}
