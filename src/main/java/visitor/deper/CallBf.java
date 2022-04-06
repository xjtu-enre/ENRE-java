package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.MethodEntity;
import entity.ScopeEntity;
import entity.properties.CallSite;
import entity.properties.Location;
import util.Configure;
import util.Tuple;

public class CallBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ScopeEntity){
                //call
                if(!((ScopeEntity) entity).getCall().isEmpty()){
                    for(CallSite className2method : ((ScopeEntity) entity).getCall()){
                        int id = findMethodByType(className2method.getDeclaringTypeQualifiedName(), className2method.getCallMethodName());
                        if(id != -1){
                            saveRelation(entity.getId(), id, Configure.RELATION_CALL, Configure.RELATION_CALLED_BY, className2method.getLocation(), className2method.getBindVar());
                        }
                    }
                }
                //call non-dynamic
                if(!((ScopeEntity) entity).getCallNondynamic().isEmpty()){
                    int superClassId = -1;
                    int superMethodId;
                    if(singleCollect.getEntityById(entity.getParentId()) instanceof ClassEntity){
                        superClassId = ((ClassEntity) singleCollect.getEntityById(entity.getParentId())).getSuperClassId();
                    }
                    if(superClassId != -1){
                        for(Tuple<String, Location> superMethodName :((MethodEntity) entity).getCallNondynamic()){
                            superMethodId = findMethodInSuper(superClassId, superMethodName.getL());
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

    public int findMethodByType(String classQualifiedName, String methodName){
        int declaredClassId = -1;
//        String classQualifiedName = classQualifiedName2method.split("-")[0];
//        String methodName = classQualifiedName2method.split("-")[1];
        if(singleCollect.getCreatedType().containsKey(classQualifiedName)){
            declaredClassId = singleCollect.getCreatedType().get(classQualifiedName);
        }
        if(declaredClassId != -1){
            for(int id : singleCollect.getEntityById(declaredClassId).getChildrenIds()){
                if(singleCollect.getEntityById(id).getName().equals(methodName)){
                    return id;
                }
            }
        }
        return -1;
    }


}
