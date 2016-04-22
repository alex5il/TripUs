package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import data.Trip;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;
import java.util.Random;

/**
 * Created by sergio on 20/04/2016.
 */
public class TripsController extends Controller {
    public Result createTrip() {
        final JsonNode values = request().body().asJson();
        String tripGroupName = values.asText("name");
        System.out.println("New trip created : " + tripGroupName);
        Random rn = new Random();
        int groupKey = rn.nextInt(1000) + 1;

        Trip newTripForDb = new Trip();
        newTripForDb.setTripName(tripGroupName);
        newTripForDb.setKey(String.valueOf(groupKey));
        newTripForDb.insert();

        return ok(String.valueOf(groupKey));
    }
}
