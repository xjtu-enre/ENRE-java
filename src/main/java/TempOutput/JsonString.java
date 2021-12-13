package TempOutput;

import java.util.*;

import entity.*;
import org.json.JSONObject;

import util.SingleCollect;
import visitor.relationInf.RelationInf;

public class JsonString {

    public static HashMap<String, Integer> turnStrMap(String status){
        HashMap<String, Integer> nameTonum = new HashMap<>();
        String[] cells = status.split("\n");
        for(String cell : cells){
            String[] nameAnum = cell.split(": ");
            nameTonum.put(nameAnum[0], Integer.valueOf(nameAnum[1]));
        }
        return nameTonum;
    }

    public static String JSONWriteRelation(Map<Integer, Map<Integer, Map<String, Integer>>> relationMap) throws Exception {

        JSONObject obj=new JSONObject();//创建JSONObject对象

        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

        obj.put("schemaVersion","1");
        Iterator<BaseEntity> iterator = singleCollect.getEntities().iterator();

        List<JSONObject> subCategories = new ArrayList<>();
        for (String i : List.of("Package", "File", "Class", "Interface", "Annotation", "Enum", "Method", "Variable", "EnumConstant", "AnnotationTypeMember")){
            JSONObject cate = new JSONObject();
            cate.put("name", i);
            subCategories.add(cate);
        }
        obj.put("categories", subCategories);

        RelationInf relationInf = new RelationInf();
        JSONObject subEntities = new JSONObject();
        for(String entity : turnStrMap(relationInf.entityStatis()).keySet()){
            subEntities.put(entity, turnStrMap(relationInf.entityStatis()).get(entity));
        }
        obj.put("entityNum", subEntities);

        JSONObject subRelations = new JSONObject();
        for(String relation : turnStrMap(relationInf.dependencyStatis()).keySet()){
            subRelations.put(relation, turnStrMap(relationInf.dependencyStatis()).get(relation));
        }
        obj.put("relationNum", subRelations);

        List<JSONObject> subObjVariable=new ArrayList<JSONObject>();//创建对象数组里的子对象
//        List<String> subObjVariable=new ArrayList<String>();

        while(iterator.hasNext()) {
            BaseEntity entity = iterator.next();
            JSONObject entityObj = new JSONObject();
            entityObj.put("id", entity.getId());
            entityObj.put("category", singleCollect.getEntityType(entity.getId()));
            entityObj.put("name", entity.getName());
            entityObj.put("qualifiedName", entity.getQualifiedName());
            entityObj.put("parentId", entity.getParentId());
            entityObj.put("external", false);
            if(entity instanceof MethodEntity){
                entityObj.put("accessibility", ((MethodEntity) entity).getAccessibility());
                entityObj.put("static", ((MethodEntity) entity).isStatic());
            }
            if(entity instanceof VariableEntity){
                entityObj.put("accessibility", ((VariableEntity) entity).getAccessibility());
            }
//            entityObj.put("childrenIds", entity.getChildrenIds());

//            subObjVariable.add(entity.getQualifiedName());
            subObjVariable.add(entityObj);
        }

        for (ExternalEntity externalEntity : singleCollect.getExternalEntities()) {
            JSONObject external = new JSONObject();
            external.put("qualifiedName", externalEntity.getQualifiedName());
            external.put("external", true);
            subObjVariable.add(external);
        }

        obj.put("variables",subObjVariable);


        for(int fromEntity:relationMap.keySet()) {
            for(int toEntity:relationMap.get(fromEntity).keySet()) {
                for(String type:relationMap.get(fromEntity).get(toEntity).keySet()) {
                    JSONObject subObj=new JSONObject();//创建对象数组里的子对象

//                    JSONObject srcObj = new JSONObject();
//                    srcObj.put("id", fromEntity);
//                    srcObj.put("type", singleCollect.getEntityType(fromEntity));
//                    srcObj.put("name", singleCollect.getEntityById(fromEntity).getName());
//                    srcObj.put("qualified name", singleCollect.getEntityById(fromEntity).getQualifiedName());
//                    srcObj.put("parentId", singleCollect.getEntityById(fromEntity).getParentId());
//                    srcObj.put("childrenIds", singleCollect.getEntityById(fromEntity).getChildrenIds());
//
//                    JSONObject destObj = new JSONObject();
//                    destObj.put("id", toEntity);
//                    destObj.put("type", singleCollect.getEntityType(toEntity));
//                    destObj.put("name", singleCollect.getEntityById(toEntity).getName());
//                    destObj.put("qualified name", singleCollect.getEntityById(toEntity).getQualifiedName());
//                    destObj.put("parentId", singleCollect.getEntityById(toEntity).getParentId());
//                    destObj.put("childrenIds", singleCollect.getEntityById(toEntity).getChildrenIds());

                    subObj.put("src",fromEntity);
                    subObj.put("dest",toEntity);
//                    subObj.accumulate("src", srcObj);
//   l                 subObj.accumulate("dest", destObj);

                    JSONObject reObj=new JSONObject();//创建对象数组里的子对象
                    reObj.put(type, 1);
                    subObj.accumulate("values",reObj);
                    obj.accumulate("cells",subObj);

                }

            }
        }

        return obj.toString();
    }

    public static String JSONWriteEntity(List<BaseEntity> entityList) throws Exception {


        JSONObject obj=new JSONObject();//创建JSONObject对象

        obj.put("schemaVersion","1");

        List<String> subObjVariable=new ArrayList<String>();//创建对象数组里的子对象
        for(BaseEntity en:entityList) {
            subObjVariable.add(en.toString());
        }
        obj.put("variables",subObjVariable);

        return obj.toString();
    }

}
