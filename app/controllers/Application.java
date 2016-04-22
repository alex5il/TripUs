package controllers;

import com.mongodb.*;
//import data.Trip;
import data.User;
import org.jongo.MongoCollection;
import play.*;
import play.mvc.*;
import uk.co.panaxiom.playjongo.PlayJongo;
import views.html.*;

public class Application extends Controller {
    public Result index() {
        //System.out.println(Trip.trips().find().as(Trip.class).next());
        //System.out.println(Trip.findByTripLeader("Anton"));

        return ok();
    }

}

