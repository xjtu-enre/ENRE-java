package util;

import entity.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *This class is aimed to collect all entities in a java project.
 *To avoid repeated and multiple objects of this class, which may lead to wrong collection, the utilization of Singleton
 *mode is necessary. The singleton class only has one object, and this object could only be created by class itself.
 *
 *Date: 24/12/2020
 */

public class SingleCollect {

    //entities' id = index
    private ArrayList<BaseEntity> entities = new ArrayList<BaseEntity>();

    private ArrayList<ExternalEntity> externalEntities = new ArrayList<>();

    //packages' qualified name and id that already be created
    private HashMap<String,Integer> createdPackage = new HashMap<String,Integer>();

    //classes's qualified name and id that already be created
    private HashMap<String,Integer> createdType = new HashMap<>();

    private HashMap<String,Integer> createdAnt = new HashMap<>();

    private ArrayList<Integer> fileIds = new ArrayList<>();

    private static SingleCollect singleCollectInstance = new SingleCollect();

    public ArrayList<BaseEntity> getEntities() {
        return this.entities;
    }

    public void addEntity(BaseEntity entity) {
        this.entities.add(entity);
    }

    public ArrayList<ExternalEntity> getExternalEntities() {
        return this.externalEntities;
    }

    public void addEntity(ExternalEntity entity) {
        this.externalEntities.add(entity);
    }

    public static SingleCollect getSingleCollectInstance() {
        return singleCollectInstance;
    }

    public int getCurrentIndex(){
        return this.entities.size();
    }

    public HashMap<String,Integer> getCreatedPackage(){
        return this.createdPackage;
    }

    public void addCreatedPackage (int packageId, String packageName){
        this.createdPackage.put(packageName,packageId);
    }

    public HashMap<String,Integer> getCreatedType(){
        return this.createdType;
    }

    public void addCreatedType (int typeId, String typeName){
        this.createdType.put(typeName,typeId);
    }

    public HashMap<String,Integer> getCreatedAnt(){
        return this.createdAnt;
    }

    public void addCreatedAnt (int antId, String antName){
        this.createdAnt.put(antName,antId);
    }

    public void addFileId (int fileId){
        this.fileIds.add(fileId);
    }

    public int getFileIndex (int fileId){
        return this.fileIds.indexOf(fileId);
    }

    public ArrayList<Integer> getFileIds(){
        return this.fileIds;
    }

    public BaseEntity getEntityById(int id) {
        return entities.get(id);
    }

    public boolean isPackage (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof PackageEntity;
    }

    public boolean isFile (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof FileEntity;
    }

    public boolean isClass (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof ClassEntity;
    }

    public boolean isInterface (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof InterfaceEntity;
    }

    public boolean isMethod (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof MethodEntity;
    }

    public boolean isConstructor (int id){
        if(id == -1) {
            return false;
        }
        if(singleCollectInstance.getEntities().get(id) instanceof MethodEntity){
            return ((MethodEntity) singleCollectInstance.getEntityById(id)).isConstructor();
        }
        return false;
    }

    public boolean isVariable (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof VariableEntity;
    }

    public boolean isEnum (int id){
        if(id == -1){
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof EnumEntity;
    }

    public boolean isEnumCont(int id){
        if(id == -1){
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof EnumConstantEntity;
    }

    public boolean isAnnotation (int id){
        if(id == -1){
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof AnnotationEntity;
    }

    public boolean isAnnotationMem (int id){
        if(id == -1){
            return false;
        }
        return singleCollectInstance.getEntityById(id) instanceof AnnotationTypeMember;
    }

    public String getEntityType(int id){
        String type = "Unknown";
        if(isPackage(id))
            type = "Package";
        else if (isClass(id))
            type = "Class";
        else if (isMethod(id))
            type = "Method";
        else if (isFile(id))
            type = "File";
        else if (isConstructor(id))
            type = "Constructor";
        else if (isInterface(id))
            type = "Interface";
        else if (isAnnotation(id))
            type = "Annotation";
        else if (isEnum(id))
            type = "Enum";
        else if (isEnumCont(id))
            type = "Enum Constant";
        else if (isAnnotationMem(id))
            type = "Annotation Member";
        else if (isVariable(id))
            type = "Variable";
        return type;
    }

}
