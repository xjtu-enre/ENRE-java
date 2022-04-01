package yyInterface;

import client.IdentifyEntities;
import client.IdentifyRelations;
import entity.BaseEntity;
import entity.ClassEntity;
import entity.FileEntity;
import entity.MethodEntity;
import entity.properties.Location;
import entity.properties.Relation;
import util.SingleCollect;
import util.Tuple;

import java.util.HashMap;
import java.util.Map;


public class Impl {
    static SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public Impl(){

    }

    public static void identify(String inputDir, String projectName) {
        //identify Entities
        IdentifyEntities entityTreeBuilder = new IdentifyEntities(inputDir,projectName);
        entityTreeBuilder.run();

        //extract Deps
        IdentifyRelations entityDepAnalyzer = new IdentifyRelations();
        entityDepAnalyzer.run();
    }

    public static HashMap<String,Integer> getClassInfo() {
        HashMap<String ,Integer> classInfo = new HashMap<>();
        int index = 1;
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ClassEntity){
                classInfo.put(entity.getQualifiedName(), index);
                ++index;
            }
        }
        return classInfo;
    }

    public static Map<String, Map<String, Integer>> getStructure() {
        Map<String, Map<String, Integer>> struct = new HashMap<>();
        for(BaseEntity entity : singleCollect.getEntities()){
            //import
            if(entity instanceof FileEntity && !entity.getChildrenIds().isEmpty()) {
                Map<String, Integer> temp = new HashMap<>();
                //file --> class
                for (Tuple<String, Location> imported: ((FileEntity) entity).getImportClass().keySet()) {
                    if (!((FileEntity) entity).getImportClass().get(imported).equals(1)) {
                        temp.put(imported.getL(), 1);
                    }
                }
                //file --> package
//                for (String imported : ((FileEntity) entity).getImportOnDemand().keySet()) {
//                    if (((FileEntity) entity).getImportOnDemand().get(imported) != -1) {
//                        temp.put(imported, 1);
//                    }
//                }
                if (!temp.isEmpty()) {
                    struct.put(singleCollect.getEntityById(entity.getChildrenIds().get(0)).getQualifiedName(), temp);
                }
            }
            if(entity instanceof ClassEntity){
                //inherit : class --> class
                String superClassName;
                if(((ClassEntity) entity).getSuperClassId() != -1){
                    superClassName = ((ClassEntity) entity).getSuperClassName();
                    if(struct.containsKey(entity.getQualifiedName())){
                        if(struct.get(entity.getQualifiedName()).containsKey(superClassName)){
                            int count = struct.get(entity.getQualifiedName()).get(superClassName);
                            count++;
                            struct.get(entity.getQualifiedName()).replace(superClassName, count);
                        }
                        else{
                            struct.get(entity.getQualifiedName()).put(superClassName, 1);
                        }
                    }else{
                        Map<String , Integer> temp = new HashMap<>();
                        temp.put(superClassName, 1);
                        struct.put(entity.getQualifiedName(), temp);
                    }
                }
                //implement : class --> interface
                for(Relation relation: entity.getRelation()){
                    if(relation.getKind().equals("Implement")){
                        String interfaceName = singleCollect.getEntityById(relation.getToEntity()).getQualifiedName();
                        if(struct.containsKey(entity.getQualifiedName())){
                            if(struct.get(entity.getQualifiedName()).containsKey(interfaceName)){
                                int count = struct.get(entity.getQualifiedName()).get(interfaceName);
                                count++;
                                struct.get(entity.getQualifiedName()).replace(interfaceName, count);
                            } else {
                                struct.get(entity.getQualifiedName()).put(interfaceName, 1);
                            }
                        }else{
                            Map<String , Integer> temp = new HashMap<>();
                            temp.put(interfaceName, 1);
                            struct.put(entity.getQualifiedName(), temp);
                        }
                    }
                }
            }
            //call method --> method
            if(entity instanceof MethodEntity){
                String currentClass = singleCollect.getEntityById(entity.getParentId()).getQualifiedName();
                for(Tuple<String, Location> class_method : ((MethodEntity) entity).getCall()){
                    String calledClass = class_method.getL().split("-")[0];
                    //different class
                    if(!currentClass.equals(calledClass)){
                        if(struct.containsKey(currentClass)){
                            if (struct.get(currentClass).containsKey(calledClass)){
                                int count = struct.get(currentClass).get(calledClass);
                                count++;
                                struct.get(currentClass).replace(calledClass, count);
                            } else {
                                struct.get(currentClass).put(calledClass, 1);
                            }
                        }else{
                            Map<String , Integer> temp = new HashMap<>();
                            temp.put(calledClass, 1);
                            struct.put(currentClass, temp);
                        }
                    }
                }
            }
        }
        return struct;
    }

    public static void clear(){
        singleCollect.getEntities().clear();
    }

}
