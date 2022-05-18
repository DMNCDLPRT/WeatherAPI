package com.example.weatherapi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult, tvCity, cite;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        tvCity = findViewById(R.id.tvCity);
        cite = findViewById(R.id.cite);
    }

    public void getWeatherDetails(View view) {
        String tempUrl;
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("City field can't be empty!");
        }else{
            String url = "https://api.openweathermap.org/data/2.5/weather";
            String apiKey = "ae86e3f7a0aef6f7709c32bd0150dd02";
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&apikey=" + apiKey;
            }else{
                tempUrl = url + "?q=" + city + "&apikey=" + apiKey;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
                String output = "";
                String cityOutput = "";
                String citeAPI = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    tvResult.setTextColor(Color.rgb(68,134,199));
                    tvCity.setTextColor(Color.rgb(0,0,0));
                    cite.setTextColor(Color.rgb(220,20,60));
                    cityOutput += cityName + " (" + countryName + ")"
                            +  "\n" + df.format(temp) + "°C";
                    output += "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";
                    citeAPI += "\n\n" + "openweathermap.org" ;
                    tvCity.setText(cityOutput);
                    tvResult.setText(output);
                    cite.setText(citeAPI);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}