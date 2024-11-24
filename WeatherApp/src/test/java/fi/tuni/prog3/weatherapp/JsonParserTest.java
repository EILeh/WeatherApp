/*Täysin COPILOTin tekemä. */

package fi.tuni.prog3.weatherapp;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testiluokka JsonParserille.
 */
public class JsonParserTest {

    /**
     * Testaa readFromJsonsHourly-metodia.
     */
    @Test
    public void testReadFromJsonsHourly() {
        // Valmistelu: Aseta testidata
        String data = "{\"hourly\": [{\"time\": 1622548800, \"temperature\": 25.5}, ...]}";
        double lat = 60.1695;
        double lon = 24.9354;
        JsonParser instance = new JsonParser();

        // Toiminta: Kutsu testattavaa metodia
        List<Weather> result = instance.readFromJsonsHourly(data, lat, lon);

        // Tarkistus: Varmista tulos
        assertNotNull(result, "Tuloksen ei pitäisi olla null");
        assertEquals(24, result.size(), "Odotettiin 24 tunnin sääennustetta");
    }

    /**
     * Testaa readFromJsonsDaily-metodia.
     */
    @Test
    public void testReadFromJsonsDaily() {
        // Valmistelu: Aseta testidata
        String data = "{\"daily\": [{\"time\": 1622548800, \"maxTemp\": 28.0, \"minTemp\": 18.0}, ...]}";
        double lat = 60.1695;
        double lon = 24.9354;
        JsonParser instance = new JsonParser();

        // Toiminta: Kutsu testattavaa metodia
        List<Weather> result = instance.readFromJsonsDaily(data, lat, lon);

        // Tarkistus: Varmista tulos
        assertNotNull(result, "Tuloksen ei pitäisi olla null");
        assertEquals(7, result.size(), "Odotettiin 7 päivittäistä sääennustetta");
        // Lisää tarkempia tarkistuksia todellisen datarakenteen perusteella
    }
}