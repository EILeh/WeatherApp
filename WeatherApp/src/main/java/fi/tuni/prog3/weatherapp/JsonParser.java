package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser
{
    /***
     * Parsii API-kutsulla saadun stringiin tallennetun JSON-muotoisen datan ja hankkii siitä kaikki tarvittavat säätie-
     * dot muuttujiin niiden käyttöä ohjelmassa varten.
     * @param data, JSON-muotoinen säädata
     * @param lat, kaupungin latitudi
     * @param lon, kaupungin longitudi
     * @return palauttaa listan, joka on täynnä yhden tunnin säätilaolioita
     */
    public List<Weather> readFromJsonsHourly(String data, double lat, double lon)
    {
        // ChatGPT:tä käytetty auttamaan tiedonhaun toiminnallisuuden ymmärtämisessä
        List<Weather> weatherList = new ArrayList<>();
        int dt = 0;
        int sunrise = 0;
        int sunset = 0;
        String wMain = "";
        String description = "";
        double temperature = 0.0;
        double feelsLike = 0.0;
        double temperatureMin = 0.0;
        double temperatureMax = 0.0;
        int pressure = 0;
        double humidity = 0.0;
        double windSpeed = 0.0;
        int cloudCover = 0;
        int windDirection = 0;
        String cityName = "";
        String countryName = "";
        String dtTxt = "";

        try
        {
            JsonObject json = com.google.gson.JsonParser.parseString(data).getAsJsonObject();
            JsonArray listInsideJsonStr = json.getAsJsonArray("list");

            JsonObject city = json.getAsJsonObject("city");
            JsonObject coord = city.getAsJsonObject("coord");

            for (int j = 0; j < 36; j++)
            {
                dt = listInsideJsonStr.get(j).getAsJsonObject().get("dt").getAsInt();

                wMain = listInsideJsonStr.get(j).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
                dtTxt = listInsideJsonStr.get(j).getAsJsonObject().get("dt_txt").getAsString();

                temperature = listInsideJsonStr.get(j).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble();
                feelsLike = listInsideJsonStr.get(j).getAsJsonObject().get("main").getAsJsonObject().get("feels_like").getAsDouble();

                pressure = listInsideJsonStr.get(j).getAsJsonObject().get("main").getAsJsonObject().get("pressure").getAsInt();
                humidity = listInsideJsonStr.get(j).getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsDouble();

                windSpeed = listInsideJsonStr.get(j).getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsDouble();
                cloudCover = listInsideJsonStr.get(j).getAsJsonObject().get("clouds").getAsJsonObject().get("all").getAsInt();
                windDirection = listInsideJsonStr.get(j).getAsJsonObject().get("wind").getAsJsonObject().get("deg").getAsInt();


                Weather weather = new Weather(dt, sunrise, sunset, lon, lat, wMain, description, temperature, feelsLike, temperatureMin, temperatureMax, pressure,
                        humidity, windSpeed, cloudCover, windDirection, cityName, countryName, dtTxt);
                listInsideJsonStr.add(String.valueOf(weather));
                weatherList.add(weather);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return weatherList;
    }

    /***
     * Parsii API-kutsulla saadun stringiin tallennetun JSON-muotoisen datan ja hankkii siitä kaikki tarvittavat säätie-
     * dot muuttujiin niiden käyttöä ohjelmassa varten.
     * @param data, JSON-muotoinen säädata
     * @param lat, kaupungin latitudi
     * @param lon, kaupungin longitudi
     * @return palauttaa listan, joka on täynnä yhden päivän säätilaolioita
     */
    public List<Weather> readFromJsonsDaily(String data, double lat, double lon)
    {
        // ChatGPT:tä käytetty auttamaan tiedonhaun toiminnallisuuden ymmärtämisessä
        List<Weather> weatherList = new ArrayList<>();
        int dt = 0;
        int sunrise = 0;
        int sunset = 0;
        String wMain = "";
        String description = "";
        double temperature = 0.0;
        double feelsLike = 0.0;
        double temperatureMin = 0.0;
        double temperatureMax = 0.0;
        int pressure = 0;
        double humidity = 0.0;
        double windSpeed = 0.0;
        int cloudCover = 0;
        int windDirection = 0;
        String cityName = "";
        String countryName = "";
        String dt_text = "";
        try
        {
            JsonObject json = com.google.gson.JsonParser.parseString(data).getAsJsonObject();
            JsonArray listInsideJsonStr = json.getAsJsonArray("list");

            JsonObject city = json.getAsJsonObject("city");
            JsonObject coord = city.getAsJsonObject("coord");

            for (int i = 0; i < 7; i++)
            {
                dt = listInsideJsonStr.get(i).getAsJsonObject().get("dt").getAsInt();
                sunrise = listInsideJsonStr.get(i).getAsJsonObject().get("sunrise").getAsInt();
                sunset = listInsideJsonStr.get(i).getAsJsonObject().get("sunset").getAsInt();

                wMain = listInsideJsonStr.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
                description = listInsideJsonStr.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

                temperature = listInsideJsonStr.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();
                feelsLike = listInsideJsonStr.get(i).getAsJsonObject().get("feels_like").getAsJsonObject().get("day").getAsDouble();

                temperatureMin = listInsideJsonStr.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("min").getAsDouble();
                temperatureMax = listInsideJsonStr.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("max").getAsDouble();

                pressure = listInsideJsonStr.get(i).getAsJsonObject().get("pressure").getAsInt();
                humidity = listInsideJsonStr.get(i).getAsJsonObject().get("humidity").getAsDouble();

                windSpeed = listInsideJsonStr.get(i).getAsJsonObject().get("speed").getAsDouble();
                cloudCover = listInsideJsonStr.get(i).getAsJsonObject().get("clouds").getAsInt();
                windDirection = listInsideJsonStr.get(i).getAsJsonObject().get("deg").getAsInt();

                cityName = city.get("name").getAsString();
                countryName = city.get("country").getAsString().toLowerCase();

                Weather weather = new Weather(dt, sunrise, sunset, lon, lat, wMain, description, temperature, feelsLike, temperatureMin, temperatureMax, pressure,
                        humidity, windSpeed, cloudCover, windDirection, cityName, countryName, dt_text);
                listInsideJsonStr.add(String.valueOf(weather));

                weatherList.add(weather);
            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return weatherList;
    }
}
