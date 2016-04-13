package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.co.panaxiom.playjongo.PlayJongo;
import play.*;
import org.jongo.*;

/**
 * Created by sergio on 08/04/2016.
 */
public class trip {

    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public static MongoCollection countries() {
        return jongo.getCollection("countries");
    }

    @JsonProperty("_id")
    public String id;

    public String name;

    public void insert() {
        countries().save(this);
    }

    public void remove() {
        countries().remove(this.id);
    }

    public static trip findByName(String name) {
        return countries().findOne("{name: #}", name).as(trip.class);
    }

    public static trip findByID(String id) {
        return countries().findOne("{_id: #}", id).as(trip.class);
    }
}
