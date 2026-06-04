package com.example.demo6.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo6.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;




public class SearchActivity extends AppCompatActivity {

    TextView cityName;
    Button search;
    TextView show;
    TextView city;
    ImageView imageView;
    String url;
    LinearLayout layoutWeather;




    class getWeather extends AsyncTask<String, Void, String>{
        @Override
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
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("main");
                JSONObject jsonObject1 = new JSONObject(weatherInfo);
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String icon = weatherObject.getString("icon");
                String temp = jsonObject1.getString("temp");
                String nameCity = jsonObject.getString("name");
                Glide.with(SearchActivity.this).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(imageView);
                show.setText(temp + "°C");
                city.setText(nameCity);
                layoutWeather.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Picasso.get().setLoggingEnabled(true);
        setContentView(R.layout.activity_search);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);
        city = findViewById(R.id.weather1);
        imageView = findViewById(R.id.imageView);
        layoutWeather = findViewById(R.id.layoutWeather);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout backBtn = findViewById(R.id.backBtn);

        final String[] temp={""};

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(SearchActivity.this,"Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                try {
                    if (cityName!=null){
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=e42367e951a1ff8fa55334d763e069ac&units=metric";
                    }else {
                        Toast.makeText(SearchActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                    }
                    SearchActivity.getWeather task= new SearchActivity.getWeather();
                    temp[0] = task.execute(url).get();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                if (temp[0] == null){
                    show.setText("Cannot able to find Weather");
                }
            }
        });


        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
        });

    }

}

