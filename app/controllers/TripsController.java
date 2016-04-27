package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import data.Trip;
import data.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
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

    public Result joinTrip() {
        final JsonNode values = request().body().asJson();
        String tripGroupKey = values.get("groupKey").asText();
        Trip tripForDb = Trip.findByKey(tripGroupKey);

        if (tripForDb == null) {
            return badRequest("Wrong Trip key");
        }

        String userName = values.get("userName").asText();
        User user = new User(userName);
        ArrayList<User> users = new ArrayList<User>();
        Integer oldNumOfUsers = 0;
        if (tripForDb.getUsers() != null)
            oldNumOfUsers = tripForDb.getUsers().length;
        User[] usersNew = new User[oldNumOfUsers + 1];
        boolean userIsInTrip = false;
        for (int i = 0; i < oldNumOfUsers; i++) {
            if (tripForDb.getUsers()[i].getName().equals(userName)) {
                System.out.println(userName + " Will login to Trip : " + tripForDb.getTripName());
                return ok(String.valueOf("Your user is already in the trip"));
            }
            usersNew[i] = tripForDb.getUsers()[i];
        }
        usersNew[oldNumOfUsers] = user;
        tripForDb.setUsers(usersNew);
        tripForDb.insert();

        System.out.println(userName + " Will join to Trip : " + tripForDb.getTripName());


        return created(String.valueOf("Your user added to the trip seccsesfuly"));
    }
}
