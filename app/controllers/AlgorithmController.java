package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class AlgorithmController extends Controller {
    public Result index() {
        //System.out.println(Trip.trips().find().as(Trip.class).next());
        System.out.println("anton !!! ");
        return ok();
    }

}

