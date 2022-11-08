package entity.properties;

import java.util.ArrayList;

public class CallSite {

    private String declaringTypeQualifiedName;
    private String callMethodName;
    //the var implement call
    private int bindVar = -1;
    private String bindVarName = null;
    private Location location;
    private ArrayList<String> parTypes;
    private ArrayList<String> arguments;

//    public CallSite(String declaringTypeQualifiedName, String callMethodName, Location location) {
//        this.declaringTypeQualifiedName = declaringTypeQualifiedName;
//        this.callMethodName = callMethodName;
//        this.location = location;
//    }

    public CallSite(String declaringTypeQualifiedName, String callMethodName, int bindVar, Location location, ArrayList<String> parTypes, ArrayList<String> arguments) {
        this.declaringTypeQualifiedName = declaringTypeQualifiedName;
        this.callMethodName = callMethodName;
        this.bindVar = bindVar;
        this.location = location;
        this.parTypes = parTypes;
        this.arguments = arguments;
    }

    public CallSite(String declaringTypeQualifiedName, String callMethodName, String bindVarName, Location location, ArrayList<String> parTypes, ArrayList<String> arguments) {
        this.declaringTypeQualifiedName = declaringTypeQualifiedName;
        this.callMethodName = callMethodName;
        this.bindVarName = bindVarName;
        this.location = location;
        this.parTypes = parTypes;
        this.arguments = arguments;
    }

    public CallSite(String declaringTypeQualifiedName, String callMethodName, String bindVarName, int bindVarId, Location loc, ArrayList<String> arguments){
        this.declaringTypeQualifiedName = declaringTypeQualifiedName;
        this.callMethodName = callMethodName;
        this.bindVarName = bindVarName;
        this.bindVar = bindVarId;
        this.location = loc;
        this.arguments = arguments;
    }

    public String getDeclaringTypeQualifiedName() {
        return declaringTypeQualifiedName;
    }

    public void setDeclaringTypeQualifiedName(String declaringTypeQualifiedName) {
        this.declaringTypeQualifiedName = declaringTypeQualifiedName;
    }

    public String getCallMethodName() {
        return callMethodName;
    }

    public void setCallMethodName(String callMethodName) {
        this.callMethodName = callMethodName;
    }

    public int getBindVar() {
        return bindVar;
    }

    public void setBindVar(int bindVar) {
        this.bindVar = bindVar;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getBindVarName() {
        return bindVarName;
    }

    public void setBindVarName(String bindVarName) {
        this.bindVarName = bindVarName;
    }

    public void addParType(String parType){this.parTypes.add(parType);}

    public ArrayList<String> getParTypes() {
        return parTypes;
    }

    public void addArgument(String arg){
        this.arguments.add(arg);
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }
}
