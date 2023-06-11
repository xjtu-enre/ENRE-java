package entity;

import entity.properties.CallSite;
import entity.properties.Location;
import entity.properties.QualifiedNameSite;
import util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

public class ScopeEntity extends BaseEntity{


    protected ArrayList<Tuple<String, Location>> casType = new ArrayList<>();

    //record the declared class's qualified name of called method and its name
    //like declared class-called method
    protected ArrayList<CallSite> call = new ArrayList<>();
    //record the called super method name
    protected ArrayList<Tuple<String, Location>> callNondynamic = new ArrayList<>();

    protected HashMap<String, Integer> name2Id = new HashMap<>();
    protected HashMap<String, String> name2Role = new HashMap<>();

    protected ArrayList<QualifiedNameSite> qualifiedNameSite = new ArrayList<>();

    public void addQualifiedNameSite(Location loc, String createdType, String varName){
        this.qualifiedNameSite.add(new QualifiedNameSite(loc, createdType, varName));
    }

    public ArrayList<QualifiedNameSite> getQualifiedNameSite(){
        return this.qualifiedNameSite;
    }

    public void addCastype (String castype, Location loc){
        this.casType.add(new Tuple<>(castype, loc));
    }

    public ArrayList<Tuple<String, Location>> getCasType(){
        return this.casType;
    }

    public void addCall(String className, String methodName, Location loc, int bindVar, ArrayList<String> parTypes, ArrayList<String> arguments){
        this.call.add(new CallSite(className, methodName, bindVar, loc, parTypes, arguments));
    }

    public void addCall(String className, String methodName, Location loc, String bindVarName, ArrayList<String> parTypes, ArrayList<String> arguments){
        this.call.add(new CallSite(className, methodName, bindVarName, loc, parTypes, arguments));
    }

    public void addExternalCall(String className, String methodName, Location loc, String bindVarName, int bindVar, ArrayList<String> arguments){
        this.call.add(new CallSite(className, methodName, bindVarName, bindVar, loc, arguments));
    }
    public  ArrayList<CallSite> getCall() {
        return this.call;
    }

    public void addCallNondynamic(String name, Location loc){
        this.callNondynamic.add(new Tuple<>(name, loc));
    }

    public  ArrayList<Tuple<String, Location>> getCallNondynamic() {
        return this.callNondynamic;
    }

    public HashMap<String, Integer> getName2Id(){
        return this.name2Id;
    }

    public void addName2Id(String name, int id){
        if(!this.name2Id.containsKey(name)){
            this.name2Id.put(name, id);
        }
    }

    public HashMap<String, String> getName2Role(){ return this.name2Role; }

    public void addName2Role (String name, String role){
        if(!this.name2Role.containsKey(name)){
            this.name2Role.put(name, role);
        }
    }
}
