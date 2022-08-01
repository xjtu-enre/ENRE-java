package visitor.deper;

import entity.*;
import util.Configure;

import java.util.ArrayList;

public class InheritBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ClassEntity && ((ClassEntity) entity).getSuperClassName()!=null){
                int superId = -1;
                String superName;
                superName = ((ClassEntity) entity).getSuperClassName();
                if (superName.contains("<")){
                    superName = superName.split("<")[0];
                }
                if (singleCollect.getCreatedType().containsKey(superName)){
                    superId = singleCollect.getCreatedType().get(superName);
                }
//                if(superName.contains(".")){
//                    //qualified name
//                    superId = findTypeWithFullname(superName);
//                } else {
//                    BaseEntity tmp = singleCollect.getEntityById(entity.getParentId());
//                    if(tmp instanceof FileEntity){
//                        superId = findTypeInImport(superName, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
//                    }
//                }
                ((ClassEntity) entity).setSuperClassId(superId);
                if(superId != -1){
                    saveRelation(entity.getId(), superId, Configure.RELATION_INHERIT, Configure.RELATION_INHERITED_BY, entity.getLocation());
                }
            }
            else if(entity instanceof InterfaceEntity && ((InterfaceEntity) entity).getExtendsNames().size()!=0){
                ArrayList<Integer> extendsIds = new ArrayList<Integer>();
                ArrayList<String> extendsNames;
                extendsNames = ((InterfaceEntity) entity).getExtendsNames();
                for(String name : extendsNames){
                    if (name.contains("<")){
                        name = name.split("<")[0];
                    }
                    if (singleCollect.getCreatedType().containsKey(name)){
                        extendsIds.add(singleCollect.getCreatedType().get(name));
                    }

//                    if (name.contains(".")){
//                        extendsIds.add(findTypeWithFullname(name));
//                    } else {
//                        BaseEntity tmp = singleCollect.getEntityById(entity.getParentId());
//                        if(tmp instanceof FileEntity){
//                            extendsIds.add(findTypeInImport(name, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand()));
//                        }
//                    }
                }
               ((InterfaceEntity) entity).addExtendsIds(extendsIds);
                for(int id : extendsIds){
                    if(id != -1){
                        saveRelation(entity.getId(), id, Configure.RELATION_INHERIT, Configure.RELATION_INHERITED_BY, entity.getLocation());
                    }
                }
            }
        }
    }
}
