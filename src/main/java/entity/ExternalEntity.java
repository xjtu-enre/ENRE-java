package entity;

import util.PathUtil;

public class ExternalEntity{

    protected int fileId = -1;
    int id = -1;
    String name;
    String qualifiedName;

    public ExternalEntity(int id, int fileId, String qualifiedName){
        this.id = id;
        this.fileId = fileId;
        this.qualifiedName = qualifiedName;
        this.name = PathUtil.getLastStrByDot(qualifiedName);
    }

    public void setFileId(int fileId){
        this.fileId = fileId;
    }

    public int getFileId(){
        return this.fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
}
