package visitor.deper;

import entity.BaseEntity;
import entity.MethodEntity;
import entity.VariableEntity;
import util.Configure;

public class VarInfoBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity :singleCollect.getEntities()){
            if(entity instanceof VariableEntity){
                //set
                if(((VariableEntity) entity).getSetBy() != 0)
                    saveRelation(((VariableEntity) entity).getSetBy(),entity.getId(), Configure.RELATION_SET, Configure.RELATION_SETED_BY);
                //Typed
                if (singleCollect.getCreatedType().containsKey(entity.getRawType())){
                    saveRelation(entity.getId(), singleCollect.getCreatedType().get(entity.getRawType()), Configure.RELATION_TYPED, Configure.RELATION_TYPED_BY);
                }
            }
            //use & modify
            if(entity instanceof MethodEntity){
                for(String varName : ((MethodEntity) entity).getName2Usage().keySet()){
                    for(String usage : ((MethodEntity) entity).getName2Usage().get(varName)){
                        if(usage.equals("use")){
                            saveRelation(entity.getId(), ((MethodEntity) entity).getName2Id().get(varName), Configure.RELATION_USE, Configure.RELATION_USED_BY);
                        }
                        else if(usage.equals("modify")){
                            saveRelation(entity.getId(), ((MethodEntity) entity).getName2Id().get(varName), Configure.RELATION_MODIFY, Configure.RELATION_MODIFIED_BY);
                        }
                    }
                }
            }
        }
    }

}
