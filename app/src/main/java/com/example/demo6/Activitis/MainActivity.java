package com.example.demo6.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo6.Adapters.HourlyAdapters;
import com.example.demo6.Domains.Hourly;
import com.example.demo6.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    TextView cityName;
    Button search;
    TextView show;
    TextView descriptionTxt,textView4,textView5,textView7, humidityTxt, dateAndTime;
    ImageView imageView;
    String url;
    LinearLayout layoutWeather, principal_layout;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    class getWeather extends AsyncTask<String, Void, String> implements com.example.demo6.Activitis.getWeather {

        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line="";
                while((line = reader.readLine()) != null){
                    result.append(line).append("\n");
                }
                return  result.toString();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            locationPermissionGranted = false;
            if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }




        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("main");
                String coordInfo = jsonObject.getString("coord");
                String windInfo = jsonObject.getString("wind");
                JSONObject jsonCoord = new JSONObject(coordInfo);
                JSONObject jsonWind = new JSONObject(windInfo);
                JSONObject jsonObject1 = new JSONObject(weatherInfo);
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = ((JSONArray) weatherArray).getJSONObject(0);
                String lon = jsonCoord.getString("lon");
                String windSpeed= jsonWind.getString("speed");
                float speed = Float.parseFloat(windSpeed);
                float metricSpeed = (speed*3600)/1000;
                String realSpeed = String.valueOf(metricSpeed);
                String lat = jsonCoord.getString("lat");
                //String icon = weatherObject.getString("icon");
                String temp = jsonObject1.getString("temp");
                String pressure = jsonObject1.getString("pressure");
                String humidity = jsonObject1.getString("humidity");
                String descriptionTexte = weatherObject.getString("description");
                String nameCity = jsonObject.getString("name");
                //Glide.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(imageView);
                show.setText(temp + "°C");
                textView4.setText("Lon:" + lon + "    Lat:" + lat);
                textView5.setText(pressure + " hPa");
                textView7.setText(realSpeed + " km/h");
                humidityTxt.setText(humidity);
                descriptionTxt.setText(descriptionTexte);
                cityName.setText(nameCity);
                layoutWeather.setVisibility(View.VISIBLE);
                principal_layout.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getLocationPermission();
        getDeviceLocation();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        
        //cityName = findViewById(R.id.cityName);
        //search = findViewById(R.id.search);
        principal_layout = findViewById(R.id.principal_layout);
        show = findViewById(R.id.weather);
        cityName = findViewById(R.id.cityName);
        descriptionTxt = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        layoutWeather = findViewById(R.id.layoutWeather);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        humidityTxt = findViewById(R.id.humidityTxt);
        dateAndTime = findViewById(R.id.dateAndTime);


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        String day = dayFormat.format(calendar.getTime());
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        String formattedDateTime = String.format("%s %s | %s", day, date, time);

        dateAndTime.setText(formattedDateTime);


        String city = cityName.getText().toString();
        //cityName.setText(fusedLocationProviderClient.toString());
/*
        String[] temp = new String[0];
        try {
            if (cityName!=null){
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=[votre clé]&units=metric";
            }else {
                Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
            }
            getWeather task= new getWeather();
            temp[0] = task.execute(url).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if (temp[0] == null){
            show.setText("Cannot able to find Weather");
        }
*/
        final String[] temp={""};

        try {
            if (cityName != null) {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=[votre clé API ici]&units=metric";
            } else {
                Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
            }
            getWeather task = new getWeather();
            temp[0] = task.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (temp[0] == null) {
            show.setText("Cannot able to find Weather");
        }


        initRecyclerview();
        setVariable();
        setVariable2();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            boolean locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    lastKnownLocation = task.getResult();
                                    if (lastKnownLocation != null) {
                                        // Use the location coordinates
                                        double latitude = lastKnownLocation.getLatitude();
                                        double longitude = lastKnownLocation.getLongitude();
                                        Log.d("Location", "Lat: " + latitude + ", Long: " + longitude);
                                    }
                                } else {
                                    Log.d("Location", "Current location is null. Using defaults.");
                                    Log.e("Location", "Exception: %s", task.getException());
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void setVariable() {
        TextView next7Days = findViewById(R.id.nextBtn);
        next7Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FutureActivity2.class));
            }
        });
    }


    private void setVariable2() {
        ImageView search = findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }


    private void initRecyclerview() {
        ArrayList<Hourly> items = new ArrayList<>();

        items.add(new Hourly("3 am", 9, "cloudy"));
        items.add(new Hourly("6 am", 12, "cloudy"));
        items.add(new Hourly("9 am", 14, "sunny"));
        items.add(new Hourly("12 am", 18, "sunny"));
        items.add(new Hourly("3 pm", 19, "sunny"));
        items.add(new Hourly("6 pm", 16, "cloudy"));
        items.add(new Hourly("9 pm", 12, "cloudy"));

        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly = new HourlyAdapters(items);
        recyclerView.setAdapter(adapterHourly);

    }
}
