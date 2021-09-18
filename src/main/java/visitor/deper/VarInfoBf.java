package visitor.deper;

import entity.BaseEntity;
import entity.MethodEntity;
import entity.VariableEntity;
import util.Configure;

public class VarInfoBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity :singleCollect.getEntities()){
            //set
            if(entity instanceof VariableEntity){
                if(((VariableEntity) entity).getSetBy() != 0)
                    saveRelation(entity.getId(),((VariableEntity) entity).getSetBy(), Configure.RELATION_SET, Configure.RELATION_SETED_BY);
            }
            //use & modify
            if(entity instanceof MethodEntity){
                for(String varName : ((MethodEntity) entity).getName2Usage().keySet()){
                    for(String usage : ((MethodEntity) entity).getName2Usage().get(varName)){
                        if(usage.equals("use")){
                            saveRelation(((MethodEntity) entity).getName2Id().get(varName), entity.getId(), Configure.RELATION_USE, Configure.RELATION_USED_BY);
                        }
                        else if(usage.equals("modify")){
                            saveRelation(((MethodEntity) entity).getName2Id().get(varName), entity.getId(), Configure.RELATION_MODIFY, Configure.RELATION_MODIFIED_BY);
                        }
                    }
                }
            }
        }
    }

}
