package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.FileEntity;
import entity.ScopeEntity;
import util.Configure;

public class CastBf extends DepBackfill {

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if((entity instanceof ScopeEntity) && !((ScopeEntity) entity).getCasType().isEmpty()){
                for(String castype : ((ScopeEntity) entity).getCasType()){
                    int typeId = -1;
                    if(castype.contains(".")){
                        typeId = findTypeWithFullname(castype);
                    }
                    else {
                        try{
                            BaseEntity tmp = singleCollect.getEntityById(getCurrentFileId(entity.getId()));
                            if(tmp instanceof FileEntity){
                                typeId = findTypeInImport(castype, ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
                            }

                            if(typeId == -1){
                                //this situation means the interface and class are in the same package
                                typeId = findTypeInPackage(tmp.getParentId(), castype);
                            }
                        }
                        catch (IndexOutOfBoundsException e){
                            e.fillInStackTrace();
                        }
                    }
                    if(typeId != -1){
                        saveRelation(entity.getId(), typeId, Configure.RELATION_CAST, Configure.RELATION_CAST_BY);
                    }
                }
            }
        }
    }

}
