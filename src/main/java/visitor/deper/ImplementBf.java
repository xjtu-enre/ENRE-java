package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.FileEntity;
import util.Configure;

public class ImplementBf extends DepBackfill{

    @Override
    public void setDep() {
        int interfaceId = -1;
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ClassEntity){
                for (String interfaceName : ((ClassEntity) entity).getInterfaces().keySet()){
                   if(interfaceName.contains(".")) {
                       interfaceId = findTypeWithFullname(interfaceName);
                   }
                   else {
                        BaseEntity tmp = singleCollect.getEntityById(entity.getParentId());
                        if(tmp instanceof FileEntity){
                            interfaceId = findTypeInImport(interfaceName, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
                        }

                       if( interfaceId == -1){
                           //this situation means the interface and class are in the same package
                           interfaceId = findTypeInPackage(tmp, interfaceName);
                       }
                   }
                   ((ClassEntity) entity).getInterfaces().replace(interfaceName, interfaceId);
                   if(interfaceId != -1){
                       saveRelation(entity.getId(), interfaceId, Configure.RELATION_IMPLEMENT, Configure.RELATION_IMPLEMENTED_BY);
                   }
                }
            }
            interfaceId = -1;
        }
    }


    public int findTypeInPackage (BaseEntity entity, String interfaceName){
        int packageId = entity.getParentId();
        int interfaceId = -1;
        if(packageId != -1){
            for(int id : singleCollect.getEntityById(packageId).getChildrenIds()){
                if(singleCollect.isFile(id)){
                    for (int childId : singleCollect.getEntityById(id).getChildrenIds()) {
                        if (singleCollect.getEntityById(childId).getName().equals(interfaceName)) {
                            interfaceId = childId;
                            break;
                        }
                    }
                }
                if(interfaceId != -1)
                    break;
            }
        } else {
            for(BaseEntity baseEntity :singleCollect.getEntities()){
                if(baseEntity instanceof FileEntity && baseEntity.getParentId() == -1){
                    for(int childId : baseEntity.getChildrenIds()){
                        if(singleCollect.getEntityById(childId).getName().equals(interfaceName)){
                            interfaceId = childId;
                        }
                    }
                }
                if (interfaceId != -1)
                    break;
            }
        }
        return interfaceId;
    }
}
