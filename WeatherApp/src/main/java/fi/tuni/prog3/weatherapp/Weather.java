package fi.tuni.prog3.weatherapp;

public class Weather
{
    private int dt = 0;
    private int sunrise = 0;
    private int sunset = 0;
    private double lon = 0.0;
    private double lat = 0.0;
    private String wMain = "";
    private String description = "";
    private double temperature = 0.0;
    private double feelsLike = 0.0;
    private double temperatureMin = 0.0;
    private double temperatureMax = 0.0;
    private int pressure = 0;
    private double humidity = 0.0;
    private double windSpeed = 0.0;
    private int cloudCover = 0;
    private int windDirection = 0;
    private String cityName = "";
    private String countryName = "";
    private String dtTxt = "";

    /***
     * Muodostaa Weather olion, joka sisältää eri olioiden säätiedot.
     * @param dt
     * @param sunrise
     * @param sunset
     * @param lon
     * @param lat
     * @param wMain
     * @param description
     * @param temperature
     * @param feelsLike
     * @param temperatureMin
     * @param temperatureMax
     * @param pressure
     * @param humidity
     * @param windSpeed
     * @param cloudCover
     * @param windDirection
     * @param cityName
     * @param countryName
     * @param dtTxt
     */

    public Weather(int dt, int sunrise, int sunset, double lon, double lat, String wMain, String description, double temperature, double feelsLike, double temperatureMin,
                   double temperatureMax, int pressure, double humidity, double windSpeed, int cloudCover,
                   int windDirection, String cityName, String countryName, String dtTxt)

    {
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.lon = lon;
        this.lat = lat;
        this.wMain = wMain;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.cloudCover = cloudCover;
        this.windDirection = windDirection;
        this.cityName = cityName;
        this.countryName = countryName;
        this.dtTxt = dtTxt;
    }

    public int getDt()
    {
        return dt;
    }

    public int getSunrise()
    {
        return sunrise;
    }

    public int getSunset()
    {
        return sunset;
    }

    public double getLon()
    {
        return lon;
    }

    public double getLat()
    {
        return lat;
    }

    public String getwMain()
    {
        return wMain;
    }

    public double getFeelsLike()
    {
        return feelsLike;
    }

    public String getDescription()
    {
        return description;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public double getTemperatureMin()
    {
        return temperatureMin;
    }

    public double getTemperatureMax()
    {
        return temperatureMax;
    }

    public int getPressure()
    {
        return pressure;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public int getCloudCover()
    {
        return cloudCover;
    }

    public int getWindDirection()
    {
        return windDirection;
    }

    public String getCityName()
    {
        return cityName;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public String getDtTxt()
    {
        return dtTxt;
    }

}
