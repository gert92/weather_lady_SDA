package com.example.weather_lady;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

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

    protected <T,V> void onSearchButtonClick() {
        try{
            Gson gson = new Gson();
            //--------- GET LOCATION-----------//

            StringBuilder locationUrlBuilder = new StringBuilder("http://dataservice.accuweather.com/locations/v1/search?apikey=mr3s3PvmHnJIsIWrP9VADG9GBwCQt55q&q=");

            JsonObject result = gson.fromJson(run(locationUrlBuilder.append(search.getText()).toString()), JsonArray.class).get(0).getAsJsonObject();
//            JsonObject result = gson.fromJson(run(locationUrlBuilder.append(search.getText()).toString()), JsonObject.class);

            System.out.println(result);
            String country = result.get("Country").getAsJsonObject().get("LocalizedName").toString();
            String city = result.get("LocalizedName").getAsString();
            String region = result.get("Region").getAsJsonObject().get("LocalizedName").toString();
            JsonObject geoPos = result.get("GeoPosition").getAsJsonObject();
            List<Double> longLat = List.of(geoPos.get("Longitude").getAsDouble(), geoPos.get("Latitude").getAsDouble());

            Location location = new Location(1, longLat,region, city, country);


            //---------GET WEATHER------------------//

            StringBuilder weatherUrlBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?units=metric&");
            Double lat = location.getLonLat().get(0);
            Double lon = location.getLonLat().get(1);
            String appid = "c4e409e7c55c17c4520a437b79a8f79b";

            weatherUrlBuilder.append("lat=").append(lat).append("&lon=").append(lon).append("&appid=").append(appid);


            JsonObject weather = gson.fromJson(run(weatherUrlBuilder.toString()), JsonObject.class).getAsJsonObject();
            JsonObject main = weather.get("main").getAsJsonObject();
            JsonObject wind = weather.get("wind").getAsJsonObject();

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