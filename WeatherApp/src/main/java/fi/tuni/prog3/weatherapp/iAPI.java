package fi.tuni.prog3.weatherapp;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 */
public interface iAPI
{

    /**
     * Palauttaa paikkakunnan koordinaatit.
     * @param loc Paikkakunnan nimi, jonka koordinaatit haetaan.
     * @return String.
     */
    public String lookUpLocation(String loc);

    /**
     * Hakee tuntikohtaisen datan annetuille koordinaateille.
     * @param lat paikkakunnan leveysaste.
     * @param lon paikkakunnan pituusaste.
     * @return String.
     */
    public String getHourlyWeather(double lat, double lon);

    /**
     * Hakee päiväkohtaisen datan annetuille koordinaateille.
     * @param lat paikkakunnan leveysaste.
     * @param lon paikkakunnan pituusaste.
     * @return String.
     */
    public String getDailyWeather(double lat, double lon);
}
