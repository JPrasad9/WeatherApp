package com.example.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat,txt,txt2;
    String lat;
    String provider;
    protected double latitude;
    protected double longitude;
    protected boolean gps_enabled, network_enabled;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = (TextView) findViewById(R.id.textview1);
        requestQueue = Volley.newRequestQueue(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();
        txtLat = (TextView) findViewById(R.id.textview1);
        txt=(TextView) findViewById(R.id.hint);
        txt2=findViewById(R.id.weath);
//        getlocation();
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        Log.d("loc",""+latitude+longitude);
//        getlocation();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    public void getlocation(View view){
//        txtLat.setText("please wait your location is loading..." +
//                "check whether GPS is on");
//        txt.setText("");
        String url="https://geocode.xyz/"+latitude+","+longitude+"?json=1";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String in;
//                    JSONObject obj=response.JSONObject();
                    in=response.getString("staddress")+"\n";
                    in+=response.getJSONObject("osmtags").getString("name")+"\n";
                    in+=response.getString("city")+"\n";
                    in+=response.getString("region")+"\n";
                    in+=response.getString("country")+"\n";
                    Log.d("city", response.getString("city"));
                    txtLat.setText(in);
                } catch (Exception e) {
                    Log.d("myapp", "something went wrong");
                    txtLat.setText("Please give the location permission to the app");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
                txtLat.setText("Please give the location permission to the app");

            }
        });
        requestQueue.add(jsonObjectRequest);
        String url2="http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=f1c35e9694dfd24140deb2f8ac75e698";
        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET,url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("temp", "hello");
                try {
                    String in;
//                    JSONObject obj=response.JSONObject();
                    in="description:"+response.getJSONArray("weather").getJSONObject(0).getString("main")+"\n";
//                    String temp=;
//                    Log.d("temp", ""+temp);
                    in+="temperature:"+response.getJSONObject("main").getString("temp")+"F\n";
                    in+="wind speed:"+response.getJSONObject("wind").getString("speed")+"m/s\n";
                    in+="humidity:"+response.getJSONObject("main").getString("humidity")+"%\n";
                    in+="pressure:"+response.getJSONObject("main").getString("pressure")+"hpa\n";
                    Log.d("temp", ""+in);
                    txt2.setText(in);
                } catch (Exception e) {
                    Log.d("error", "onResponse: ");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");

            }
        });
        requestQueue.add(jsonObjectRequest2);
    }
}