package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeatherDetails implements iAPI
{
    private static final String API_KEY = "";
    String city = "";

    /***
     *
     * @param city
     */
    public WeatherDetails(String city)
    {
        this.city = city;
    }

    /***
     * Hakee paikkakunnan nimen perusteella koordinaatit.
     * @param loc Paikkakunnan nimi, jonka koordinaatit haetaan.
     * @return
     */
    @Override
    public String lookUpLocation(String loc)
    {
        List<Weather> weatherList = new ArrayList<>();

        String data = "";
        String lonAndLat = "";

        // https://api.openweathermap.org/data/2.5/weather?q=Kangasala&appid=7e5f6d6f302c3ee0ca1f370b871d905a&units=metric
        String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = null;
        try
        {
            url = new URL(urlStr);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        try
        {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e)
        {
            throw new RuntimeException(e);
        }

        InputStream in = null;
        try
        {
            in = conn.getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        data = scanner.hasNext() ? scanner.next() : "";

        scanner.close();
        conn.disconnect();


        try
        {
            JsonObject json = JsonParser.parseString(data).getAsJsonObject();
            JsonObject coord = json.getAsJsonObject("coord");

            String lon = String.valueOf(coord.get("lon"));
            String lat = String.valueOf(coord.get("lat"));

            lonAndLat = lon + "," + lat;

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lonAndLat;
    }

    /***
     * Hakee paikkakunnan tuntikohtaiset tiedot leveys- ja pituusasteen perusteella.
     * @param lat paikkakunnan leveysaste
     * @param lon paikkakunnan pituusaste
     * @return
     */
    @Override
    public String getHourlyWeather(double lat, double lon)
    {
        // // https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
        String urlStr = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=" + lat + "&lon=" + lon + "&cnt=" + 36 + "&appid=" + API_KEY + "&units=metric";
        URL url = null;
        try
        {
            url = new URL(urlStr);
        }

        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn = null;

        try
        {
            conn = (HttpURLConnection) url.openConnection();

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            conn.setRequestMethod("GET");
        }

        catch (ProtocolException e)
        {
            throw new RuntimeException(e);
        }

        InputStream in = null;

        try
        {
            in = conn.getInputStream();
        }

        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        String content = scanner.hasNext() ? scanner.next() : "";

        scanner.close();
        conn.disconnect();

        return content;

    }

    /***
     * Hakee paikkakunnan päiväkohtaiset tiedot leveys- ja pituusasteen perusteella.
     * @param lat paikkakunnan leveysaste
     * @param lon paikkakunnan pituusaste
     * @return
     */
    @Override
    public String getDailyWeather(double lat, double lon)
    {
        String urlStr = "https://api.openweathermap.org/data/2.5/forecast/daily?lat="  + lat + "&lon=" + lon + "&cnt=" + 7 + "&appid=" + API_KEY + "&units=metric";
        URL url = null;
        try
        {
            url = new URL(urlStr);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        try
        {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e)
        {
            throw new RuntimeException(e);
        }

        InputStream in = null;
        try
        {
            in = conn.getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        String content = scanner.hasNext() ? scanner.next() : "";

        scanner.close();
        conn.disconnect();

        return content;
    }
}
