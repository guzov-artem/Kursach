package spring.service_3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public static HttpStatus runApp(@RequestParam(value = "size") int size) throws InterruptedException, IOException,
            RuntimeException {
        if (size <= 0) {
            throw new RuntimeException("Size must be more than zero!");
        }
        RestTemplate restTemplate = new RestTemplate();
        String urlTimeTable = "http://localhost:8002/timetable?size=" + size;
        ResponseEntity<String> response1 = restTemplate.getForEntity(urlTimeTable, String.class);
        if (response1.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Exception in second service: " + response1.getBody().toString());
        }

        ArrayList<Ship>[] ships = parseShips(response1.getBody());
        Calendar start = new GregorianCalendar(2021, Calendar.MARCH, 0, 0, 0, 0);
        Calendar end = new GregorianCalendar(2021, Calendar.APRIL, 0, 0, 0, 0);
        PortSimulator portSimulator = new PortSimulator(ships, start, end);
        portSimulator.findBestStatistic();
        Statistic stat = portSimulator.getStatistic();

        String urlStatistic = "http://localhost:8002/statistic";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(stat), headers);
        ResponseEntity<String> response2 = restTemplate.postForEntity(urlStatistic, request, String.class);

        if (response2.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Exception in second service: " + response1.getBody());
        }
        return HttpStatus.OK;
    }

    static public ArrayList<Ship>[] parseShips(String string) throws IOException, JsonSyntaxException, JsonIOException {
        try (Reader reader = new StringReader(string)) {
            Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
            Type SHIP_TYPE = new TypeToken<ArrayList<Ship>[]>() {
            }.getType();
            ArrayList<Ship>[] ships = gson.fromJson(reader, SHIP_TYPE);
            return ships;
        }
    }
}
