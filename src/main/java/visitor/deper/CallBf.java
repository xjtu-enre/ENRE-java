package visitor.deper;

import entity.*;
import entity.properties.CallSite;
import entity.properties.Location;
import org.jetbrains.annotations.Nullable;
import util.Configure;
import util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ScopeEntity){
                //call
                if(!((ScopeEntity) entity).getCall().isEmpty()){
                    for(CallSite className2method : ((ScopeEntity) entity).getCall()){
//                        System.out.println(className2method.getCallMethodName());
//                        System.out.println(className2method.getDeclaringTypeQualifiedName());
//                        System.out.println(className2method.getParTypes());
//                        System.out.println(className2method.getBindVar());
                        int id = findMethodByType(className2method.getDeclaringTypeQualifiedName(), className2method.getCallMethodName(), className2method.getParTypes());
                        if (className2method.getBindVar() == -1 && !className2method.getBindVarName().equals("") && entity instanceof MethodEntity){
                            ((MethodEntity) entity).getCall().get(((MethodEntity) entity).getCall().indexOf(className2method)).setBindVar(findBindVar(entity.getId(), className2method.getBindVarName()));
                        }
                        if(id != -1){
                            saveRelation(entity.getId(), id, Configure.RELATION_CALL, Configure.RELATION_CALLED_BY, className2method.getLocation(), className2method.getBindVar(), className2method.getArguments());
                        }else {
                            int externalId = findExternalMethod(className2method.getDeclaringTypeQualifiedName(), className2method.getCallMethodName());
                            if (externalId != -1){
                                saveRelation(entity.getId(), externalId, Configure.RELATION_CALL, className2method.getLocation(), className2method.getBindVar(), className2method.getArguments());
                            }
                        }
                    }
                }
                //call non-dynamic
                if(!((ScopeEntity) entity).getCallNondynamic().isEmpty()){
                    int superClassId = -1;
                    String superClassName = "";
                    int superMethodId;
                    if(singleCollect.getEntityById(entity.getParentId()) instanceof ClassEntity){
                        superClassId = ((ClassEntity) singleCollect.getEntityById(entity.getParentId())).getSuperClassId();
                        superClassName = ((ClassEntity) singleCollect.getEntityById(entity.getParentId())).getSuperClassName();
                    }
                    if(superClassId != -1){
                        for(Tuple<String, Location> superMethodName :((MethodEntity) entity).getCallNondynamic()){
                            superMethodId = findMethodInSuper(superClassId, superMethodName.getL());
                            if(superMethodId != -1){
                                saveRelation(entity.getId(), superMethodId, Configure.RELATION_CALL_NON_DYNAMIC, Configure.RELATION_CALLBY_NON_DYNAMIC, superMethodName.getR());
                            }
                        }
                    } else {
                        //call extended external class method
                        if (!(entity instanceof MethodEntity)) {
                            continue;
                        }
                        for (Tuple<String, Location> superMethodName :((MethodEntity) entity).getCallNondynamic()){
                            superMethodId = findExternalMethod(superClassName, superMethodName.getL());
                            if(superMethodId != -1){
                                saveRelation(entity.getId(), superMethodId, Configure.RELATION_CALL_NON_DYNAMIC, Configure.RELATION_CALLBY_NON_DYNAMIC, superMethodName.getR());
                            }
                        }
                    }
                }
            }
        }
    }

    public int findMethodInSuper(int superId, String methodName){
        int methodId = -1;
        for(int id : singleCollect.getEntityById(superId).getChildrenIds()){
            if(singleCollect.getEntityById(id) instanceof MethodEntity
                    && singleCollect.getEntityById(id).getName().equals(methodName)){
                methodId = id;
                break;
            }
        }
        return methodId;
    }

    public int findMethodByType(String classQualifiedName, String methodName, ArrayList<String> paraTypes){
        int declaredClassId = -1;
//        String classQualifiedName = classQualifiedName2method.split("-")[0];
//        String methodName = classQualifiedName2method.split("-")[1];
        if (classQualifiedName.contains("<")){
            classQualifiedName = classQualifiedName.split("<")[0];
        }
        if(singleCollect.getCreatedType().containsKey(classQualifiedName)){
            declaredClassId = singleCollect.getCreatedType().get(classQualifiedName);
        }
        if(declaredClassId != -1){
            Integer id = iterateMethod(methodName, paraTypes, declaredClassId);
            if (id != null) return id;
            if(singleCollect.getEntityById(declaredClassId) instanceof ClassEntity){
                int superClassId = ((ClassEntity) singleCollect.getEntityById(declaredClassId)).getSuperClassId();
                while (superClassId != -1){
                    id = iterateMethod(methodName, paraTypes, superClassId);
                    if (id != null) return id;
                    superClassId = ((ClassEntity) singleCollect.getEntityById(superClassId)).getSuperClassId();
                }
            }
        }
        return -1;
    }

    @Nullable
    private Integer iterateMethod(String methodName, ArrayList<String> paraTypes, int superClassId) {
        for(int id : singleCollect.getEntityById(superClassId).getChildrenIds()){
            if (singleCollect.getEntityById(id) instanceof MethodEntity){
                if(singleCollect.getEntityById(id).getName().equals(methodName)
                        && (((MethodEntity) singleCollect.getEntityById(id)).isGenerics()
                        ||comparePara(((MethodEntity) singleCollect.getEntityById(id)).getParameterTypes(), paraTypes)) ){
                    return id;
                }
            }
        }
        return null;
    }

    public int findBindVar(int currentMethodId, String bindVarName){
        for(int id : singleCollect.getEntityById(singleCollect.getEntityById(currentMethodId).getParentId()).getChildrenIds()){
            if(singleCollect.getEntityById(id) instanceof VariableEntity
                    && bindVarName.equals(singleCollect.getEntityById(id).getName())){
                return id;
            }
        }
        return -1;
    }

    public int findExternalMethod(String className, String methName){
        int externalId = -1;
        String qualifiedName = className + "." + methName;
        for (String externalName : singleCollect.getThirdPartyAPIs().keySet()){
            if (externalName.contains(qualifiedName)){
                externalId = singleCollect.getThirdPartyAPIs().get(externalName);
            }
        }
        return externalId;
    }

    public static boolean comparePara(ArrayList<String> methParas, ArrayList<String> calledParas){
        if (calledParas == null){
            // external method
            return false;
        }
        if (methParas.size() == 0 && calledParas.size() == 0){
            return true;
        }
        else if (methParas.size() == calledParas.size()){
            boolean flag = false;
//                System.out.println(para);
//                System.out.println(this.para);
            for (String s : calledParas) {
                for (String value : methParas) {
                    if (s.contains(value)) {
                        flag = true;
                        break;
                    } else if ((s.equals("Integer") && value.equals("int")) || (value.equals("Integer") && s.equals("int"))){
                        flag = true;
                        break;
                    } else if ((s.equals("Boolean") && value.equals("boolean")) || (value.equals("Boolean") && s.equals("boolean"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Long") && value.equals("long")) || (value.equals("Long") && s.equals("long"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Byte") && value.equals("byte")) || (value.equals("Byte") && s.equals("byte"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("Character") && value.equals("char")) || (value.equals("Character") && s.equals("char"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("double") && value.equals("Double")) || (value.equals("double") && s.equals("Double"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("float") && value.equals("Float")) || (value.equals("float") && s.equals("Float"))) {
                        flag = true;
                        break;
                    } else if ((s.equals("short") && value.equals("Short")) || (value.equals("short") && s.equals("Short"))) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
            }
            return flag;
        }else {
            return false;
        }
    }



}
