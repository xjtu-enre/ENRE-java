package visitor.deper;

import entity.BaseEntity;
import entity.TypeEntity;
import entity.FileEntity;
import entity.PackageEntity;
import util.Configure;

public class ImportBf extends DepBackfill {

    @Override
    public void setDep() {
        int tmpId = -1;
        for(BaseEntity entity : singleCollect.getEntities()){
            if (entity instanceof FileEntity){
                for (String pkg : ((FileEntity) entity).getImportOnDemand().keySet()){
                    if(singleCollect.getCreatedPackage().containsKey(pkg)){
                        tmpId = singleCollect.getCreatedPackage().get(pkg);
                        ((FileEntity) entity).getImportOnDemand().replace(pkg, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof PackageEntity){
                            ((PackageEntity) singleCollect.getEntityById(tmpId)).addImportBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY);
                    }
                }

                for (String file : ((FileEntity) entity).getImportClass().keySet()){
                    tmpId = findTypeWithFullname(file);
                    if(tmpId != -1){
                        ((FileEntity) entity).getImportClass().replace(file, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof TypeEntity){
                            ((TypeEntity) singleCollect.getEntityById(tmpId)).addImportedBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY);
                    }
                }
            }
        }
    }

//superclass.isEqualTo(cu.getAST().resolveWellKnownType("java.lang.Object")) need to supplement

}
