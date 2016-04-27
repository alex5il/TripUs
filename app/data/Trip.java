package data;

import org.jongo.MongoCollection;
import org.jongo.RawResultHandler;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

public class Trip {
    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);
    // auto
    @MongoObjectId
    private String _id;
    private String tripLeader, tripName, key;
    private User[] users;
    private Point[] point;

    public Trip() {
    }

    public static MongoCollection trips() {
        return PlayJongo.getCollection("trips");
    }

    public static Object tripJSON() {
        return trips().find().map(new RawResultHandler());
    }

    public static Trip findByKey(String key) {

        return trips().findOne("{key: #}", key).as(Trip.class);
    }

    public static Trip findByTripLeader(String tripLeader) {
        return trips().findOne("{tripLeader: #}", tripLeader).as(Trip.class);
    }

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

    public Point[] getPoinst() {
        return point;
    }

    public void setPoints(Point[] picks) {
        this.point = picks;
    }

    public String getTripPoints() {
        StringBuilder returnArrayJson = new StringBuilder("[");
        for (int i = 0; i < point.length; i++) {
            returnArrayJson.append("{\"name\":\"" + point[i].getName() + "\",\"amenity\":\"" + point[i].getAmenity() + "\",\"latitude\":\"" + point[i].getLatitude() + "\",\"amenity\":\"" + point[i].getLongitude() + "}");
            if (i < point.length - 1)
                returnArrayJson.append(",");
        }
        returnArrayJson.append("]");
        return returnArrayJson.toString();
    }

    public void insert() {
        trips().save(this);
    }

    public void remove() {
        trips().remove(this._id);
    }
}
