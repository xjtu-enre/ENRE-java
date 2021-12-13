package visitor.deper;

import entity.BaseEntity;
import util.Configure;

public class ContainBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity: singleCollect.getEntities()){
            if(!entity.getChildrenIds().isEmpty()){
                for(int childId:entity.getChildrenIds()){
                    saveRelation(entity.getId(), childId, Configure.RELATION_CONTAIN, Configure.RELATION_CONTAINED_BY);
                }
            }
        }
    }
}
