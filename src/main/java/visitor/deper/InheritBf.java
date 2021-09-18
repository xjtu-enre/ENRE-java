package visitor.deper;

import entity.*;
import util.Configure;

import java.util.ArrayList;

public class InheritBf extends DepBackfill{

    @Override
    public void setDep() {
        int superId = -1;
        String superName;
        ArrayList<Integer> extendsIds = new ArrayList<Integer>();
        ArrayList<String> extendsNames;
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ClassEntity && ((ClassEntity) entity).getSuperClassName()!=null){
                superName = ((ClassEntity) entity).getSuperClassName();
                if(superName.contains(".")){
                    //qualified name
                    superId = findTypeWithFullname(superName);
                } else {
                    BaseEntity tmp = singleCollect.getEntityById(entity.getParentId());
                    if(tmp instanceof FileEntity){
                        superId = findTypeInImport(superName, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
                    }
                }
                ((ClassEntity) entity).setSuperClassId(superId);
                if(superId != -1){
                    saveRelation(entity.getId(), superId, Configure.RELATION_INHERIT, Configure.RELATION_INHERITED_BY);
                }
            }
            else if(entity instanceof InterfaceEntity && ((InterfaceEntity) entity).getExtendsNames().size()!=0){
                extendsNames = ((InterfaceEntity) entity).getExtendsNames();
                for(String name : extendsNames){
                    if (name.contains(".")){
                        extendsIds.add(findTypeWithFullname(name));
                    } else {
                        BaseEntity tmp = singleCollect.getEntityById(entity.getParentId());
                        if(tmp instanceof FileEntity){
                            extendsIds.add(findTypeInImport(name, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand()));
                        }
                    }
                }
               ((InterfaceEntity) entity).addExtendsIds(extendsIds);
                for(int id : extendsIds){
                    if(id != -1){
                        saveRelation(entity.getId(), id, Configure.RELATION_INHERIT, Configure.RELATION_INHERITED_BY);
                    }
                }
            }
        }
    }
}
