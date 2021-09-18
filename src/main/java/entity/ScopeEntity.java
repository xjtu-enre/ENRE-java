package entity;

import entity.properties.Location;

public class ScopeEntity extends BaseEntity{

    protected Location location = new Location();

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
