package visitor.deper;

import entity.*;
import util.Configure;

public class ContainDefineBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if((entity instanceof PackageEntity || entity instanceof FileEntity) && !entity.getChildrenIds().isEmpty()) {
                for(int id : entity.getChildrenIds()){
                    saveRelation(entity.getId(), id, Configure.RELATION_CONTAIN, Configure.RELATION_CONTAINED_BY);
                }
            }else if((entity instanceof TypeEntity || entity instanceof MethodEntity) && !entity.getChildrenIds().isEmpty()){
                for(int id :entity.getChildrenIds()){
                    saveRelation(entity.getId(), id, Configure.RELATION_DEFINE, Configure.RELATION_DEFINED_BY);
                }
            }
        }
    }
}
