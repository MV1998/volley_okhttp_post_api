package com.example.postmanpostdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // okhttp3
        new MyAsyncTask().execute("url");

        //with volley
        loadApi();

    }

    //this project was made for testing post api response with content-type = application/vi-json
    //Result - True
    void loadApi() {
        try {
            Log.d(TAG, "loadApi: ");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "type your url";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", "u");
            jsonBody.put("password", "p");
            final String requestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/vi-json";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, Integer> {


        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        @Override
        protected Integer doInBackground(String... strings) {

            String url = strings[0];

            // create your json here
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", "u");
                jsonObject.put("password", "p");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json");
            // put your json here
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = null;
            try {
                okhttp3.Response response1 = okHttpClient.newCall(request).execute();
                String resStr = response1.body().string();
                Log.d("Response---->", resStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
