package visitor.deper;

import entity.BaseEntity;
import entity.MethodEntity;
import util.Configure;

import java.util.ArrayList;
import java.util.Iterator;

public class ParametersBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof MethodEntity){
                ArrayList<Integer> paras = ((MethodEntity) entity).getParameters();
                if(!paras.isEmpty()){
                    for(Integer para :paras){
                        saveRelation(entity.getId(),para,Configure.RELATION_PARAMETER,Configure.RELATION_PARAMETERED_BY, singleCollect.getEntityById(para).getLocation());
                    }
                }
            }
        }
    }
}
