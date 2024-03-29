package spring.service_2;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import spring.Ship;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class SecondService {
    @GetMapping(value = "/timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String getTimetable(@RequestParam(value = "size") int size) throws IOException,
            RestClientException, JsonSyntaxException, IllegalStateException, SecurityException,
            NullPointerException, IllegalArgumentException {
        if (size <= 0) {
            throw new RuntimeException("Size must be more than zero!");
        }
        Gson gsonOne = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder.build();
        String schedule = restTemplate.getForObject("http://localhost:8001/list?size=" + size, String.class);
        JsonArray object = JsonParser.parseString(schedule).getAsJsonArray();
        Type SHIP_TYPE = new TypeToken<ArrayList<Ship>[]>() {}.getType();
        ArrayList<Ship>[] ships = gsonOne.fromJson(object, SHIP_TYPE);
        writeToJson(ships);
        return gsonOne.toJson(ships);
    }

    @GetMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String getFile(@RequestParam(value = "file") String fileName) throws IOException,
            JsonIOException, JsonSyntaxException {
        try(JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir")
                + "/secondServiceDirectory/" + fileName))) {
            Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
            Type SHIP_TYPE = new TypeToken<ArrayList<Ship>[]>() {
            }.getType();
            ArrayList<Ship>[] ships = gson.fromJson(reader, SHIP_TYPE);
            reader.close();
            return gson.toJson(ships);
        }
    }
    @PostMapping(value = "/statistic", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    HttpStatus postResults(@RequestBody String statiststicString) throws IOException {
        try (FileWriter fileWriter = new FileWriter("secondServiceDirectory/statistic.json")) {
            fileWriter.write(statiststicString);
            fileWriter.close();
            return HttpStatus.OK;
        }
    }
    static private void writeToJson( ArrayList<Ship>[] ships) throws IOException, SecurityException,
            NullPointerException, IllegalArgumentException {
        try(JsonWriter writer = new JsonWriter(new FileWriter(System.getProperty("user.dir")
                + "/secondServiceDirectory/timetable.json"))) {
            Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
            gson.toJson(ships, ships.getClass(), writer);
        }
    }
}
