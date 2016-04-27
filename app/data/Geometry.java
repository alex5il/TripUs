package data;

public class Geometry {
    private double coordinates[];

    public double getLatitude() {
        return this.coordinates[1];
    }

    public double getLongitude() {
        return this.coordinates[0];
    }
}
