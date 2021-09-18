package entity;

import java.util.ArrayList;

public class PackageEntity extends BaseEntity{

    protected String fullPath;
    protected ArrayList<Integer> importedBy = new ArrayList<Integer>();

    public PackageEntity(int id, String name, String fullPath){
        this.id = id;
        this.qualifiedName = name;
        this.fullPath = fullPath;
    }

    public void setSimpleName(){
        String[] elements = this.getQualifiedName().split("\\.");
        this.name = elements[elements.length-1];
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void addImportBy(int id){
        this.importedBy.add(id);
    }

    public ArrayList<Integer> getImportedBy(){
        return this.importedBy;
    }

    @Override
    public String toString() {
        return "PackageEntity{" +
                "fullPath='" + fullPath + '\'' +
                ", importedBy=" + importedBy +
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
