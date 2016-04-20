package data;

import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;
import play.*;

/**
 * Created by sergio on 08/04/2016.
 */
public class Trip {
    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public static MongoCollection trips() {
        return jongo.getCollection("trips");
    }

    // auto
    @MongoObjectId
    private String _id;

    private String tripLeader, tripName, key;
    private User[] users;
    private Pick[] picks;

    public Trip() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTripLeader() {
        return tripLeader;
    }

    public void setTripLeader(String tripLeader) {
        this.tripLeader = tripLeader;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Pick[] getPicks() {
        return picks;
    }

    public void setPicks(Pick[] picks) {
        this.picks = picks;
    }

    public void insert() {
        trips().save(this);
    }

    public void remove() {
        trips().remove(this._id);
    }

    public static Trip findByKey(String key) {
        return trips().findOne("{key: #}", key).as(Trip.class);
    }

    public static Trip findByTripLeader(String tripLeader) {
        return trips().findOne("{tripLeader: #}", tripLeader).as(Trip.class);
    }
}
