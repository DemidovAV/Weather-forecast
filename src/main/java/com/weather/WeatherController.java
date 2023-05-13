package com.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.*;


public class WeatherController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text averageMornTemp;

    @FXML
    private TextField cityName;

    @FXML
    private Text currentTemp;

    @FXML
    private Button getData;

    @FXML
    private Text maxMornTemp;

    @FXML
    void initialize() {
        getData.setOnAction(event -> {
            String city = cityName.getText().trim();
            if (!city.equals("")) {
                String outputCurrent = getURLContent("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=c99e1555a619a018a9c4989f19d36f97&units=metric");
                String output5Days = getURLContent("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=c99e1555a619a018a9c4989f19d36f97&units=metric");
                System.out.println(outputCurrent);

                if (!outputCurrent.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(outputCurrent);
                    JSONObject jsonObject1 = new JSONObject(output5Days);
                    currentTemp.setText("Current temperature:   " + jsonObject.getJSONObject("main").getDouble("temp") + " C");
                    List <String> data = new ArrayList<>();
                    data = jsonObject1.getJSONArray("list").toList().stream().map(obj -> Objects.toString(obj, null)).toList();
                    JSONArray jsonArray = jsonObject1.getJSONArray("list");
                    Iterator arrayIter = jsonArray.iterator();
                    double sumTemp = 0.00;
                    double maxTemp = -100.00;
                    while (arrayIter.hasNext()) {
                        JSONObject arrData = (JSONObject) arrayIter.next();
                        if (Arrays.stream(arrData.get("dt_txt").toString().split(" ")).toArray()[1].equals("06:00:00")) {
                            Double mornTemp = arrData.getJSONObject("main").getDouble("temp");
                            System.out.println(mornTemp);
                            sumTemp += mornTemp;
                            if (mornTemp > maxTemp) {
                                maxTemp = mornTemp;
                            }
                        }
                    }
                    averageMornTemp.setText("Average:   " + String.format("%.2f", sumTemp/5) + " C");
                    maxMornTemp.setText("Max:   " + maxTemp + " C");
                    System.out.println("Current temperature:   " + jsonObject.getJSONObject("main").getDouble("temp") + " C");
                }
            }
        });
    }

    private static String getURLContent(String urlAdress) {
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(urlAdress);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(urlConnection.getInputStream())));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Город не найден");
            throw new RuntimeException(e);
        }
        return content.toString();
    }

}