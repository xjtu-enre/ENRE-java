package entity;

import java.util.HashMap;

public class ClassEntity extends TypeEntity{

    protected HashMap<String, Integer> interfaces = new HashMap<String, Integer>();
    protected String superClassName;
    protected int superClassId = -1;
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

    public void addInterfaces(String name, int id){
        this.interfaces.put(name, id);
    }

    public HashMap<String, Integer> getStaticMap(){
        return this.staticMap;
    }

    public void addStaticMap(String name, int id){
        this.staticMap.put(name, id);
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
