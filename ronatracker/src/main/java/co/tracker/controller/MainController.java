package co.tracker.controller;


import co.tracker.model.LocationData;
import org.springframework.stereotype.Component;
import co.tracker.services.DataServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    DataServiceManager virusDataService;

    @GetMapping("/") //mapping to root url
    public String home(Model model) {
        List<LocationData> allData = virusDataService.getAllStats(); //creating a list of data from DataServiceManager class
        int totalReportedCases = allData.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum(); //using lambda
        int totalNewCases = allData.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum(); //getting the int value, summing up for total cases
        //Attaching java data numbers to the html attributes ui
        model.addAttribute("locationData", allData);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home"; //mapping to html file
    }
}

//Autowired -
//GetMapping -
//Controller - Makes class a spring co.tracker.controller
