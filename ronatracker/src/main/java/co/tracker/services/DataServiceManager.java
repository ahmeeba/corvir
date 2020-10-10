package co.tracker.services;


import co.tracker.model.LocationData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

//This class is for grabbing covid data from the github repo,

@Service
public class DataServiceManager {

    //Grabbing the covid data github url and putting it into a variable for usage.
    private static String VIRUS_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    //Creating a list of the locations of covid hotspots from LocationData class
    private List<LocationData> allStats = new ArrayList<>();

    //getter for the List of locations
    public List<LocationData> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException { //method that gets covid data
        List<LocationData> newStats = new ArrayList<>(); //used for updating the allstats list when the data is updated,
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_URL)) //converting string to uri
                .build();
        //HttpResponse is getting data(response) from the github, turning into a string(csv)
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        //code from apache doc, parsing csv, grabbing certain values from the header from csv
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {  //looping through grabbing states, countries from data
            LocationData locationStat = new LocationData(); //LocationData Object
            locationStat.setState(record.get("Province/State")); //getting state header for LocationData class List
            locationStat.setCountry(record.get("Country/Region")); //getting country header for LocationData class List
            int latestCases = Integer.parseInt(record.get(record.size() - 1)); //Grabbing covid case numbers for List
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2)); //grabbing stats from prev day
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases); //calculating the difference in total numbers from current and prev day
            newStats.add(locationStat); //adding data to newstats list for updating the allstats list
        }
        this.allStats = newStats; //updating allstats list with newstats. doing it with two lists so people don't receive an error while the allstats list updates its numbers
    }

}

//PostCurrent - When an instance is constructed, run whatever this annotation is attached to(execute method)
//Scheduled - Schedules the run of a method on a fixed basis(how often it needs to run)
//Service - marks the class as a spring service
