package controllers;

import play.*;
import play.mvc.*;

import views.html.*;


import com.mongodb.*;
import uk.co.panaxiom.playjongo.PlayJongo;

public class Application extends Controller {
	
	public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public Result index() {
		jongo.getCollection("users");
        return ok(index.render("Your new application is ready."));
    }

}
