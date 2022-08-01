package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.FileEntity;
import entity.ScopeEntity;
import entity.properties.Location;
import util.Configure;
import util.Tuple;

public class CastBf extends DepBackfill {

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if((entity instanceof ScopeEntity) && !((ScopeEntity) entity).getCasType().isEmpty()){
                for(Tuple<String, Location> castInfo : ((ScopeEntity) entity).getCasType()){
                    int typeId = -1;
                    if(castInfo.getL().contains(".")){
                        typeId = findTypeWithFullname(castInfo.getL());
                    }
                    else {
                        try{
                            BaseEntity tmp = singleCollect.getEntityById(getCurrentFileId(entity.getId()));
                            if(tmp instanceof FileEntity){
                                typeId = findTypeInImport(castInfo.getL(), ((FileEntity) tmp).getImportClass(), ((FileEntity) tmp).getImportOnDemand());
                            }

                            if(typeId == -1){
                                //this situation means the interface and class are in the same package
                                typeId = findTypeInPackage(tmp.getParentId(), castInfo.getL());
                            }
                        }
                        catch (IndexOutOfBoundsException e){
                            e.fillInStackTrace();
                        }
                    }
                    if(typeId != -1){
                        saveRelation(entity.getId(), typeId, Configure.RELATION_CAST, Configure.RELATION_CAST_BY, castInfo.getR());
                    }
                }
            }
        }
    }

}
