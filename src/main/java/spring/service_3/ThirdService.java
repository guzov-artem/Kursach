package spring.service_3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import spring.Ship;
import spring.Statistic;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootApplication
@RestController
public class ThirdService {
    @GetMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
    public static HttpStatus runApp() throws InterruptedException, IOException, RestClientException,
            JsonSyntaxException, JsonIOException {
        RestTemplate restTemplate = new RestTemplate();
        String urlTimeTable = "http://localhost:8002/timetable?size=300";
        ResponseEntity<String> response = restTemplate.getForEntity(urlTimeTable, String.class);

        ArrayList<Ship>[] ships = parseShips(response.getBody());
        Calendar start = new GregorianCalendar(2021, 3, 0, 0, 0, 0);
        Calendar end = new GregorianCalendar(2021, 4, 0, 0, 0, 0);
        PortSimulator portSimulator = new PortSimulator(ships, start, end);
        portSimulator.findBestStatistic();
        Statistic stat = portSimulator.getStatistic();

        String urlStatistic = "http://localhost:8002/statistic";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(stat), headers);
        restTemplate.postForEntity(urlStatistic, request, String.class);
        return HttpStatus.OK;
    }
    static public ArrayList<Ship>[] parseShips(String string) throws IOException, JsonSyntaxException, JsonIOException {
        Reader reader = new StringReader(string);
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
        Type SHIP_TYPE = new TypeToken<ArrayList<Ship>[]>() {}.getType();
        ArrayList<Ship>[] ships = gson.fromJson(reader, SHIP_TYPE);
        reader.close();
        return ships;
    }
}
