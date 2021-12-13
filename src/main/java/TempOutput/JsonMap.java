package TempOutput;

import entity.BaseEntity;
import formator.MapObject;
import util.Configure;
import util.SingleCollect;
import util.Tuple;

import java.util.HashMap;
import java.util.Map;

public class JsonMap {

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
    private Map<Integer, Map<Integer, Map<String, Integer>>> finalRes = new HashMap<Integer, Map<Integer, Map<String, Integer>>>();

    public Map<Integer, Map<Integer, Map<String, Integer>>> getFinalRes (){
        for(BaseEntity entity :singleCollect.getEntities()){
            for(Tuple relation : entity.getRelation()){
                switch ((String)relation.getRelation()){
                    case Configure.RELATION_IMPORT:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_IMPORT));
                        break;
                    case Configure.RELATION_INHERIT:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_INHERIT));
                        break;
                    case Configure.RELATION_PARAMETER:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_PARAMETER));
                        break;
                    case Configure.RELATION_CALL:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CALL));
                        break;
                    case Configure.RELATION_CALL_NON_DYNAMIC:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CALL_NON_DYNAMIC));
                        break;
                    case Configure.RELATION_IMPLEMENT:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_IMPLEMENT));
                        break;
                    case Configure.RELATION_SET:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_SET));
                        break;
                    case Configure.RELATION_USE:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_USE));
                        break;
                    case Configure.RELATION_MODIFY:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_MODIFY));
                        break;
                    case Configure.RELATION_CAST:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CAST));
                        break;
                    case Configure.RELATION_ANNOTATE:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_ANNOTATE));
                        break;
                    case Configure.RELATION_CONTAIN:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CONTAIN));
                        break;
                }
            }
        }
        return finalRes;
    }

    private Map<Integer, Map<String, Integer>> supplementRes(int entityId, Tuple relation, String type){
        Map<String, Integer> relationType = new HashMap<String, Integer>();
        relationType.put(type, 1);

        Map<Integer, Map<String, Integer>> dest = new HashMap<>();
        if(finalRes.containsKey(entityId)){
            dest = finalRes.get(entityId);
        }
        dest.put((Integer) relation.getId(),relationType);

        return dest;
    }


}
