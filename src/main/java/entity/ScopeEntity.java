package entity;

import entity.properties.Location;
import util.Tuple;

import java.util.ArrayList;

public class ScopeEntity extends BaseEntity{


    protected ArrayList<String> casType = new ArrayList<>();

    //record the declared class's qualified name of called method and its name
    //like declared class-called method
    protected ArrayList<Tuple<String, Location>> call = new ArrayList<>();
    //record the called super method name
    protected ArrayList<Tuple<String, Location>> callNondynamic = new ArrayList<>();

    public void addCastype (String castype){
        this.casType.add(castype);
    }

    public ArrayList<String> getCasType(){
        return this.casType;
    }

    public void addCall(String className, Location loc){
        this.call.add(new Tuple<>(className, loc));
    }

    public  ArrayList<Tuple<String, Location>> getCall() {
        return this.call;
    }

    public void addCallNondynamic(String name, Location loc){
        this.callNondynamic.add(new Tuple<>(name, loc));
    }

    public  ArrayList<Tuple<String, Location>> getCallNondynamic() {
        return this.callNondynamic;
    }
}
