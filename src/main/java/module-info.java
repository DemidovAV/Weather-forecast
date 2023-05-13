module com.weather {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.weather to javafx.fxml;
    exports com.weather;
}