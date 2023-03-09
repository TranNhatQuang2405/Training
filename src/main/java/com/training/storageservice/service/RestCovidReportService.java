package com.training.storageservice.service;

import com.training.storageservice.entity.CovidReport;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RestCovidReportService {

    @ConfigProperty(name = "covid-report.url")
    String baseUrl;

    public List<CovidReport> getByDate(String date)  {
        List<CovidReport> covidReports = new ArrayList<>();
        try {
            String realUrl = String.format(baseUrl, date);
            URL url = new URL(realUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            //Remove first line
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null){
                CovidReport covidReport = CovidReport.convertFromRawData(line);
                covidReports.add(covidReport);
            }
        }catch (Exception exception){
            Log.error(exception.getMessage());
        }
        return filterDuplicateData(covidReports);
    }

    private List<CovidReport> filterDuplicateData(List<CovidReport> covidReports){
        return covidReports.stream()
                .filter(covidReport -> covidReport.checkDontHaveDetail())
                .collect(Collectors.toList());
    }
}
