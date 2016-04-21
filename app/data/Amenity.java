package data;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.RawResultHandler;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

public class Amenity {
    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);
    // auto
    @MongoObjectId
    private String _id;
    private String name;
    private String displayName;

    public Amenity() {
    }

    public static MongoCollection ameneties() {
        return PlayJongo.getCollection("amenities");
    }

    public static MongoCursor<Amenity> amenitiesThatContainsString(String stringToFind) {
        return ameneties().find("{'name': {$regex: /s/}}").as(Amenity.class);
    }

    public static MongoCursor<Amenity> amenities() {
        return ameneties().find().as(Amenity.class);
    }

    public static Object amenitiesJSON() {
        return ameneties().find().map(new RawResultHandler());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void insert() {
        ameneties().save(this);
    }

    public void remove() {
        ameneties().remove(this._id);
    }
}
