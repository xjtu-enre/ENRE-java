package TempOutput;

import entity.BaseEntity;
import entity.properties.Relation;
import formator.MapObject;
import util.Configure;
import util.SingleCollect;
import util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonMap {

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
    private Map<Integer, ArrayList<Tuple<Integer, Relation>>> finalRes = new HashMap<>();

    public Map<Integer, ArrayList<Tuple<Integer, Relation>>> getFinalRes (){
        for(BaseEntity entity :singleCollect.getEntities()){
            for(Relation relation : entity.getRelation()){

                if(!finalRes.containsKey(entity.getId())){
                    finalRes.put(entity.getId(),new ArrayList<>());
                }
                finalRes.get(entity.getId()).add(new Tuple<Integer, Relation>(relation.getToEntity(),relation));


                }
            }
        return finalRes;
    }


}
