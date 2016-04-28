package data;

public class Result {
    private String _id;
    private String name;
    private Point[] point;

    public Result(String name, Point[] point) {
        this.name = name;
        this.point = point;
    }

    public Result() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTripResult() {
        StringBuilder returnArrayJson = new StringBuilder("{\"name\":\"" + this.getName() + "\",\"points\" : [");
        for (int i = 0; i < point.length; i++) {
            returnArrayJson.append("{\"name\":\"" + point[i].getName() + "\",\"amenity\":\"" + point[i].getAmenity() + "\",\"latitude\":\"" + point[i].getLatitude() + "\",\"longitude\":\"" + point[i].getLongitude() + "\"}");
            if (i < point.length - 1)
                returnArrayJson.append(",");
        }
        returnArrayJson.append("]}");
        return returnArrayJson.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point[] getPoint() {
        return point;
    }

    public void setPoint(Point[] point) {
        this.point = point;
    }
}
