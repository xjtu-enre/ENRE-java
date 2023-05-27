package entity;

import util.Configure;
import util.PathUtil;

import java.util.ArrayList;
import java.util.Objects;

public class ExternalEntity{

    protected int fileId = -1;
    int id = -1;
    String name;
    String qualifiedName;
    String type;
    String[] parameterType;
    String returnType;

    public ExternalEntity(int id, int fileId, String qualifiedName){
        this.type = Configure.EXTERNAL_ENTITY_UNKNOWN;
        this.id = id;
        this.fileId = fileId;
        this.qualifiedName = qualifiedName;
        this.name = PathUtil.getLastStrByDot(qualifiedName);
    }

    public ExternalEntity(int id, String qualifiedName, String methodName, String[] parameterType, String returnType){
        this.type = Configure.EXTERNAL_ENTITY_METHOD;
        this.id = id;
        this.qualifiedName = qualifiedName;
        this.name = methodName;
        this.parameterType = parameterType;
        this.returnType = returnType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getParameterType() {
        return parameterType;
    }

    public void setParameterType(String[] parameterType) {
        this.parameterType = parameterType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
