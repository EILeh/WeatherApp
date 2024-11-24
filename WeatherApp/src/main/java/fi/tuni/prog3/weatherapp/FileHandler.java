package fi.tuni.prog3.weatherapp;
import java.io.*;
import java.util.LinkedList;
import java.util.LinkedHashSet;

public class FileHandler implements iReadAndWriteToFile
{

    @Override
    public boolean readFromFile(String fileNameFavourites, String FilenameHistory, LinkedHashSet<String> setOfCities,
                                LinkedList<String> history) throws Exception
    {
        try
        {
            FileReader fr = new FileReader(fileNameFavourites + ".json");
            FileReader frHistory = new FileReader(FilenameHistory + ".json");

            BufferedReader br = new BufferedReader(fr);
            BufferedReader brHistory = new BufferedReader(frHistory);

            String line;
            while ((line = br.readLine()) != null)
            {
                setOfCities.add(line);
            }

            while ((line = brHistory.readLine()) != null)
            {
                history.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean writeToFile(String fileNameFavourites, String FilenameHistory, LinkedHashSet<String> setOfCities,
                               LinkedList<String> history)
    {
        FileWriter fwFavourites;
        FileWriter fwHistory;
        BufferedWriter bwFavourites = null;
        BufferedWriter bwHistory = null;
        try
        {
            // Argumentti true asettaa kirjoitusmoodiksi append, false asettaisi write
            fwFavourites = new FileWriter(fileNameFavourites + ".json", false);
            bwFavourites = new BufferedWriter(fwFavourites);
            for (String city : setOfCities)
            {
                bwFavourites.write(city);
                bwFavourites.newLine();
            }

            bwFavourites.close();
            fwFavourites.close();

            fwHistory = new FileWriter(FilenameHistory + ".json", false);
            bwHistory = new BufferedWriter(fwHistory);
            for (String city : history)
            {
                bwHistory.write(city);
                bwHistory.newLine();
            }
            bwHistory.close();
            fwHistory.close();
        }

        catch (Exception e)
        {
            return false;
        }
        return true;
    }

}
