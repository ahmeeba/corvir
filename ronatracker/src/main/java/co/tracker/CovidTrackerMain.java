package co.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidTrackerMain {
    public static void main(String[] args) {
        SpringApplication.run(CovidTrackerMain.class, args); //starting spring framework/ runs server

    }

}
//EnableScheduling - Enabling the use of the scheduled annotation in DataService class
//SpringBootApplication - enabling configuration, component scanning, etc.