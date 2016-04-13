package controllers;

import com.mongodb.*;
import play.*;
import play.mvc.*;
import uk.co.panaxiom.playjongo.PlayJongo;
import views.html.*;

public class Application extends Controller {

        public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public Result index() {
		jongo.getCollection("users");
        return ok();
    }

}
