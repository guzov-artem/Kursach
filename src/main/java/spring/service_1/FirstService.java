package spring.service_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
@RestController
public class FirstService {
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String writeToJson(@RequestParam(value = "size") int size) throws IOException, JsonIOException {
        TimeTable timeTable = new TimeTable(size);
        timeTable.generateShips();
        try(JsonWriter writer = new JsonWriter(new FileWriter("firstServiceDirectory/table.json")))
        {
            Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
            gson.toJson(timeTable.getShipArrayList(), timeTable.getShipArrayList().getClass(), writer);
            writer.close();
            return gson.toJson(timeTable.getShipArrayList());
        }
    }

}
