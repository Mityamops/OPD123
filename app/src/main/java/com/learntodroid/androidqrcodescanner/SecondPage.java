package com.learntodroid.androidqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SecondPage extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;

    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("qr").toString();
        TextView Code = findViewById(R.id.Code);
        Code.setText(name);

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String strDate = dateFormat.format(currentTime);

        TextView Data = findViewById(R.id.Data);
        Data.setText(strDate);

        TextView GeoPos = findViewById(R.id.GeoPos);
        GeoPos.setText("No coord");

        TextView Whether = findViewById(R.id.whether);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                String geopos = "x:" + location.getLatitude() + ", y:" + location.getLongitude();
                                GeoPos.setText(geopos);


                                url = "https://api.weatherapi.com/v1/current.json?q="+location.getLatitude()+","+location.getLongitude()+"&key=52fb3618a2644f1b9d6115703232505";

                                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                                        Request.Method.GET, url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response)
                                            {
                                                try {
                                                    JSONObject data = response.getJSONObject("current");
                                                    String temp_c = String.valueOf(data.getDouble("temp_c"));
                                                    JSONObject condition = data.getJSONObject("condition");
                                                    String pogoda = condition.getString("text");
                                                    String wind_speed = String.valueOf(data.getDouble("wind_kph"));
                                                    String wind_dir = data.getString("wind_dir");
                                                    String pressure_mb = String.valueOf(data.getDouble("pressure_mb"));  // milli bar


                                                    String result = "Температура: "+temp_c+", \nСостояние: "+pogoda+", \nСкорость ветра: "+
                                                            wind_speed+", \nНаправление ветра: "+wind_dir+", \nДавление(миллибары): "+pressure_mb;

                                                    Whether.setText(result);
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error)
                                            {
                                            }
                                        });

                                requestQueue.add(jsonArrayRequest);
                            }
                        }
                    });
        }
    }
}