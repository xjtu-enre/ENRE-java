package visitor.deper;

import entity.BaseEntity;
import entity.MethodEntity;
import entity.VariableEntity;
import entity.properties.Location;
import org.apache.commons.io.input.NullInputStream;
import util.Configure;
import util.Tuple;

public class VarInfoBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity :singleCollect.getEntities()){
            if(entity instanceof VariableEntity){
                //set
                if(((VariableEntity) entity).getSetBy() != 0)
                    saveRelation(((VariableEntity) entity).getSetBy(),entity.getId(), Configure.RELATION_SET, Configure.RELATION_SETED_BY, entity.getLocation());
                //Typed
                if (singleCollect.getCreatedType().containsKey(entity.getRawType())){
                    saveRelation(entity.getId(), singleCollect.getCreatedType().get(entity.getRawType()), Configure.RELATION_TYPED, Configure.RELATION_TYPED_BY, entity.getLocation());
                }
            }
            //use & modify
            if(entity instanceof MethodEntity){
                for(String varName : ((MethodEntity) entity).getName2Usage().keySet()){
                    for(Tuple<String, Location> usage : ((MethodEntity) entity).getName2Usage().get(varName)){
                        if(usage.getL().equals("use")){
                            saveRelation(entity.getId(), ((MethodEntity) entity).getName2Id().get(varName), Configure.RELATION_USE, Configure.RELATION_USED_BY, usage.getR());
                        }
                        else if(usage.getL().equals("modify")){
                            try{
                                saveRelation(entity.getId(), ((MethodEntity) entity).getName2Id().get(varName), Configure.RELATION_MODIFY, Configure.RELATION_MODIFIED_BY, usage.getR());
                            } catch (NullPointerException e){
//                                System.out.println("NULL ID VAR: "+varName);
                            }
                        }
                    }
                }
            }
        }
    }
}
