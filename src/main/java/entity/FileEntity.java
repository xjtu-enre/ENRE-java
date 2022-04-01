package entity;

import entity.properties.Location;
import util.PathUtil;
import util.Tuple;

import java.util.HashMap;

public class FileEntity extends BaseEntity{

    protected HashMap<Tuple<String, Location>, Integer> importOnDemand = new HashMap<Tuple<String, Location>, Integer>();
    //the first is name, the second is package
    protected HashMap<Tuple<String, Location>, Integer> importClass = new HashMap<Tuple<String, Location>, Integer>();

    protected HashMap<Tuple<String, Location>, Integer> importStatic = new HashMap<>();


    protected String fullPath;

    public FileEntity (int fileId, String fullPath){
        this.id = fileId;
        this.setFullPath(fullPath);
//        this.setName(PathUtil.getLastStrByPathDelimiter(fullPath).split("\\.")[0]);
        this.setName(PathUtil.getLastStrByPathDelimiter(fullPath));
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void addImportOnDemand(String pkg, Location loc){
        this.importOnDemand.put(new Tuple<>(pkg, loc), -1);
    }

    public void addImportClass(String file, Location loc){
        this.importClass.put(new Tuple<>(file, loc), -1);
    }

    public HashMap<Tuple<String, Location>, Integer> getImportOnDemand(){
        return this.importOnDemand;
    }

    public HashMap<Tuple<String, Location>, Integer> getImportClass(){
        return this.importClass;
    }

    public void addImportStatic(String constant, Location loc){
        this.importStatic.put(new Tuple<>(constant, loc), -1);
    }

    public HashMap<Tuple<String, Location>, Integer> getImportStatic(){
        return this.importStatic;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "importOnDemand=" + importOnDemand +
                ", importClass=" + importClass +
                ", fullPath='" + fullPath + '\'' +
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
