package entity.properties;

import util.Configure;

public class ReflectSite {

    String reflectObj;
    int reflectObjId;

    String kind;
    String[] arguments;

    /**
     * For reflect class and method in same line
     * Class.class.getDeclaredMethod(...)
     */
    String declaredClass;

    /**
     * For reflect class and method in separated line
     */
    int implementVar;
    int bindVar;

    boolean modifyAccessible;
    Location location;

    public ReflectSite(String classQualifiedName, int implementVar){
        this.reflectObj = classQualifiedName;
        this.implementVar = implementVar;
        this.kind = Configure.REFLECT_CLASS;
    }

    public ReflectSite(String methodSimpleName, String[] args, String declaredClass, int implementVar){
        this.reflectObj = methodSimpleName;
        this.arguments = args;
        this.declaredClass = declaredClass;
        this.kind = Configure.REFLECT_METHOD;
        this.implementVar = implementVar;
    }

    public ReflectSite(String methodSimpleName, String[] args, int implementVar, int bindVar){
        this.reflectObj = methodSimpleName;
        this.implementVar = implementVar;
        this.kind = Configure.REFLECT_METHOD;
        this.arguments = args;
        this.bindVar = bindVar;
    }

    public void setBindVar(int id){
        this.bindVar = id;
    }

    public int getBindVar(){
        return this.bindVar;
    }

    public String getReflectObj() {
        return reflectObj;
    }

    public void setReflectObj(String reflectObj) {
        this.reflectObj = reflectObj;
    }

    public void setReflectObjId(int id){
        this.reflectObjId = id;
    }

    public int getReflectObjId(){
        return this.reflectObjId;
    }

    public int getImplementVar() {
        return implementVar;
    }

    public void setImplementVar(int implementVar) {
        this.implementVar = implementVar;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean getModifyAccessible() {
        return modifyAccessible;
    }

    public void setModifyAccessible(boolean setAccessible) {
        this.modifyAccessible = setAccessible;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDeclaredClass(String declaredClass){
        this.declaredClass = declaredClass;
    }

    public String getDeclaredClass(){
        return this.declaredClass;
    }

}
