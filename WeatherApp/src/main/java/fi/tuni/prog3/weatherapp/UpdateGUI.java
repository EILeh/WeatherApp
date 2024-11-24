package fi.tuni.prog3.weatherapp;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static javafx.scene.paint.Color.valueOf;

public class UpdateGUI
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
    private String temperatureAsCelsius = " °C";
    private String temperatureAsFarenheit = " F";
    private boolean isTemperatureCelsius = true;
    private Label currentWeather = new Label();
    private Label feelsLikeLabel = new Label();
    private Label windSpeedLabel = new Label();
    private String windSpeedAsMS = " m/s";
    private String windSpeedAsMPH = " mph";
    private List<Weather> dailyWeather;
    private List<Weather> hourlyWeather;
    private List<String> dates;
    private int currentDateIndex = 0;
    private String todaysDate = "";


    private Label minMaxTempLabel = new Label();

    private List<Label> hour;
    private List<ImageView> weatherIcons;
    private List<Label> temperatureLabels;
    private List<ImageView> windIcons;
    private List<Label> windSpeedLabels;
    private List<Label> pressureLabels;
    private List<Label> humidityLabels;


    private List<Label> dailyDataLabels;

    public UpdateGUI()
    {

    }

    public void setWindDirection(int windDirection)
    {
        this.windDirection = windDirection;
    }

    public void setDt(int dt)
    {
        this.dt = dt;
    }

    public void setSunrise(int sunrise)
    {
        this.sunrise = sunrise;
    }

    public void setSunset(int sunset)
    {
        this.sunset = sunset;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public void setwMain(String wMain)
    {
        this.wMain = wMain;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }

    public void setFeelsLike(double feelsLike)
    {
        this.feelsLike = feelsLike;
    }

    public void setTemperatureMin(double temperatureMin)
    {
        this.temperatureMin = temperatureMin;
    }

    public void setTemperatureMax(double temperatureMax)
    {
        this.temperatureMax = temperatureMax;
    }

    public void setPressure(int pressure)
    {
        this.pressure = pressure;
    }

    public void setHumidity(double humidity)
    {
        this.humidity = humidity;
    }

    public void setWindSpeed(double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public void setCloudCover(int cloudCover)
    {
        this.cloudCover = cloudCover;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public void setTemperatureCelsius(boolean temperatureCelsius)
    {
        isTemperatureCelsius = temperatureCelsius;
    }

    public void setDailyWeather(List<Weather> dailyWeather)
    {
        this.dailyWeather = dailyWeather;
    }

    public void setHourlyWeather(List<Weather> hourlyWeather)
    {
        this.hourlyWeather = hourlyWeather;
    }

    /***
     * Palauttaa VBoxin joka lisätään käyttöliittymään uusilla tyyleillä
     * @return VBox topVBox lisätään käyttöliittymään
     * @throws IOException
     */
    public VBox getTopVBox() throws IOException
    {
        VBox topVBox = new VBox(10);
        topVBox.setStyle("-fx-background-color: #b0e0e6;");
        return topVBox;
    }

    /***
     * Palautaa HBoxin, joka sisältää kaupungin nimen, maan kirjaimet ja lipun.
     * @return HBox citynNameHBox sisältää kaupungin nimen, maan kirjaimet ja lipun
     */
    public HBox getCityNameHBox()
    {
        HBox cityNameHBox = new HBox(10);
        Label cityNameLabel = new Label("");
        ImageView imageView = new ImageView();
        Image image;

        countryName = dailyWeather.get(currentDateIndex).getCountryName();

        try
        {
            image = new Image(new FileInputStream("icons/flags/" + countryName + ".png"));
            imageView.setImage(image);
            imageView.setFitHeight(45);
            imageView.setFitWidth(45);
            imageView.setPreserveRatio(true);
        }

        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        cityNameLabel.setText(cityName + ", " + countryName.toUpperCase());
        cityNameLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 24; -fx-font-weight: bold;" +
                "-fx-background-color: #b0e0e6;");
        cityNameHBox.getChildren().addAll(cityNameLabel, imageView);
        cityNameHBox.setAlignment(Pos.TOP_CENTER);

        return cityNameHBox;
    }

    /**
     * Käy for-loopissa läpi tuntikohtaisen datan Labeleitä ja asettaa niihin uudet tiedot riippuen halutaanko metrics
     * vai imperial.
     */
    public void changeHourlyImperialMetricsUnit()
    {
        for (int i = 0; i < 36 ; i++)
        {
            double tempTemperature = hourlyWeather.get(i).getTemperature();
            double tempWindSpeed = hourlyWeather.get(i).getWindSpeed();

            // fahreneheit/MPH
            if (!isTemperatureCelsius)
            {
                tempTemperature = (9.0 / 5.0) * tempTemperature + 32;
                tempWindSpeed = tempWindSpeed * 2.23694;
                temperatureLabels.get(i).setText(Math.round(tempTemperature) + temperatureAsFarenheit);
                windSpeedLabels.get(i).setText(String.valueOf(Math.round(tempWindSpeed)) + windSpeedAsMPH);
            }

            // celsius/m/S
            else
            {
                temperatureLabels.get(i).setText(Math.round(tempTemperature) + temperatureAsCelsius);
                windSpeedLabels.get(i).setText(String.valueOf(Math.round(tempWindSpeed)) + windSpeedAsMS);
            }

        }
    }

    /***
     * Käy for-loopissa läpi päiväkohtaisen datan Labeleitä ja asettaa niihin uudet tiedot riippuen halutaanko metrics
     * vai imperial.
     */
    public void changeDailyImperialMetricsUnit()
    {
        for (int i = 0; i < dailyDataLabels.size(); i++)
        {
            double tempTemperatureMin = dailyWeather.get(i).getTemperatureMin();
            double tempTemperatureMax = dailyWeather.get(i).getTemperatureMax();

            // celsius/m/S
            if (isTemperatureCelsius)
            {
                dailyDataLabels.get(i).setText(Math.round(tempTemperatureMin) + "..." + Math.round(tempTemperatureMax) +
                        temperatureAsCelsius);
            }

            // fahreneheit/MPH
            else
            {
                tempTemperatureMin = (9.0 / 5.0) * tempTemperatureMin + 32;
                tempTemperatureMax =  (9.0 / 5.0) * tempTemperatureMax + 32;
                dailyDataLabels.get(i).setText(Math.round(tempTemperatureMin) + "..." + Math.round(tempTemperatureMax) +
                        temperatureAsFarenheit);
            }
        }
    }

    /***
     * Muuttaa tämän hetkisen säätilan Labeleitä ja asettaa niihin uudet tiedot riippuen halutaanko metrics vai
     * imperial.
     * @param imperialBtn Muutetaan riippuen siitä, halutaanko metrics vai imperial
     * @throws Exception
     */
    public void changeImperialMetricsUnits(Button imperialBtn) throws Exception
    {
        String feelsLikeAsStr = "";
        String windSpeedStr = "";

        // fahreneheit/MPH
        if (isTemperatureCelsius)
        {
            imperialBtn.setText("Metric");
            temperature = (9.0 / 5.0) * temperature + 32;
            currentWeather.setText(Math.round(temperature) + temperatureAsFarenheit);

            feelsLike = (9.0 / 5.0) * feelsLike + 32;
            feelsLikeAsStr = String.valueOf(Math.round(feelsLike));
            feelsLikeLabel.setText("Feels Like " + feelsLikeAsStr + temperatureAsFarenheit);
            feelsLikeLabel.setStyle("-fx-font-family: SansSerif");

            temperatureMin = (9.0 / 5.0) * temperatureMin + 32;
            temperatureMax = (9.0 / 5.0) * temperatureMax + 32;

            windSpeed = windSpeed * 2.23694;
            windSpeedStr = String.valueOf(Math.round(windSpeed));
            windSpeedLabel.setText(windSpeedStr + windSpeedAsMPH);
            windSpeedLabel.setStyle("-fx-font-family: SansSerif");

            isTemperatureCelsius = !(isTemperatureCelsius);

        }

        // celsius/m/S
        else
        {
            temperature = (temperature - 32) * (5.0 / 9.0);
            currentWeather.setText(Math.round(temperature) + temperatureAsCelsius);

            feelsLike = (feelsLike - 32) * (5.0 / 9.0) ;
            feelsLikeAsStr = String.valueOf(Math.round(feelsLike));
            feelsLikeLabel.setText("Feels Like " + feelsLikeAsStr + temperatureAsCelsius);

            temperatureMin = (temperatureMin - 32) * (5.0 / 9.0) ;
            temperatureMax = (temperatureMax - 32) * (5.0 / 9.0) ;

            windSpeed = windSpeed / 2.23694;
            windSpeedStr = String.valueOf(Math.round(windSpeed));
            windSpeedLabel.setText(windSpeedStr + windSpeedAsMS);

            isTemperatureCelsius = !(isTemperatureCelsius);
            imperialBtn.setText("Imperial");
        }
    }

    /***
     * Hakee sään yksityiskohdat ja asettaa ne näkyviin.
     * @return HBox weatherDetailsHBox yksityiskohdat HBOxissa
     * @throws IOException
     */
    public HBox getTopHBox() throws IOException
    {
        HBox weatherDetailsHBox = new HBox();
        VBox weatherVBox = new VBox(10);
        int imageSize = 200;

        wMain = dailyWeather.get(0).getwMain();
        windSpeed = dailyWeather.get(0).getWindSpeed();
        temperature = dailyWeather.get(0).getTemperature();

        ImageView imageView = getImage(imageSize, wMain, windSpeed, cloudCover, temperature);

        weatherDetailsHBox.setPrefHeight(300);
        weatherDetailsHBox.setPrefWidth(300);

        HBox centerHBox = new HBox();
        centerHBox.setPrefHeight(300);
        centerHBox.setPrefWidth(300);

        weatherDetailsHBox.setStyle("-fx-background-color: #b0e0e6;");
        centerHBox.setStyle("-fx-background-color: #b0e0e6;");

        currentWeather.setText(Math.round(temperature) + temperatureAsCelsius);
        currentWeather.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 32; -fx-font-weight: bold;");

        centerHBox.setAlignment(Pos.CENTER);
        centerHBox.getChildren().addAll(imageView, currentWeather);

        HBox descriptionAndWindDirecAr = new HBox();

        description = dailyWeather.get(0).getDescription();
        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 20; -fx-font-weight: bold;");

        windDirection = dailyWeather.get(0).getWindDirection();
        ImageView windDirectionArrow = getWindDirection(windDirection);
        windDirectionArrow.setFitHeight(50);
        windDirectionArrow.setFitWidth(50);
        windDirectionArrow.setPreserveRatio(true);

        windSpeed = dailyWeather.get(0).getWindSpeed();
        String windSpeedStr = String.valueOf(windSpeed);
        windSpeedLabel.setText(windSpeedStr + windSpeedAsMS);
        windSpeedLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 20; -fx-font-weight: bold;");

        descriptionAndWindDirecAr.getChildren().addAll(descriptionLabel, windDirectionArrow, windSpeedLabel);

        weatherVBox.getChildren().addAll(centerHBox, descriptionAndWindDirecAr, currentWeatherDetails());

        weatherDetailsHBox.getChildren().addAll(weatherVBox);

        return weatherDetailsHBox;

    }

    /***
     * Valitsee kuvan muuttujien perusteella, jonka tiedostosijainti tallennetaan, kuva avataan ja palautetaan sekä
     * kutsufunktiossa lisätään käyttöliittymään.
     * @param imageSize kuvan koko
     * @param currentWmain kuvaus esim. Clouds
     * @param currentWindSpeed tuulennopeus
     * @param currentCloudCover pilvipeite
     * @param currentTemperature lämpötila
     * @return ImageView imageView kuva muuttujien perusteella
     * @throws FileNotFoundException
     */
    public ImageView getImage(int imageSize, String currentWmain, double currentWindSpeed, int currentCloudCover,
                              double currentTemperature) throws FileNotFoundException
    {
        String imagePath = "";

        switch (currentWmain)
        {
            case "Clouds":
                if ((currentWindSpeed >= 21) && (currentCloudCover > 60))
                {
                    imagePath = "icons/weather/windy and cloudy.png";
                }
                else if (currentWindSpeed > 21)
                {
                    imagePath = "icons/weather/windy.png";
                }
                else
                {
                    imagePath = "icons/weather/cloudy.png";
                }
                break;

            case "Fog":
            case "Haze":
            case "Mist":
                imagePath = "icons/weather/hail.png";
                break;

            case "Clear":
            case "Sunny":
                if ((currentCloudCover <= 20) && (currentTemperature <= -5))
                {
                    imagePath = "icons/weather/cold and sunny.png";
                }
                else if ((currentCloudCover <= 20) && (currentTemperature > -5))
                {
                    imagePath = "icons/weather/sun.png";
                }
                else
                {
                    imagePath = "icons/weather/sun behind clouds.png";
                }
                break;

            case "Snow":
                imagePath = "icons/weather/snow.png";
                break;

            case "Rain":
                if (currentWindSpeed >= 21)
                {
                   imagePath = "icons/weather/windy and rainy.png";
                }
                else if ((currentTemperature < 0) && (currentTemperature > -6))
                {
                    imagePath = "icons/weather/rain and snow.png";
                }
                else if (currentTemperature <= -6)
                {
                    imagePath = "icons/weather/snowflake.png";
                }
                else
                {
                    imagePath = "icons/weather/rain.png";
                }
                break;

            default:
                if (currentTemperature < 0)
                {
                    imagePath = "icons/weather/cold.png";
                }
                else
                {
                    imagePath = "icons/weather/hot.png";
                }
                break;
            }

        Image image = new Image(new FileInputStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(imageSize);
        imageView.setFitWidth(imageSize);
        imageView.setPreserveRatio(true);

        return imageView;

    }

    /***
     * Tämänhetkisen säätietojen asettelu, hakee kuvauksen, nuolen kuvan, joka osoitaa tuulen suunnan, tuulennopeuden,
     * miltä sää tuntuu, auringonnousun ja -laskun sekä sään kosteuden ja asettelee ne GridPaneen, joka palautetaan ja
     * lisätään käyttöliitymään.
     * @return GridPane gridPane lisätään käyttöliittymään tämänhetkisen sään tiedoilla
     * @throws FileNotFoundException
     */
    private GridPane currentWeatherDetails() throws FileNotFoundException
    {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        gridPane.setVgap(10);

        feelsLike = dailyWeather.get(0).getFeelsLike();
        String feelsLikeAsStr = String.valueOf(feelsLike);
        feelsLikeLabel.setText("Feels Like " + feelsLikeAsStr + temperatureAsCelsius);
        feelsLikeLabel.setStyle("-fx-font-family: 'SansSerif';");

        // Muutetaan sunrise tieto muotoon, josta tunnit saadaan
        long timeStampSunrise = dailyWeather.get(0).getSunrise();
        LocalDateTime dateTimeSunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStampSunrise),
                ZoneOffset.UTC);
        DateTimeFormatter sunriseFormatter = DateTimeFormatter.ofPattern("HH", Locale.ENGLISH);
        String sunriseAsStr = sunriseFormatter.format(dateTimeSunrise);
        Label sunriseLabel = new Label("Sunrise: " + sunriseAsStr + ":00");
        sunriseLabel.setStyle("fx-font-family: 'SansSerif';");

        // Muutetaan sunset tieto muotoon, josta tunnit saadaan
        long timeStampSunset = dailyWeather.get(0).getSunset();
        LocalDateTime dateTimeSunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStampSunset),
                ZoneOffset.UTC);
        DateTimeFormatter sunsetFormatter = DateTimeFormatter.ofPattern("HH", Locale.ENGLISH);
        String sunsetAsStr = sunsetFormatter.format(dateTimeSunset);
        Label sunsetLabel = new Label("Sunset: " + sunsetAsStr + ":00");
        sunsetLabel.setStyle("fx-font-family: 'SansSerif';");

        humidity = dailyWeather.get(0).getHumidity();
        String humidityStr = String.valueOf(humidity);
        Label humidityLabel = new Label("Humidity " + humidityStr);
        humidityLabel.setStyle("-fx-font-family: 'SansSerif';");

        gridPane.add(feelsLikeLabel, 0, 1);
        gridPane.add(sunriseLabel, 0, 2);
        gridPane.add(sunsetLabel, 1, 2);
        gridPane.add(humidityLabel, 1, 1);

        return gridPane;
    }

    /***
     * Funktio tarkastelee tuulen suuntaa ja valitsee tuulensuunta-arvoa vastaavaan ilmansuuntaan osoittavan nuolikuvan.
     * Tiedosto haetaan levyltä try-catch rakenteessa, ja palautetaan imageviewiin asetettuna, mikäli sen lataaminen
     * levyltä onnistui ongelmitta.
     * @param currentWindDirection, tarkka tuulen suunta kokonaislukuna
     * @return, palautetaan imageview-olio, johon asetettu itse kuva
     * @throws FileNotFoundException
     */
    private ImageView getWindDirection(int currentWindDirection) throws FileNotFoundException
    {
        String windDirectionStr = "";
        FileInputStream inputstream;
        String imagePath = "";

        Image image;
        ImageView imageView;

        if (((currentWindDirection >= 337) && (currentWindDirection <= 360)) || (currentWindDirection >= 0)
                && (currentWindDirection <= 22))
        {
            imagePath = "icons/arrows/north_arrow.png";
        }
        else if ((currentWindDirection >= 23) && (currentWindDirection <= 67))
        {
            imagePath = "icons/arrows/north_east_arrow.png";
        }

        else if ((currentWindDirection >= 68) && (currentWindDirection <= 112))
        {
            imagePath = "icons/arrows/east_arrow.png";
        }

        else if ((currentWindDirection >= 113) && (currentWindDirection <= 157))
        {
            imagePath = "icons/arrows/south_east_arrow.png";
        }

        else if ((currentWindDirection >= 158) && (currentWindDirection <= 247))
        {
            imagePath = "icons/arrows/south_west_arrow.png";
        }

        else if ((currentWindDirection >= 248) && (currentWindDirection <= 292))
        {
            imagePath = "icons/arrows/west_arrow.png";
        }

        else
        {
            imagePath = "icons/arrows/north_west_arrow.png";

        }
        inputstream = new FileInputStream(imagePath);
        image = new Image(inputstream);
        imageView = new ImageView(image);

        return imageView;
    }

    /***
     * Luo keskimmäisen alueen käyttöliittymään, jossa on daily sään tiedot eli päiväkohtainen yleisennuste. Asetetaan
     * sille ulkoasumääreitä, mm. maksimikoot ja taustavärit.
     * @return HBOX, joka on keskimmäinen alue käyttöliittymässä
     * @throws FileNotFoundException yritetään avata kuvaa toisessa funktiossa, jota täältä kutsutaan
     */
    public HBox getCenterBoxLabels() throws FileNotFoundException
    {

        HBox box = new HBox();
        //UpdateGUI updateGUI = new UpdateGUI();

        box.setMaxHeight(350);
        box.setPrefWidth(300);

        Region leftSpacer = new Region();
        leftSpacer.setBackground(new Background(new BackgroundFill(valueOf("#8fc6fd"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        Region rightSpacer = new Region();
        rightSpacer.setBackground(new Background(new BackgroundFill(valueOf("#8fc6fd"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        box.getChildren().add(leftSpacer);

        VBox vBox;

        String currentWmain;
        double currentWindSpeed;
        int currentCloudCover;
        double currentTemperature;
        double currentMinTemperature;
        double currentMaxTemperature;

        dailyDataLabels = new ArrayList<>(7);

        int imageSize = 100;

        // Hankitaan ja asetetaan daily tietoja näkyviin
        for (int i = 0; i < 7; i++)
        {
            vBox = new VBox();

            vBox.setId(String.valueOf(i));

            vBox.setMinWidth(200);
            Label dateLabel = new Label("");
            dateLabel.setMinWidth(200);
            HBox imageHBox = new HBox();

            imageHBox.setMinWidth(200);
            minMaxTempLabel = new Label("");

            minMaxTempLabel.setMinWidth(200);
            imageHBox.setMinWidth(200);
            box.setBackground(new Background(new BackgroundFill(Color.valueOf("#8fc6fd"), new CornerRadii(10),
                    Insets.EMPTY)));

            currentWmain = dailyWeather.get(i).getwMain();
            currentWindSpeed = dailyWeather.get(i).getWindSpeed();
            currentCloudCover = dailyWeather.get(i).getCloudCover();
            currentTemperature = dailyWeather.get(i).getTemperature();
            currentMinTemperature = dailyWeather.get(i).getTemperatureMin();
            currentMaxTemperature = dailyWeather.get(i).getTemperatureMax();

            ImageView imageView = getImage(imageSize, currentWmain, currentWindSpeed, currentCloudCover,
                    currentTemperature);
            imageHBox.getChildren().add(imageView);

            long timestamp = dailyWeather.get(i).getDt();
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE dd-MM-yyyy", Locale.ENGLISH);
            String formattedDate = date.format(formatter);

            dateLabel.setText(formattedDate);
            minMaxTempLabel.setText(Math.round(currentMinTemperature) + " ... " + Math.round(currentMaxTemperature) +
                    temperatureAsCelsius);

            imageHBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#dafeff"), CornerRadii.EMPTY,
                    Insets.EMPTY)));
            minMaxTempLabel.setBackground(new Background(new BackgroundFill(Color.valueOf("#dafeff"),
                    new CornerRadii(0, 0, 10, 10, false), Insets.EMPTY)));
            dateLabel.setBackground(new Background(new BackgroundFill(Color.valueOf("#dafeff"),
                    new CornerRadii(10, 10, 0, 0, false), Insets.EMPTY)));
            dateLabel.setStyle("-fx-font-family: SansSerif");

            vBox.getChildren().addAll(dateLabel, imageHBox, minMaxTempLabel);

            Region spacer1 = new Region();
            spacer1.setMinWidth(30);
            spacer1.setStyle("-fx-background-color: #8fc6fd;");

            Region spacer2 = new Region();
            spacer2.setMinWidth(30);
            spacer2.setStyle("-fx-background-color: #8fc6fd;");

            vBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#8fc6fd"), new CornerRadii(10),
                    Insets.EMPTY)));

            Region bottomSpacer = new Region();

            bottomSpacer.setBackground(new Background(new BackgroundFill(Color.valueOf("#8fc6fd"), CornerRadii.EMPTY,
                    Insets.EMPTY)));
            VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
            vBox.getChildren().addAll(bottomSpacer);

            dailyDataLabels.add(minMaxTempLabel);

            box.getChildren().addAll(spacer1, vBox, spacer2);

        }
        box.getChildren().add(rightSpacer);
        return box;
    }

    /***
     * Luo VBoxin, joka sisältää Daily Labelin.
     * @return VBox centerVBox dailyLabel tietojen kanssa käyttöliittymään
     * @throws IOException
     */
    public VBox getCenterVBox() throws IOException
    {
        VBox centerVBox = new VBox();

        centerVBox.setSpacing(40);
        centerVBox.setMinHeight(250);
        centerVBox.setMaxHeight(400);

        centerVBox.setStyle("-fx-background-color: #8fc6fd;");
        Label dailyLabel = new Label("Daily");
        dailyLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold");

        centerVBox.getChildren().add(dailyLabel);

        return centerVBox;
    }

    /***
     * Luo ScrollPanen, jolloin tietoja pystyy selaamaan. Funktio on kokonaan ChatGPT:n luoma.
     * @return ScrollPane scrollPane lisätään scrollpane haluttuun kohtaan käyttöliittymässä
     */
    public ScrollPane getScrollPane()
    {
        // ChatGPT
        // |
        // |
        // v
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.addEventFilter(ScrollEvent.ANY, Event::consume);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
        // ^
        // |
        // |
    }

    /***
     * Luo VBoxin, joka sisältää Hourly Labelin.
     * @return VBox bottomVBox hourlyLabel tietojen kanssa käyttöliittymään
     */
    public VBox getBottomVBox()
    {
        VBox bottomVBox = new VBox(10);
        bottomVBox.setSpacing(20);

        Label hourlyLabel = new Label("Hourly");

        bottomVBox.setMinHeight(200);

        bottomVBox.setStyle("-fx-background-color: #b1c2d4;");
        bottomVBox.setAlignment(Pos.CENTER);

        hourlyLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 30; -fx-background-color: #b1c2d4; -fx-font-weight: bold");

        bottomVBox.getChildren().addAll(hourlyLabel);

        return bottomVBox;
    }

    /***
     * Hakee tuntikohtaisen datan tiedot ennen kuin ne asetetaan käyttöliittymään ohjelman nopeuttamiseksi. Tiedot
     * tallennetaan listoihin, jotta ne on helppo ja nopeampi hakea indeksin perusteella, kun niitä tarvitaan eikä
     * aikaa kulu niiden luomiseen.
     * @throws FileNotFoundException
     */
    public void getHourlyDetailLists() throws FileNotFoundException
    {
        hour = new ArrayList<>(36);
        Label dayLabel;

        weatherIcons = new ArrayList<>(36);
        ImageView imageView;

        temperatureLabels = new ArrayList<>(36);
        String hourlyTemperature;
        Label hourlyTemperatureLabel;

        windIcons = new ArrayList<>(36);
        ImageView imageView1;

        windSpeedLabels = new ArrayList<>(36);
        String hourlyWindSpeed;
        Label hourlyWindSpeedLabel;

        pressureLabels = new ArrayList<>(36);
        String hourlyPressure;
        Label hourlyPressureLabel;

        humidityLabels = new ArrayList<>(36);
        String hourlyHumidity;
        Label humidityLabel;

        LocalDateTime dateTime = LocalDateTime.parse(hourlyWeather.get(currentDateIndex).getDtTxt(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String startTime = dateTime.format(DateTimeFormatter.ofPattern("HH"));

        int startTimeInt = Integer.parseInt(startTime);

        // Muuttuja pitää kirjaa tämän hetkisestä tunnista, jotta aloitetaan tuntien käyminen oikeasta kellonajasta
        int temp = startTimeInt;

        for (int i = 0; i < 36; i++)
        {
            // Nollataan muuttuja, koska päästy kellon ympäri
            if (temp == 24)
            {
                temp = 0;
            }

            dayLabel = new Label(String.valueOf(temp));
            dayLabel.setMinSize(40, 40);
            dayLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 15; -fx-background-color: #b1c2d4;");
            hour.add(dayLabel);

            imageView = getImage(50,
            hourlyWeather.get(i).getwMain(),
            hourlyWeather.get(i).getWindSpeed(),
            hourlyWeather.get(i).getCloudCover(),
            hourlyWeather.get(i).getTemperature());
            weatherIcons.add(imageView);

            hourlyTemperature = String.valueOf(Math.round(hourlyWeather.get(i).getTemperature()));
            hourlyTemperatureLabel = new Label(hourlyTemperature + temperatureAsCelsius);
            temperatureLabels.add(hourlyTemperatureLabel);

            imageView1 = getWindDirection(hourlyWeather.get(i).getWindDirection());
            imageView1.setFitHeight(20);
            imageView1.setFitWidth(20);
            windIcons.add(imageView1);

            hourlyWindSpeed = String.valueOf(Math.round(hourlyWeather.get(i).getWindSpeed()));
            hourlyWindSpeedLabel = new Label(hourlyWindSpeed + windSpeedAsMS);
            windSpeedLabels.add(hourlyWindSpeedLabel);

            hourlyPressure = String.valueOf(hourlyWeather.get(i).getPressure());
            hourlyPressureLabel = new Label(hourlyPressure);
            pressureLabels.add(hourlyPressureLabel);

            hourlyHumidity = String.valueOf(hourlyWeather.get(i).getHumidity());
            humidityLabel = new Label (hourlyHumidity);
            humidityLabels.add(humidityLabel);

            temp++;
        }
    }

    /***
     * Käyttää funktiossa getHourlyDetailLists tallennettuja tietoja ja luo gridPanen sen pohjalta.
     * @return GridPane gridPane lisätään käyttöliittymään, sisältää tuntikohtaiset säätiedot
     * @throws FileNotFoundException
     */
    public GridPane getBottomGridPane() throws FileNotFoundException
    {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(0, 0, 25, 0));

        gridPane.setMaxHeight(0);
        gridPane.setStyle("-fx-background-color: #b1c2d4;");

        for (int i = 0; i < 36; i++)
        {
            gridPane.add(hour.get(i), i, 0);

            // ChatGPT
            // |
            // |
            // v
            hour.get(i).setAlignment(Pos.CENTER);
            hour.get(i).setWrapText(true);
            // ^
            // |
            // |

            gridPane.add(weatherIcons.get(i), i, 1);

            gridPane.add(temperatureLabels.get(i), i, 2);

            gridPane.add(windIcons.get(i), i, 3);

            gridPane.add(windSpeedLabels.get(i), i, 4);

            gridPane.add(pressureLabels.get(i), i, 5);

            gridPane.add(humidityLabels.get(i), i, 6);

        }

        gridPane.setAlignment(Pos.TOP_CENTER);

        return gridPane;
    }
}
