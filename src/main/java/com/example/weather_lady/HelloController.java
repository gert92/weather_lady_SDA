package com.example.weather_lady;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {
    OkHttpClient httpClient = new OkHttpClient();
    @FXML
    private Label weLocation;
    @FXML
    private Label temperature;
    @FXML
    private Label pressure;
    @FXML
    private Label humidity;
    @FXML
    private Label weWind;

    private Label searchBar;
    @FXML
    private TextField search;

    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
    protected <T,V> void onSearchButtonClick() {
        try{
            Gson gson = new Gson();
            //--------- GET LOCATION-----------//

            StringBuilder locationUrlBuilder = new StringBuilder("http://dataservice.accuweather.com/locations/v1/search?apikey=mr3s3PvmHnJIsIWrP9VADG9GBwCQt55q&q=");

           List results = gson.fromJson(run(locationUrlBuilder.append(search.getText()).toString()), List.class);
           Map coords = (Map) results.get(0);
           Map country = (Map) coords.get("Country");
           Map region = (Map) coords.get("Region");
           Map<String, Double> longlat = (Map) coords.get("GeoPosition");
            Location location = new Location(1, List.of(longlat.get("Latitude"), longlat.get("Longitude")), (String) region.get("LocalizedName"), (String) coords.get("LocalizedName"), (String) country.get("LocalizedName"));

            //---------GET WEATHER------------------//

            StringBuilder weatherUrlBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?units=metric&");
            Double lat = location.getLonLat().get(0);
            Double lon = location.getLonLat().get(1);
            String appid = "c4e409e7c55c17c4520a437b79a8f79b";

            weatherUrlBuilder.append("lat=").append(lat).append("&lon=").append(lon).append("&appid=").append(appid);


            Map weather = gson.fromJson(run(weatherUrlBuilder.toString()), Map.class);
            Map main = (Map) weather.get("main");
            Map wind = (Map) weather.get("wind");

            weLocation.setText("Location: " + location.getCity());
            temperature.setText("Temperature: " + main.get("temp").toString());
            pressure.setText("Pressure: " + main.get("pressure").toString());
            humidity.setText("Humidity: " + main.get("humidity").toString());
            weWind.setText("Wind direction: " + wind.get("deg").toString() + " / " + "Speed: " + wind.get("speed"));

        } catch (IOException e){
            e.printStackTrace();
        }

    }
    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        }

    }
}