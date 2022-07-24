package entity;

import java.util.HashMap;

public class ClassEntity extends TypeEntity{

    protected HashMap<String, Integer> interfaces = new HashMap<String, Integer>();
    protected String superClassName;
    protected int superClassId = -1;

    //mark whether the current class is anonymous, if it's not, the value will be 0, otherwise the value will be the order of anonymous
    protected int anonymousRank = 0;

    protected int anonymousBindVar = -1;

    protected HashMap<String, Integer> staticMap = new HashMap<>();

    public ClassEntity(int id, String name){
        this.id = id;
        this.name = name;
    }

    public ClassEntity(int id, String name, String qualifiedName, int parentId){
        this.id = id;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public int getSuperClassId() {
        return superClassId;
    }

    public void setSuperClassId(int superClassId) {
        this.superClassId = superClassId;
    }

    public HashMap<String, Integer> getInterfaces(){
        return this.interfaces;
    }

    public void addInterface(String name, int id){
        this.interfaces.put(name, id);
    }

    public HashMap<String, Integer> getStaticMap(){
        return this.staticMap;
    }

    public void addStaticMap(String name, int id){
        this.staticMap.put(name, id);
    }

    public void setAnonymousRank(int rank){
        this.anonymousRank = rank;
    }

    public int getAnonymousRank(){
        return this.anonymousRank;
    }

    public int getAnonymousBindVar(){
        return this.anonymousBindVar;
    }

    public void setAnonymousBindVar(int bindVar){
        this.anonymousBindVar = bindVar;
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "interfaces=" + interfaces +
                ", superClassName='" + superClassName + '\'' +
                ", superClassId=" + superClassId +
                ", importedBy=" + importedBy +
                ", location=" + location +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", simpleName='" + name + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                ", childrenIds=" + childrenIds +
                //", codeSnippet='" + codeSnippet + '\'' +
                ", relation=" + relation +
                '}';
    }
}
