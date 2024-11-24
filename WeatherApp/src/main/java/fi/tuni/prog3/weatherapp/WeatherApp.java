package fi.tuni.prog3.weatherapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.awt.Desktop;
import java.net.URI;

import static javafx.scene.paint.Color.valueOf;


/**
 * JavaFX Weather Application.
 */
public class WeatherApp extends Application
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
    private double humidity = 0.0;
    private int pressure = 0;
    private double windSpeed = 0.0;
    private int cloudCover = 0;
    private int windDirection = 0;
    private String cityName = "";
    private String countryName = "";
    private String dtTxt = "";
    private String temperatureAsCelsius = " °C";
    private String temperatureAsFarenheit = " F";
    private boolean isTemperatureCelsius = true;
    private String windSpeedAsMS = " m/s";
    private String windSpeedAsMPH = " mph";
    private WeatherDetails weatherDetails;
    private List<Weather> dailyWeather;
    private List<Weather> hourlyWeather;
    private BorderPane root;
    private Stage stage1;
    private Stage citiesStage;
    private LinkedHashSet<String> favouriteCities;
    private LinkedList<String> history;
    private int currentDateIndex;
    private Double compareLon = 0.0;
    private Double compareLat = 0.0;
    private boolean wasCityFound;
    private UpdateGUI updateGUI;
    private Label currentWeather;
    private Label feelsLikeLabel;
    private Label windSpeedLabel;

    // Oletusrakentaja
    public WeatherApp()
    {
        this.updateGUI = new UpdateGUI();
    }

    /***
     *
     * @param stage päästage, johon ohjelman scene asetetaan
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        stage1 = stage;
        String iconPath = "icons/weather/sun.png";
        File iconFile = new File(iconPath);
        String iconFileToString = iconFile.toURI().toString();
        Image icon = new Image(iconFileToString);
        stage1.getIcons().add(icon);
        getStage(updateGUI);
    }

    /***
     * Javan sisäänrakennettu päämetodi. Käynnistää JavaFX-kirjaston oletusmetodin launch();.
     * @param args
     */
    public static void main(String[] args)
    {
        launch();
    }

    /***
     * Funktio asettaa arvoja updateGUI oliolle, jota käytetään GUI:n päivittämiseen. Ottaa upgradeGUI-olion
     * parametrinä, ei palauta mitään.
     * @param updateGUI, olio
     */
    private void setCurrentWeatherDetails(UpdateGUI updateGUI)
    {
        updateGUI.setLon(lon);
        updateGUI.setLat(lat);
        updateGUI.setDailyWeather(dailyWeather);
        updateGUI.setHourlyWeather(hourlyWeather);

        dt = dailyWeather.get(0).getDt();
        updateGUI.setDt(dt);

        sunrise = dailyWeather.get(0).getSunrise();
        updateGUI.setSunrise(sunrise);

        sunset = dailyWeather.get(0).getSunset();
        updateGUI.setSunset(sunset);

        wMain = dailyWeather.get(0).getwMain();
        updateGUI.setwMain(wMain);

        description = dailyWeather.get(0).getDescription();
        updateGUI.setDescription(description);

        temperature = dailyWeather.get(0).getTemperature();
        updateGUI.setTemperature(temperature);

        feelsLike = dailyWeather.get(0).getFeelsLike();
        updateGUI.setFeelsLike(feelsLike);

        temperatureMin = dailyWeather.get(0).getTemperatureMin();
        updateGUI.setTemperatureMin(temperatureMin);

        temperatureMax = dailyWeather.get(0).getTemperatureMax();
        updateGUI.setTemperatureMax(temperatureMax);

        pressure = dailyWeather.get(0).getPressure();
        updateGUI.setPressure(pressure);

        humidity = dailyWeather.get(0).getHumidity();
        updateGUI.setHumidity(humidity);

        windSpeed = dailyWeather.get(0).getWindSpeed();
        updateGUI.setWindSpeed(windSpeed);

        cloudCover = dailyWeather.get(0).getCloudCover();
        updateGUI.setCloudCover(cloudCover);

        windDirection = dailyWeather.get(0).getWindDirection();
        updateGUI.setWindDirection(windDirection);

        updateGUI.setCityName(cityName);

        countryName = dailyWeather.get(0).getCountryName();
        updateGUI.setCountryName(countryName);

        updateGUI.setTemperatureCelsius(isTemperatureCelsius);
    }

    /***
     * Alustaa ohjelman toiminnan ja luo pohjan käyttöliittymälle. Asettaa käyttöliittymälle oletuskoon.
     * Alustaa sitä tarven tarvittavat tietorakenteet sekä GUI-palikat.
     * Yrittää tallentaa historian ja suosikit levylle, mikäli niissä on jotain ohjelmaa sulkiessa.
     * @param updateGUI käyttöliittymäolio
     * @throws Exception
     */
    private void getStage(UpdateGUI updateGUI) throws Exception
    {
        AnchorPane anchorPane = new AnchorPane();
        favouriteCities = new LinkedHashSet<>();
        history = new LinkedList<>();

        FileHandler fileHandler = new FileHandler();
        fileHandler.readFromFile("favourites", "history", favouriteCities, history);

        String cityToBeSearched = "";

        if (!history.isEmpty())
        {
            cityToBeSearched = history.get(history.size() - 1);
        }

        root = new BorderPane();
        setWeatherFetcher(cityToBeSearched);

        setCurrentWeatherDetails(updateGUI);

        root.setPadding(new Insets(10, 10, 10, 10));

        updateGuiComponents(updateGUI);

        anchorPane.getChildren().add(root);

        // ChatGPT
        // |
        // |
        // v
        anchorPane.setPrefSize(600, 600);
        AnchorPane.setTopAnchor(root, 10.0);
        AnchorPane.setLeftAnchor(root, 10.0);
        AnchorPane.setRightAnchor(root, 10.0);
        AnchorPane.setBottomAnchor(root, 10.0);
        // ^
        // |
        // |

        Scene scene = new Scene(anchorPane);

        stage1.setOnCloseRequest(event ->
        {
            FileHandler fileHandler1 = new FileHandler();
            try
            {
                fileHandler1.writeToFile("favourites",  "history", favouriteCities, history);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

        });

        stage1.setScene(scene);
        stage1.setMinHeight(600);
        stage1.setMinWidth(800);
        stage1.setTitle("WeatherApp");
        stage1.show();
    }

    /***
     * Funktioon tullaan, jos haettua kaupunkia ei löytynyt, ts. sitä ei ole olemassa. Luo uuden ikkunan tätä varten,
     * jossa näytetään informoiva teksti aiheesta. Käyttää kaupungin nimeä osana virheviestiä. Ei palauta arvoa.
     * @param cityToBeSearched, kaupunki jolla hakua yritettiin suorittaa
     */
    private void errorWindow(String cityToBeSearched)
    {
        Stage errorWindowStage = new Stage();
        VBox vBox = new VBox(20);
        Label errorLabel = new Label("The city " + cityToBeSearched + " could not be found");
        errorLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");

        Button okBtn = new Button("OK");
        okBtn.setStyle("-fx-font-family: SansSerif");

        // Kursori muuttuu, jos hiiri viedään päälle
        okBtn.setOnMouseEntered(mouseEvent ->
        {
            okBtn.setCursor(Cursor.HAND);
        });

        vBox.getChildren().addAll(errorLabel, okBtn);

        okBtn.setOnAction((ActionEvent event) ->
        {
            errorWindowStage.close();
        });

        Scene scene = new Scene(vBox);
        errorWindowStage.setScene(scene);
        errorWindowStage.showAndWait();
    }

    /***
     * Funktiota kutsutaan uutta kaupunkia haettaessa. Tarkistaa syötteen olemassaolon sekä yrittää hakea kaupungille
     * koordinaatit poikkeuskäsittelyrakenteessa. Kutsuu erillistä funktiota, mikäli kaupunkia ei löytynyt. Haun onnis-
     * tuessa hakee kaupungille tiedot.
     * @param inputName, syötetty kaupunki
     * @return, sään yksityiskohdat sisältävä olio
     * @throws Exception
     */
    private WeatherDetails setWeatherFetcher(String inputName) throws Exception
    {

        String data = "";
        String[] splitData = new String[0];
        WeatherDetails weatherDetailsDefault;

        if (cityName.isEmpty() && inputName.isEmpty())
        {
            cityName = "Tampere";
            weatherDetailsDefault = new WeatherDetails(cityName);
            data = weatherDetailsDefault.lookUpLocation(cityName);

            splitData = data.split(",");

            lon = Double.parseDouble(splitData[0]);
            lat = Double.parseDouble(splitData[1]);

            String dailyData = weatherDetailsDefault.getDailyWeather(lat, lon);
            String hourlyData = weatherDetailsDefault.getHourlyWeather(lat, lon);

            weatherDetails = weatherDetailsDefault;
            JsonParser jsonParser = new JsonParser();
            dailyWeather = jsonParser.readFromJsonsDaily(dailyData, lat, lon);

            hourlyWeather = jsonParser.readFromJsonsHourly(hourlyData, lat, lon);

            return weatherDetails;
        }

        WeatherDetails weatherDetailsSearch = new WeatherDetails(inputName);

        try
        {
            data = weatherDetailsSearch.lookUpLocation(inputName);
            splitData = data.split(",");
            compareLon = Double.parseDouble(splitData[0]);
            compareLat = Double.parseDouble(splitData[1]);
        }
        catch (Exception e)
        {
            errorWindow(inputName);
            wasCityFound = false;
            return weatherDetailsSearch;
        }

        cityName = inputName;
        wasCityFound = true;
        splitData = data.split(",");

        lon = Double.parseDouble(splitData[0]);
        lat = Double.parseDouble(splitData[1]);

        weatherDetails = weatherDetailsSearch;
        String dailyData = weatherDetails.getDailyWeather(lat, lon);

        String hourlyData = weatherDetails.getHourlyWeather(lat, lon);

        JsonParser jsonParser = new JsonParser();
        dailyWeather = jsonParser.readFromJsonsDaily(dailyData, lon, lat);
        hourlyWeather = jsonParser.readFromJsonsHourly(hourlyData, lon, lat);

        return weatherDetails;
    }

    /***
     * Tätä funktiota kutsutaan, kun käyttäjä on suorittanut haun onnistuneesti, ts. kaupunki on löytynyt ja data sille
     * haettu API-kutsulla. Tuon jälkeen tässä funktiossa avataan uusi ikkuna, jossa kysytään käyttäjältä haluaako tämä
     * lisätä juuri hakemansa kaupungin suosikkikaupunkeihinsa. Vastausta seurataan "lisää suosikkeihin" ja "ei"
     * -nappien tapahtumankäsittelijöillä. Mikäli klikataan lisää suosikkeihin, lisätään se suosikkilistalle. Muuten
     * suljetaan ikkuna. Ei ota vastaan parametria eikä palauta mitään.
     */
    private void addToFavourites()
    {
        HBox searchAndAddToFavouritesHBox = new HBox(20);
        Label askToAddFavouritesLabel = new Label("Do you want to add " + cityName + " to favourites?");
        askToAddFavouritesLabel.setStyle("-fx-font-family: SansSerif;");
        Stage favouritesWindow = new Stage();
        favouritesWindow.setTitle("Add to Favourites");
        Button addToFavouritesBtn = new Button("Add to Favourites");
        Button noBtn = new Button("No");

        // Asetetaan molempiin luotuihin nappeihin tapahtumakäsittelijät, jotta niiden klikkaukset voidaan tunnistaa
        // ja tehdä toimintoja niiden klikkauksen perusteella. Asetetaan kursori kädeksi, joka yleinen käytöntö
        // nappien päälle hiiren asetettaessa.
        addToFavouritesBtn.setOnAction((ActionEvent click) ->
        {
            favouriteCities.add(cityName);
            favouritesWindow.close();
        });

        // Kursori muuttuu, jos hiiri viedään päälle
        addToFavouritesBtn.setOnMouseEntered(mouseEvent ->
        {
            addToFavouritesBtn.setCursor(Cursor.HAND);
        });

        noBtn.setOnAction((ActionEvent click) ->
        {
            favouritesWindow.close();
        });

        // Kursori muuttuu, jos hiiri viedään päälle
        noBtn.setOnMouseEntered(mouseEvent ->
        {
            noBtn.setCursor(Cursor.HAND);
        });

        searchAndAddToFavouritesHBox.getChildren().addAll(askToAddFavouritesLabel, addToFavouritesBtn, noBtn);
        Scene searchScene = new Scene(searchAndAddToFavouritesHBox);
        favouritesWindow.setScene(searchScene);
        favouritesWindow.show();
    }

    /***
     * Suorittaa funktion setWeatherFetcher avulla haun, onko kaupunki olemassa vai onko syöte virheellinen, yritetäänkö
     * hakea kaupunkia, jonka tiedot on tällä hetkellä esillä tai, jos kaupunki on olemassa ja eroaa nykyisestä,
     * suorittaa käyttöliittymän päivittämisen.
     * @param cityToBeChecked paikkakunta, jonka käyttäjä syöttää, oikeellisuus tarkistetaan
     * @return boolean sen mukaan, onko paikkakunta olemassa tai haetaan kaupunkia, jonka tiedot tällä hetkellä esillä,
     * uudestaan
     */
    private Boolean getNewDataAndUpdateGUI(String cityToBeChecked)
    {
        try
        {
            String tempCity = cityName;
            WeatherDetails weatherDetail = setWeatherFetcher(cityToBeChecked);

            if ((compareLon != 0.0) && (compareLat != 0.0) && (!tempCity.equals(cityName)) && (wasCityFound))
            {
                compareLon = 0.0;
                compareLat = 0.0;
                setCurrentWeatherDetails(updateGUI);
                updateGuiComponents(updateGUI);
                history.add(cityToBeChecked);
                return true;
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * Käyttäjä voi hakea uusia paikkakuntia klikkaamalla nappia Search, syöttämällä paikkakunnan nimen ja painamalla
     * näppäintä ENTER.
     */
    private void searchAndAddToFavourites()
    {
        TextField searchField = new TextField();
        searchField.setPromptText("esim. Helsinki");
        Stage searchWindow = new Stage();
        Scene searchScene = new Scene(searchField);

        searchField.setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.ENTER)
            {
                String cityToBeChecked = searchField.getText();
                searchWindow.close();

                if (getNewDataAndUpdateGUI(cityToBeChecked))
                {
                    // Kysyy käyttäjältä lisätäänkö haettu paikkakunta suosikkeihin ja suorittaa toimintoja valinnan
                    // perusteella
                    addToFavourites();
                }
            }
        });

        searchWindow.setScene(searchScene);
        searchWindow.show();
    }

    /***
     * Päivittää käyttöliittymää joko ensimmäisen kerran ajettaessa tai uusia säätietoja hakiessa.
     * @param updateGUI käyttöliittymäolio
     * @throws Exception
     */
    private void updateGuiComponents(UpdateGUI updateGUI) throws Exception
    {
        HBox topHBox = new HBox();

        topHBox.setAlignment(Pos.CENTER);
        VBox topVBox = updateGUI.getTopVBox();

        HBox cityNameHBox = updateGUI.getCityNameHBox();
        topVBox.getChildren().addAll(cityNameHBox);

        HBox getTopHboxFromGUI = new HBox();

        // ChatGPT
        // |
        // |
        // v
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        // ^
        // |
        // |

        getTopHboxFromGUI = updateGUI.getTopHBox();
        topHBox.getChildren().addAll(getTopVBoxBtn(), leftSpacer, getTopHboxFromGUI, rightSpacer);

        topVBox.getChildren().add(topHBox);

        root.setTop(topVBox);

        VBox getCenterVBoxFromGUI = updateGUI.getCenterVBox();

        ScrollPane dailyScrollPane = updateGUI.getScrollPane();
        dailyScrollPane.setContent(updateGUI.getCenterBoxLabels());

        // ChatGPT
        // |
        // |
        // v
        getCenterVBoxFromGUI.setAlignment(Pos.CENTER);
        VBox.setVgrow(dailyScrollPane, Priority.ALWAYS);
        // ^
        // |
        // |

        getCenterVBoxFromGUI.getChildren().add(dailyScrollPane);

        root.setCenter(getCenterVBoxFromGUI);

        ScrollPane bottomScrollBarAndGridPane = updateGUI.getScrollPane();

        updateGUI.getHourlyDetailLists();

        GridPane getBottomGridPaneFromGUI = updateGUI.getBottomGridPane();
        bottomScrollBarAndGridPane.setContent(getBottomGridPaneFromGUI);

        VBox getBottomVBoxFromGUI = updateGUI.getBottomVBox();
        getBottomVBoxFromGUI.getChildren().add(bottomScrollBarAndGridPane);

        root.setBottom(getBottomVBoxFromGUI);

    }

    /***
     * Käy listan läpi riippuen parametrista whichList, joka tarkistaa, halutaanko käydä läpi historia vai suosikki-
     * paikkakunnat.
     * @param whichList sisältää tiedon, halutaanko käydä läpi historia vai suosikkipaikkakunnat
     * @return VBox citiesVBox ikkunassa näytetään kaupungit
     * @throws FileNotFoundException
     */
    private VBox showList(String whichList) throws FileNotFoundException
    {
        VBox citiesVBox = new VBox();
        Label titleLabel = new Label();
        ArrayList<String> listOfCities = new ArrayList<>();

        // Tyhjennetään varuiksi
        listOfCities.clear();

        // Jos whichList on favourite, käydään silloin läpi suosikkipaikkakunnat, muuten historia
        if (whichList.equals("favourite"))
        {
            titleLabel.setText("Favourites");

            listOfCities = new ArrayList<>(favouriteCities);
        }
        else
        {
            titleLabel.setText("History");
            listOfCities.addAll(history);
        }

        titleLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");
        citiesVBox.getChildren().add(titleLabel);

        String imagePath = "icons/misc/delete.png";
        Image trashIcon = new Image(new FileInputStream(imagePath));

        for (int i = listOfCities.size() - 1; i >= 0; i--)
        {
            String city = listOfCities.get(i);

            ImageView trashImageView = new ImageView();
            trashImageView.setImage(trashIcon);
            trashImageView.setFitWidth(20);
            trashImageView.setFitHeight(20);
            trashImageView.setPreserveRatio(true);

            BorderPane labelAndImageBorderPane = new BorderPane();

            Label currentCityLabel = new Label();

            currentCityLabel.setText(city);

            // Kursori ja tekstin väri muuttuvat, jos hiiri viedään päälle
            currentCityLabel.setOnMouseEntered(mouseEvent ->
            {
                currentCityLabel.setTextFill((valueOf("#0000EE")));
                currentCityLabel.setCursor(Cursor.HAND);
            });

            // Tekstin väri palaa mustaksi
            currentCityLabel.setOnMouseExited(mouseEvent ->
            {
                currentCityLabel.setTextFill((valueOf("#000000")));
            });

            currentCityLabel.setOnMouseClicked(mouseEvent ->
            {
                citiesStage.close();
                String cityToBeChecked = currentCityLabel.getText();

                // Haetaan uusi data
                getNewDataAndUpdateGUI(cityToBeChecked);

            });

            // Kursori muuttuu, jos hiiri viedään päälle
            trashImageView.setOnMouseEntered(mouseEvent ->
            {
                trashImageView.setCursor(Cursor.HAND);
            });

            ArrayList<String> finalListOfCities = listOfCities;

            // Poistaa sen kaupungin, minkä roskakoria on klikattu, if tarkistaa poistetaan historiasta vai suosikeista
            trashImageView.setOnMouseClicked(mouseEvent ->
            {
                finalListOfCities.remove(city);

                if (whichList.equals("favourite"))
                {
                    favouriteCities.remove(city);
                }
                else
                {
                    history.remove(city);
                }

                // Kursori ja tekstin väri muuttuvat, jos hiiri viedään päälle
                currentCityLabel.setOnMouseEntered(mouse ->
                {
                    currentCityLabel.setTextFill(Color.LIGHTGRAY);
                    currentCityLabel.setCursor(Cursor.DEFAULT);
                });

                // Tekstin väri muuttuu harmaaksi poiston jälkeen
                currentCityLabel.setOnMouseExited(mouse ->
                {
                    currentCityLabel.setTextFill(Color.LIGHTGRAY);
                });

                // Poistettua paikkakuntaa ei voi enää klikata
                currentCityLabel.setOnMouseClicked(null);

                // Poistettua paikkakuntaa ei voi poistaa "uudestaan"
                trashImageView.setOnMouseEntered(mouse ->
                {
                    trashImageView.setCursor(Cursor.DEFAULT);
                });

                currentCityLabel.setTextFill(Color.LIGHTGRAY);
                currentCityLabel.setText(city);
                currentCityLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 20; ");

            });

            currentCityLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 20");
            // ChatGPT
            // |
            // |
            // v
            labelAndImageBorderPane.setLeft(currentCityLabel);
            labelAndImageBorderPane.setRight(trashImageView);
            // ^
            // |
            // |
            citiesVBox.getChildren().add(labelAndImageBorderPane);
        }

        Button emptyBtn = new Button("Empty");

        // Kursori muuttuu, jos hiiri viedään päälle
        emptyBtn.setOnMouseEntered(mouseEvent ->
        {
            emptyBtn.setCursor(Cursor.HAND);
        });

        ArrayList<String> finalListOfCities1 = listOfCities;
        // Tyhjentää historian/suosikit riippuen muuttujasta whichList
        emptyBtn.setOnAction((ActionEvent click) ->
        {
            finalListOfCities1.clear();

            if (whichList.equals("favourite"))
            {
                favouriteCities.clear();
            }
            else
            {
                history.clear();
            }

            // Klikattaessa nappia Empty, poistetaan koko BorderPane
            citiesVBox.getChildren().removeIf(node -> node instanceof BorderPane);
        });

        citiesVBox.getChildren().add(emptyBtn);

        return citiesVBox;
    }

    /***
     * Avaa ikkunan, jossa näytetään suosikkipaikkakunnat. Jos suosikkipaikkakuntia ei ole, ilmoitetaan siitä.
     * @throws FileNotFoundException
     */
    private void getFavourites() throws FileNotFoundException
    {
        Stage favouritesWindow = new Stage();
        citiesStage = favouritesWindow;
        String favourite = "favourite";

        VBox favouritesVBox = new VBox();

        if (favouriteCities.isEmpty())
        {
            Button exitBtn = new Button("Exit");

            // Kursori muuttuu, jos hiiri viedään päälle
            exitBtn.setOnMouseEntered(mouseEvent ->
            {
                exitBtn.setCursor(Cursor.HAND);
            });

            exitBtn.setOnMouseClicked((click) ->
            {
                citiesStage.close();
            });
            Label emptyFavouriteCityLabel = new Label("Error: You have not added any cities to favourites");
            emptyFavouriteCityLabel.setStyle("-fx-font-family: 'SansSerif';");
            favouritesVBox.getChildren().addAll(emptyFavouriteCityLabel, exitBtn);
            favouritesVBox.setAlignment(Pos.CENTER);

        }

        else
        {
            // Näyttää suosikkipaikkakunnat
            favouritesVBox = showList(favourite);
        }


        Scene favouriteScene = new Scene(favouritesVBox);

        favouritesWindow.setScene(favouriteScene);

        favouritesWindow.show();
    }

    /***
     * Funktio käsittelee sitä, kun käyttöliittymän Maps-nappia on painettu. Ilmoittaa käyttäjälle, että kartat aukeavat
     * käyttöjärjestelmän oletusselaimeen. Tähän ok:ta klikaessa yritetään avata tuota, ja mikäli se epäonnistuu, kaapa-
     * taan joko IO-operaatiosta tai virheellisestä osoitteesta johtuva poikkeus. Muuten suljetaan ikkuna. Ei ota para-
     * metrejä eikä palauta arvoja.
     */
    public void mapWindowManager()
    {
        try
        {
            Stage primaryStage = new Stage();

            Label infoTextLabel = new Label("The maps will open in your OS's default browser.");
            Button okButton = new Button("Ok");
            // Kursori muuttuu, jos hiiri viedään päälle
            okButton.setOnMouseEntered(mouseEvent ->
            {
                okButton.setCursor(Cursor.HAND);
            });

            Button cancelButton = new Button("Cancel");

            // Kursori muuttuu, jos hiiri viedään päälle
            cancelButton.setOnMouseEntered(mouseEvent ->
            {
                cancelButton.setCursor(Cursor.HAND);
            });

            infoTextLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 20");

            VBox vbox = new VBox(10);
            vbox.setAlignment(Pos.CENTER);
            HBox buttonsHBox = new HBox(10);
            buttonsHBox.getChildren().addAll(okButton, cancelButton);
            buttonsHBox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(infoTextLabel, buttonsHBox);
            Scene scene = new Scene(vbox, 500, 100);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Maps");

            okButton.setOnMouseClicked(mouseEvent ->
            {
                primaryStage.close();
                String url = "https://openweathermap.org/weathermap";
                try
                {
                    Desktop.getDesktop().browse(new URI(url));
                }
                catch (IOException | URISyntaxException e)
                {
                    throw new RuntimeException(e);
                }

            });

            cancelButton.setOnMouseClicked(mouseEvent ->
            {
                primaryStage.close();
            });

            primaryStage.show();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * Luo uuden ikkunan, jossa historia näytetään. Jos historia on tyhjä, siitä ilmoitetaan.
     * @throws FileNotFoundException
     */
    private void getHistory() throws FileNotFoundException
    {
        Stage historyWindow = new Stage();

        // Käyttää aiemmin luotoa stagea hyödyksi
        citiesStage = historyWindow;
        String historyText = "history";

        VBox historyVBox = new VBox();

        // Jos historia on tyhjä, siitä ilmoitetaan
        if (history.isEmpty())
        {
            Button exitBtn = new Button("Exit");

            // Kursori muuttuu, jos hiiri viedään päälle
            exitBtn.setOnMouseEntered(mouseEvent ->
            {
                exitBtn.setCursor(Cursor.HAND);
            });

            Label emptyHistoryLabel = new Label("History is empty");
            emptyHistoryLabel.setStyle("-fx-font-family: 'SansSerif'");

            // Kursori muuttuu, jos hiiri viedään päälle
            exitBtn.setOnMouseClicked((click) ->
            {
                citiesStage.close();
            });
            historyVBox.getChildren().addAll(emptyHistoryLabel, exitBtn);
            historyVBox.setAlignment(Pos.CENTER);
        }
        else
        {
            // Näyttää historian
            historyVBox = showList(historyText);
        }

        Scene historyScene = new Scene(historyVBox);
        historyWindow.setScene(historyScene);
        historyWindow.show();
    }

    /***
     * Luo VBoxin, joka sisältää napit History, Maps, Imperial, Search ja Favourites. Jokaisen napin kohdalla hiiren
     * kursori muuttuu HAND-tyyppiseksi, joka ilmaisee, että painiketta on mahdollista klikata.
     * @return VBox topVBox, joka lisätään käyttöliittymään
     */
    public VBox getTopVBoxBtn()
    {
        VBox topVBox = new VBox(10);

        Button historyBtn = new Button("History");

        // Kursori muuttuu, jos hiiri viedään päälle
        historyBtn.setOnMouseEntered(mouseEvent ->
        {
            historyBtn.setCursor(Cursor.HAND);
        });
        historyBtn.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");
        topVBox.getChildren().add(historyBtn);

        historyBtn.setOnMouseClicked(mouseEvent ->
        {
            try
            {
                // Näyttää historian
                getHistory();
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }

        });

        Button mapsBtn = new Button("Maps");
        mapsBtn.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");

        // Kursori muuttuu, jos hiiri viedään päälle
        mapsBtn.setOnMouseEntered(mouseEvent ->
        {
            mapsBtn.setCursor(Cursor.HAND);
        });
        topVBox.getChildren().add(mapsBtn);

        mapsBtn.setOnAction((ActionEvent click) ->
        {
            // Näyttää kartan
            mapWindowManager();
        });

        Button imperialBtn = new Button("Imperial");
        imperialBtn.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");

        // Kursori muuttuu, jos hiiri viedään päälle
        imperialBtn.setOnMouseEntered(mouseEvent ->
        {
            imperialBtn.setCursor(Cursor.HAND);
        });

        topVBox.getChildren().add(imperialBtn);

        // Kursori muuttuu, jos hiiri viedään päälle
        imperialBtn.setOnMouseEntered(mouseEvent ->
        {
            imperialBtn.setCursor(Cursor.HAND);
        });

        imperialBtn.setOnAction((ActionEvent event) ->
        {
            try
            {
                // Päivittää käyttöliittymän tiedot metrics/imperial välillä
                updateGUI.changeImperialMetricsUnits(imperialBtn);
                updateGUI.changeDailyImperialMetricsUnit();
                updateGUI.changeHourlyImperialMetricsUnit();

            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");
        topVBox.getChildren().add(searchBtn);

        // Kursori muuttuu, jos hiiri viedään päälle
        searchBtn.setOnMouseEntered(mouseEvent ->
        {
            searchBtn.setCursor(Cursor.HAND);
        });
        searchBtn.setOnAction((ActionEvent event) ->
        {
            // Hakee paikkakunnan ja käyttäjän halutessa lisää sen suosikkeihin
            searchAndAddToFavourites();
        });

        Button favouritesBtn = new Button("Favourites");
        favouritesBtn.setStyle("-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 15");

        // Kursori muuttuu, jos hiiri viedään päälle
        favouritesBtn.setOnMouseEntered(mouseEvent ->
        {
            favouritesBtn.setCursor(Cursor.HAND);
        });
        topVBox.getChildren().add(favouritesBtn);

        favouritesBtn.setOnAction((ActionEvent event) ->
        {
            try
            {
                // Näyttää suosikkipaikkakunnat
                getFavourites();
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        });

        return topVBox;
    }
}
