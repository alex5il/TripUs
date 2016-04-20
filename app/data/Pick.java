package data;

import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by sergio on 08/04/2016.
 */
public class Pick {

    // auto
    @MongoObjectId
    private String _id;

    private int id;

    public Pick() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
