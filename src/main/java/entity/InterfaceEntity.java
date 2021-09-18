package entity;

import java.util.ArrayList;

public class InterfaceEntity extends TypeEntity{

    protected ArrayList<String> extendsNames = new ArrayList<String>();
    protected ArrayList<Integer> extendsIds = new ArrayList<Integer>();

    public InterfaceEntity(int id, String name){
        this.id = id;
        this.name = name;
    }

    public InterfaceEntity(int id, String name, String qualifiedName, int parentId){
        this.id = id;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    public ArrayList<String> getExtendsNames(){
        return this.extendsNames;
    }

    public void addExtendsName(String extendName){
        this.extendsNames.add(extendName);
    }

    public ArrayList<Integer> getExtendsIds(){
        return this.extendsIds;
    }

    public void addExtendsId(int extendsId){
        this.extendsIds.add(extendsId);
    }

    public void addExtendsIds(ArrayList<Integer> extendsIds){
        this.extendsIds.addAll(extendsIds);
    }

    @Override
    public String toString() {
        return "InterfaceEntity{" +
                "extendsNames=" + extendsNames +
                ", extendsIds=" + extendsIds +
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
