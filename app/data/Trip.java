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
    private Result[] results;
    private boolean isStarted;
    private boolean isFinished;

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

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
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

    public String getTripResults() {
        StringBuilder returnArrayJson = new StringBuilder("{\"results\":[");
        if (this.results != null) {
            for (int i = 0; i < results.length; i++) {
                returnArrayJson.append(results[i].getTripResult());
                if (i < results.length - 1)
                    returnArrayJson.append(",");
            }
        }
        returnArrayJson.append("], \"isFinished\":");
        returnArrayJson.append(this.isFinished);
        returnArrayJson.append("}");
        return returnArrayJson.toString();
    }

    public void addAnotherResult(Result result) {
        Result[] resultsNew;
        if (this.getResults() != null) {
            resultsNew = new Result[this.getResults().length + 1];
            for (int i = 0; i < results.length; i++) {
                resultsNew[i] = results[i];
            }
            resultsNew[results.length] = result;
        } else {
            resultsNew = new Result[1];
            resultsNew[0] = result;
        }

        this.setResults(resultsNew);
    }

    public void insert() {
        trips().save(this);
    }

    public void remove() {
        trips().remove(this._id);
    }
}
