package data;

import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by sergio on 08/04/2016.
 */
public class User {

    // auto
    @MongoObjectId
    private String _id;
    private Pick[] requirements;
    private String name;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public Pick[] getRequirements() {
        return requirements;
    }

    public void setRequirements(Pick[] requirements) {
        this.requirements = requirements;
    }

    public String getRequirementsJsonReturn() {
        StringBuilder returnArrayJson = new StringBuilder("[");
        if (requirements != null) {
            for (int i = 0; i < requirements.length; i++) {
                returnArrayJson.append("{ \"amenity\":\"" + requirements[i].getAmenity() + "\",\"rank\":\"" + requirements[i].getRank() + "\"}");
                if (i < requirements.length - 1)
                    returnArrayJson.append(",");
            }
        }
        returnArrayJson.append("]");
        return returnArrayJson.toString();
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

}
