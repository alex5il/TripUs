package controllers;

import data.Amenity;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by sergio on 20/04/2016.
 */
public class Amenities extends Controller {
    public Result getAllAmenities() {
        //System.out.println(Trip.trips().find().as(Trip.class).next());
        System.out.println(Amenity.amenities());
        return ok("withoutFilter");
    }

    public Result getAllAmenitiesWithFilter() {
        //System.out.println(Trip.trips().find().as(Trip.class).next());
        System.out.println(Amenity.amenitiesThatContainsString("asda"));
        return ok("withFilter");
    }
}
