package entity;

import util.PathUtil;

import java.util.HashMap;

public class FileEntity extends BaseEntity{

    protected HashMap<String, Integer> importOnDemand = new HashMap<String, Integer>();
    //the first is name, the second is package
    protected HashMap<String, Integer> importClass = new HashMap<String, Integer>();

    protected String fullPath;

    public FileEntity (int fileId, String fullPath){
        this.id = fileId;
        this.setFullPath(fullPath);
        this.setName(PathUtil.getLastStrByPathDelimiter(fullPath).split("\\.")[0]);
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void addImportOnDemand(String pkg){
        this.importOnDemand.put(pkg, -1);
    }

    public void addImportClass(String file){
        this.importClass.put(file, -1);
    }

    public HashMap<String, Integer> getImportOnDemand(){
        return this.importOnDemand;
    }

    public HashMap<String, Integer> getImportClass(){
        return this.importClass;
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
