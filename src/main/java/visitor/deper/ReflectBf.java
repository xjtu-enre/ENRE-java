package visitor.deper;

import entity.BaseEntity;
import util.Configure;

public class ReflectBf extends DepBackfill{
    @Override
    public void setDep() {
        for (BaseEntity entity: singleCollect.getEntities()){
            if(!entity.getReflects().isEmpty()){
                for(String name: entity.getReflects()){
                    if(singleCollect.getCreatedType().containsKey(name)){
                        int name2Id = singleCollect.getCreatedType().get(name);
                        saveRelation(entity.getId(), name2Id, Configure.RELATION_REFLECT, Configure.RELATION_REFLECTED_BY);
                    }
                }
            }
        }
    }
}
