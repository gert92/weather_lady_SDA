module com.example.weather_lady {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.weather_lady to javafx.fxml;
    exports com.example.weather_lady;
}