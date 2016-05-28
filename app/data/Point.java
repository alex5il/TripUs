package data;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Point {
    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    @MongoObjectId
    private String _id;
    private Properties properties;
    private Geometry geometry;

    public static MongoCollection getPointsCollection() {
        return PlayJongo.getCollection("points");
    }

    public static MongoCursor<Point> getPointsCursor() {
        return getPointsCollection().find().as(Point.class);
    }

    public static MongoCursor<Point> getRandomPoints(int quantity) {
        int totalPoints = (int) getPointsCollection().count();
        Random rnd = new Random();

        if (quantity > totalPoints) {
            quantity = totalPoints;
        }

        return getPointsCollection().find().limit(quantity).skip(rnd.nextInt(totalPoints - quantity)).as(Point.class);
    }

    public static MongoCursor<Point> getByAmenities(Collection<String> amenities) {
        return getPointsCollection().find("{'properties.amenity':{$in:#}}", amenities).as(Point.class);
    }

    public static ArrayList<Point> getByAmenities(Collection<String> amenities, int limit) {
        MongoCursor<Point> pointsCursor;
        ArrayList<Point> pointsArray = new ArrayList<>();

        // Fetching in loop so that every amenity will get equal amount of results
        for (String amenity : amenities) {
            pointsCursor = getPointsCollection().find("{'properties.amenity':#}", amenity).
                    limit(limit / amenities.size()).as(Point.class);
            
            pointsCursor.forEach(point -> {
                pointsArray.add(point);
            });
        }

        return pointsArray;
    }

    public String getId() {
        return this._id;
    }

    public long getOsmId() {
        return this.properties.getOsmId();
    }

    public String getName() {
        return this.properties.getName();
    }

    public String getAmenity() {
        return this.properties.getAmenity();
    }

    public double getLatitude() {
        return this.geometry.getLatitude();
    }

    public double getLongitude() {
        return this.geometry.getLongitude();
    }
}
