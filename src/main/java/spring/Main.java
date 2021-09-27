package spring;

import org.springframework.boot.SpringApplication;
import spring.service_1.Writer;
import spring.service_2.SecondService;
import spring.service_3.ThirdService;

import java.io.IOException;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication app1 = new SpringApplication(Writer.class);
        SpringApplication app2 = new SpringApplication(SecondService.class);
        SpringApplication app3 = new SpringApplication(ThirdService.class);
        app1.setDefaultProperties(Collections
                .singletonMap("server.port", "8001"));
        app2.setDefaultProperties(Collections
                .singletonMap("server.port", "8002"));
        app3.setDefaultProperties(Collections
                .singletonMap("server.port", "8003"));
        app1.run();
        app2.run();
        app3.run();
    }
}
