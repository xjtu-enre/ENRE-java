package entity;

import entity.properties.Location;

import java.util.ArrayList;

public class ScopeEntity extends BaseEntity{


    protected ArrayList<String> casType = new ArrayList<>();


    public void addCastype (String castype){
        this.casType.add(castype);
    }

    public ArrayList<String> getCasType(){
        return this.casType;
    }

}
