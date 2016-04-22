package controllers;

import data.Amenity;
import org.jongo.MongoCursor;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

/**
 * Created by sergio on 20/04/2016.
 */
public class AmenitiesController extends Controller {
    public Result getAllAmenities() {
        MongoCursor<Amenity> result = Amenity.amenities();
        ArrayList<String> stringsToReturn = new ArrayList<String>();
        while (result.hasNext()) {
            stringsToReturn.add("\"" + result.next().getDisplayName() + "\"");
        }
        return ok(stringsToReturn.toString());
    }

    public Result getAllAmenitiesWithFilter(String word) {
        MongoCursor<Amenity> result = Amenity.amenitiesThatContainsString(word);
        ArrayList<String> stringsToReturn = new ArrayList<String>();
        while (result.hasNext()) {
            stringsToReturn.add("\"" + result.next().getDisplayName() + "\"");
        }
        return ok(stringsToReturn.toString());
    }
}
