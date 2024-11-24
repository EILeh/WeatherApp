package fi.tuni.prog3.weatherapp;

import java.util.LinkedList;
import java.util.List;
import java.util.LinkedHashSet;

/**
 * Rajapinta, joka sisältää metodit tiedosta luku ja tiedostoon kirjoittaminen.
 */
public interface iReadAndWriteToFile
{

    /***
     * Lukee JSON annetuista tiedostoista annettuihin tietorakenteisiin.
     * @param fileNameFavourites tiedoston nimi, joka sisältää suosikkipaikkakunnat
     * @param FilenameHistory tiedoston nimi, joka sisältää hakuhistorian
     * @param setOfCities set, johon luetaan tiedoston sisältö
     * @param history list, johon luetaan tiedoston sisältö
     * @return totuusarvo sen mukaan, onnistuttiin tiedostoon lukea tiedot
     * @throws Exception
     */
    public boolean readFromFile(String fileNameFavourites, String FilenameHistory, LinkedHashSet<String> setOfCities,
                                LinkedList<String> history) throws Exception;

    /***
     * Kirjoittaa historia- ja suosikkikaupunkeja tiedostoon. Suosikkeihin halutaan vain uniikkeja kaupunkeja, minkä
     * vuoksi niiden kanssa käytetään settiä. Historiaan halutaan jokainen haku, myös duplikaatit, joten käytetään
     * listaa.
     * @param fileNameFavourites, suosikkien tiedostonimi
     * @param FilenameHistory, historian tiedostonimi
     * @param setOfCities, luetaan kaupungit setistä tiedostoon
     * @param history, luetaan kaupungit listasta historiaan
     * @return
     * @throws Exception
     */
    public boolean writeToFile(String fileNameFavourites, String FilenameHistory, LinkedHashSet<String> setOfCities,
                               LinkedList<String> history) throws Exception;


    /**
     * Kirjoittaa historia- ja suosikkikaupunkeja tiedostoon.
     * @param fileName name of the file to write to.
     * @return true if the write was successful, otherwise false.
     * @throws Exception if the method e.g., cannot write to a file.
     */

}
