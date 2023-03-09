package com.training.storageservice.resource;

import com.training.storageservice.entity.CovidReport;
import com.training.storageservice.service.RestCovidReportService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("test")
public class TestResource {
    @Inject
    RestCovidReportService restCovidReportService;

    @GET
    public List<CovidReport> getData(@QueryParam("date") String dateString) {
        return restCovidReportService.getByDate(dateString);
    }

}
