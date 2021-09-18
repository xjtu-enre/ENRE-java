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

    //packages' qualified name and id that already be created
    private HashMap<String,Integer> createdPackage = new HashMap<String,Integer>();

    //classes's qualified name and id that already be created
    private HashMap<String,Integer> createdType = new HashMap<>();

    private static SingleCollect singleCollectInstance = new SingleCollect();

    public ArrayList<BaseEntity> getEntities() {
        return this.entities;
    }

    public void addEntity(BaseEntity entity) {
        this.entities.add(entity);
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

    public BaseEntity getEntityById(int id) {
        return entities.get(id);
    }

    public boolean isPackage (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof PackageEntity;
    }

    public boolean isFile (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof FileEntity;
    }

    public boolean isClass (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof ClassEntity;
    }

    public boolean isInterface (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof InterfaceEntity;
    }

    public boolean isMethod (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof MethodEntity;
    }

    public boolean isConstructor (int id){
        if(id == -1) {
            return false;
        }
        if(singleCollectInstance.getEntities().get(id) instanceof MethodEntity){
            return ((MethodEntity) singleCollectInstance.getEntities().get(id)).isConstructor();
        }
        return false;
    }

    public boolean isVariable (int id){
        if(id == -1) {
            return false;
        }
        return singleCollectInstance.getEntities().get(id) instanceof VariableEntity;
    }

}
