package TempOutput;

import entity.BaseEntity;
import entity.properties.Relation;
import formator.MapObject;
import util.Configure;
import util.SingleCollect;
import util.Tuple;

import java.util.HashMap;
import java.util.Map;

public class JsonMap {

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
    private Map<Integer, Map<Integer, Relation>> finalRes = new HashMap<Integer, Map<Integer, Relation>>();

    public Map<Integer, Map<Integer, Relation>> getFinalRes (){
        for(BaseEntity entity :singleCollect.getEntities()){
            for(Relation relation : entity.getRelation()){
                switch (relation.getKind()){
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
//                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CALL_NON_DYNAMIC));
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_CALL));
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
                    case Configure.RELATION_DEFINE:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_DEFINE));
                        break;
                    case Configure.RELATION_OVERRIDE:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_OVERRIDE));
                        break;
                    case Configure.RELATION_REFLECT:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation,Configure.RELATION_REFLECT));
                        break;
                    case Configure.RELATION_TYPED:
                        finalRes.put(entity.getId(),supplementRes(entity.getId(), relation, Configure.RELATION_TYPED));
                }
            }
        }
        return finalRes;
    }

    private Map<Integer, Relation> supplementRes(int entityId, Relation relation, String type){

        Map<Integer, Relation> dest = new HashMap<>();
        if(finalRes.containsKey(entityId)){
            dest = finalRes.get(entityId);
        }
        relation.setKind(type);
        dest.put(relation.getToEntity(),relation);

        return dest;
    }


}
