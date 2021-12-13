package entity;

import entity.properties.Location;

import java.util.ArrayList;

public class ScopeEntity extends BaseEntity{

    protected Location location = new Location();
    protected ArrayList<String> casType = new ArrayList<>();

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addCastype (String castype){
        this.casType.add(castype);
    }

    public ArrayList<String> getCasType(){
        return this.casType;
    }

}
