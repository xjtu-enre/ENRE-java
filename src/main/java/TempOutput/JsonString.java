package TempOutput;

import java.util.*;

import entity.*;
import entity.properties.Relation;
import org.json.JSONObject;

import util.PathUtil;
import util.SingleCollect;
import util.Tuple;
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

    /**
     * get current entity's file id
     * @param id
     * @return
     */
    public static int getCurrentFileId(int id){
        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        if(singleCollect.getEntityById(id) instanceof FileEntity){
            return id;
        }
        else if (singleCollect.getEntityById(singleCollect.getEntityById(id).getParentId()) instanceof FileEntity){
            return singleCollect.getEntityById(id).getParentId();
        }
        else {
            return getCurrentFileId(singleCollect.getEntityById(id).getParentId());
        }
    }

    public static String JSONWriteRelation(Map<Integer, ArrayList<Tuple<Integer, Relation>>> relationMap, String hiddenPath) throws Exception {

        JSONObject obj=new JSONObject();//创建JSONObject对象

        SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
        ProcessHidden processHidden = ProcessHidden.getProcessHiddeninstance();
        if (hiddenPath != null){
            processHidden.convertCSV(hiddenPath);
//            System.out.println(processHidden.getResult());
        }

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

        JSONObject subCKIndices = new JSONObject();
        for (String index : singleCollect.getCkIndices().keySet()){
            subCKIndices.put(index, singleCollect.getCk(index));
        }
        obj.put("CKIndices", subCKIndices);

        List<JSONObject> subObjVariable=new ArrayList<JSONObject>();

        while(iterator.hasNext()) {
            BaseEntity entity = iterator.next();
            JSONObject entityObj = new JSONObject();
            entityObj.put("id", entity.getId());
            entityObj.put("category", singleCollect.getEntityType(entity.getId()));
            entityObj.put("name", entity.getName());
            entityObj.put("qualifiedName", entity.getQualifiedName());
            entityObj.put("parentId", entity.getParentId());
            entityObj.put("external", false);
            if (entity.getRawType() != null){
                String raw = entity.getRawType();
                entityObj.put("rawType", processRawType(raw));
            }
            //AOSP HIDDEN API
//            JSONObject hiddenObj = new JSONObject();
//            hiddenObj.put("hidden", entity.getHidden());
//            hiddenObj.put("maxTargetSdk", entity.getMaxTargetSdk());
//            entityObj.accumulate("aosp_hidden", hiddenObj);
            //Modifiers
            if (!entity.getModifiers().isEmpty()){
                String m = "";
                for (String modifier : entity.getModifiers()){
                    if (!modifier.contains("@")){
                        m = m.concat(modifier + " ");
                    }
                }
                if (m.endsWith(" ")){
                    m = m.substring(0, m.length()-1);
                }
//                try {
//                    entityObj.put("modifiers", m.substring(0, m.length()-1));
//                }catch (StringIndexOutOfBoundsException e){
                entityObj.put("modifiers", m);
//                }

            }
            //entity File
            String entityFile;
            if (entity instanceof PackageEntity){
                entityFile = null;
            } else {
                entityFile = ((FileEntity) singleCollect.getEntityById(getCurrentFileId(entity.getId()))).getFullPath();
            }
            entityObj.put("File", entityFile);
            //variable kind
            if(entity instanceof VariableEntity){
                entityObj.put("global", ((VariableEntity) entity).getGlobal());
                if (!processHidden.getResult().isEmpty() && ((VariableEntity) entity).getGlobal() && processHidden.checkHidden((VariableEntity) entity)!= null){
                    entityObj.put("hidden", processHidden.checkHidden((VariableEntity) entity));
                }
            }
            //inner Type
            if(entity instanceof TypeEntity){
                if (!processHidden.getResult().isEmpty() && processHidden.checkHidden((TypeEntity)entity) != null){
                    entityObj.put("hidden", processHidden.checkHidden((TypeEntity)entity));
                }
                if (!((TypeEntity) entity).getInnerType().isEmpty()){
                    entityObj.put("innerType", ((TypeEntity) entity).getInnerType());
                }
            }
            //location
            if (!(entity instanceof FileEntity || entity instanceof PackageEntity)){
                JSONObject locObj = new JSONObject();
                locObj.put("startLine", entity.getLocation().getStartLine());
                locObj.put("endLine", entity.getLocation().getEndLine());
                locObj.put("startColumn", entity.getLocation().getStartColumn());
                locObj.put("endColumn", entity.getLocation().getEndColumn());
                entityObj.accumulate("location", locObj);
            }
            //method parameter Type
            if (entity instanceof MethodEntity){
                String parType = "";
                String parName = "";
                if (! ((MethodEntity) entity).getParameters().isEmpty()){
                    for (int parId : ((MethodEntity) entity).getParameters()){
                        parType = parType.concat(processRawType(singleCollect.getEntityById(parId).getRawType()) + " ");
                        parName = parName.concat(singleCollect.getEntityById(parId).getName() + " ");
                    }
                    parType = parType.substring(0, parType.length()-1);
                    parName = parName.substring(0, parName.length()-1);
                }
                JSONObject parObj = new JSONObject();
                parObj.put("names", parName);
                parObj.put("types", parType);
                entityObj.accumulate("parameter", parObj);
                if (!processHidden.getResult().isEmpty() && processHidden.checkHidden((MethodEntity)entity, parType)!= null){
                    entityObj.put("hidden", processHidden.checkHidden((MethodEntity)entity, parType));
                }
            }
            //bin path
            if (entity.getBinPath()!= null){
                JSONObject binObj = new JSONObject();
                binObj.put("binPath", entity.getBinPath().getL());
                binObj.put("binNum", entity.getBinPath().getR());
                entityObj.accumulate("additionalBin", binObj);
            }

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
            for(Tuple<Integer,Relation> toEntityObj:relationMap.get(fromEntity)) {
                    int toEntity=toEntityObj.getL();

//                for(Relation type : relationMap.get(fromEntity).get(toEntity)) {
                    Relation type = toEntityObj.getR();
                    JSONObject subObj=new JSONObject();//创建对象数组里的子对象

//                    JSONObject srcObj = new JSONObject();
//                    srcObj.put("id", fromEntity);
//                    srcObj.put("type", singleCollect.getEntityType(fromEntity));
//                    srcObj.put("name", singleCollect.getEntityById(fromEntity).getName());
//                    srcObj.put("qualified name", singleCollect.getEntityById(fromEntity).getQualifiedName());
//                    srcObj.put("parentId", singleCollect.getEntityById(fromEntity).getParentId());
//                    srcObj.put("childrenIds", singleCollect.getEntityById(fromEntity).getChildrenIds());

                    subObj.put("src",fromEntity);
                    subObj.put("dest",toEntity);
//                    subObj.accumulate("src", srcObj);
//   l                 subObj.accumulate("dest", destObj);

                    JSONObject reObj=new JSONObject();//创建对象数组里的子对象
                    reObj.put(type.getKind(), 1);
                    if (type.getBindVar() != -1){
                        reObj.put("bindVar", type.getBindVar());
                    }
                    if (type.getModifyAccessible()){
                        reObj.put("modifyAccessible", true);
                    }
                    if (type.getInvoke()){
                        try{
                            JSONObject locObj = new JSONObject();
                            locObj.put("startLine", type.getLocation().getStartLine());
                            locObj.put("endLine", type.getLocation().getEndLine());
                            locObj.put("startColumn", type.getLocation().getStartColumn());
                            locObj.put("endColumn", type.getLocation().getEndColumn());
                            reObj.accumulate("invoke", locObj);
                        } catch (NullPointerException e){
                            reObj.put("invoke", true);
                        }
                    }
                    subObj.accumulate("values",reObj);
                    obj.accumulate("cells",subObj);

//                }

            }
        }
        /**
         * Output not match hidden
         */
//        processHidden.outputResult();
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

    public static String processRawType (String rawType){
        if (rawType == null){
            return null;
        }else {
            if (rawType.contains("<")){
                String[] temp = rawType.split("<");
                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
            }
            if (rawType.contains(">")){
                rawType = rawType.replace(">", "");
            }
            if (rawType.contains("[")){
                String[] temp = rawType.split("\\[");
                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
            }
            if (rawType.contains("]")){
                rawType = rawType.replace("]", "");
            }
            if (rawType.contains(",")){
                String[] temp = rawType.split(",");
                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
            }
            if (rawType.contains("java")){
                String[] temp = rawType.split("\\.");
                rawType = temp[temp.length - 1];
            }
        }
        return rawType;
    }

}
