package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.EnumEntity;
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
                           interfaceId = findTypeInPackage(tmp.getParentId(), interfaceName);
                       }
                   }
                   ((ClassEntity) entity).getInterfaces().replace(interfaceName, interfaceId);
                   if(interfaceId != -1){
                       saveRelation(entity.getId(), interfaceId, Configure.RELATION_IMPLEMENT, Configure.RELATION_IMPLEMENTED_BY);
                   }
                }
            }
            if(entity instanceof EnumEntity){
                int currentFileId = getCurrentFileId(entity.getId());
                BaseEntity tmp = singleCollect.getEntityById(currentFileId);
                for(String interfaceName : ((EnumEntity) entity).getInterfaces().keySet()){
                    if(tmp instanceof FileEntity){
                        interfaceId = findTypeInImport(interfaceName, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
                    }
                    if(interfaceId == -1){
                        interfaceId = findTypeInPackage(tmp.getParentId(), interfaceName);
                    }
                    if(interfaceId != -1){
                        ((EnumEntity) entity).getInterfaces().replace(interfaceName, interfaceId);
                        saveRelation(entity.getId(), interfaceId, Configure.RELATION_IMPLEMENT, Configure.RELATION_IMPLEMENTED_BY);
                    }
                }
            }
            interfaceId = -1;
        }
    }
}
