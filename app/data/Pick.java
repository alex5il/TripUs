package data;

import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by sergio on 08/04/2016.
 */
public class Pick {

    // auto
    @MongoObjectId
    private String _id;
    private String amenity;
    private String rank;

    public Pick(String amenityVal, String rankVal) {
        this.amenity = amenityVal;
        this.rank = rankVal;
    }

    public Pick() {
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
