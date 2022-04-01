package formator.spreadsheet;

import entity.*;
import entity.properties.Relation;
import util.Configure;
import util.Tuple;
import util.SingleCollect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Csvgrapher {
    private SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();
    private ArrayList<String[]> nodes = new ArrayList<String[]>();
    private ArrayList<String[]> edges = new ArrayList<String[]>();

    /**
     * store all nodes and deps from singlecollect into nodes and edges
     */
    public void buildProcess() {
        processNodes();
        processDeps(transDep2Map());
    }

    /**
     * id, name, type, parentId
     */
    private void processNodes() {
        nodes.add(new String[] {"Id", "type", "label", "Parent"});
        for (BaseEntity entity : singleCollect.getEntities()) {
            String id = Integer.toString(entity.getId());
            String type = getEntityType(entity);
            String name = entity.getName();
            String parentId = Integer.toString(entity.getParentId());
            if(type.equals("Variable") && entity.getParentId() == -1) {
                continue;
            }
            String[] row = new String[]{id,type,name, parentId};
            nodes.add(row);
        }
    }


    //src, dst, deptype, primitivetype, weight
    private Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>>  transDep2Map() {
        Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps = new HashMap<Integer, Map<Integer, Map<String, Map<String, Integer>>>>();
        for (BaseEntity entity : singleCollect.getEntities()) {
            int id1 = entity.getId();
            for (Relation re : entity.getRelation()) {
                int id2 = re.getToEntity();
                String pritiveType = re.getKind();
                if(!(pritiveType.equals(Configure.RELATION_IMPORT)
                        || pritiveType.equals(Configure.RELATION_INHERIT)
                        || pritiveType.equals(Configure.RELATION_IMPLEMENT)
                        || pritiveType.equals(Configure.RELATION_PARAMETER)
                        || pritiveType.equals(Configure.RELATION_CALL)
                        || pritiveType.equals(Configure.RELATION_RETURN)
                        || pritiveType.equals(Configure.RELATION_USE)
                        || pritiveType.equals(Configure.RELATION_SET)
//                        || pritiveType.equals(Configure.RELATION_IMPLICIT_INTERNAL_CALL)
//                        || pritiveType.equals(Configure.RELATION_IMPLICIT_EXTERNAL_CALL)
                         ))
                {
                    continue;
                }
                String visibleType= getVisibility(pritiveType);
                if(!deps.containsKey(id1)) {
                    deps.put(id1, new HashMap<Integer, Map<String, Map<String, Integer>>>());
                }
                if(!deps.get(id1).containsKey(id2)) {
                    deps.get(id1).put(id2, new HashMap<String, Map<String, Integer>>());
                }
                if(!deps.get(id1).get(id2).containsKey(visibleType)) {
                    deps.get(id1).get(id2).put(visibleType, new HashMap<String, Integer>());
                }
                if(!deps.get(id1).get(id2).get(visibleType).containsKey(pritiveType)) {
                    deps.get(id1).get(id2).get(visibleType).put(pritiveType, 0);
                }
                int oldweight = deps.get(id1).get(id2).get(visibleType).get(pritiveType);
                deps.get(id1).get(id2).get(visibleType).put(pritiveType, oldweight + 1);
            }
        }
        return deps;
    }

    private String getVisibility(String pritiveType) {
        if(pritiveType.equals(Configure.RELATION_IMPORT)
                || pritiveType.equals(Configure.RELATION_INHERIT)
                || pritiveType.equals(Configure.RELATION_IMPLEMENT)
                || pritiveType.equals(Configure.RELATION_PARAMETER)
                || pritiveType.equals(Configure.RELATION_CALL)
                || pritiveType.equals(Configure.RELATION_RETURN)
                || pritiveType.equals(Configure.RELATION_USE)
                || pritiveType.equals(Configure.RELATION_SET)
        ){
          return "Explicit";
        }
//        if(pritiveType.equals(Configure.RELATION_IMPLICIT_INTERNAL_CALL)){
//            return "Implicit internal";
//        }
//        if(pritiveType.equals(Configure.RELATION_IMPLICIT_EXTERNAL_CALL)) {
//            return "Implicit external";
//        }
        return "";
    }

    private String getEntityType(BaseEntity entity) {
        String type="";
        if(entity instanceof PackageEntity) {
            return "Package";
        }
        if(entity instanceof FileEntity) {
            return "File";
        }
        if(entity instanceof ClassEntity) {
            return "Class";
        }
        if(entity instanceof MethodEntity) {
            return "Method";
        }
        if(entity instanceof VariableEntity) {
            return "Variable";
        }
        return type;
    }



    //src, dst, deptype, primitivetype, weight
    private void processDeps(Map<Integer, Map<Integer, Map<String, Map<String, Integer>>>> deps) {
        edges.add(new String[]{"Source", "Target", "VisibleType", "PrimitiveType", "Weight"});
        for (Map.Entry<Integer, Map<Integer, Map<String, Map<String, Integer>>>> entry1 : deps.entrySet()) {
            int src = entry1.getKey();
            for (Map.Entry<Integer, Map<String, Map<String, Integer>>> entry2 : entry1.getValue().entrySet()) {
                int dst = entry2.getKey();
                for (Map.Entry<String, Map<String, Integer>> entry3 : entry2.getValue().entrySet()) {
                    String depType = entry3.getKey();
                    for (Map.Entry<String, Integer> entry4 : entry3.getValue().entrySet()) {
                        String primitiveType = entry4.getKey();
                        int weight = entry4.getValue();
                        String arr[] = new String[]{Integer.toString(src),
                                Integer.toString(dst), depType, primitiveType, Integer.toString(weight)};
                        edges.add(arr);
                    }
                }
            }
        }
    }


    public ArrayList<String[]> getNodes() {
        return nodes;
    }

    public ArrayList<String[]> getEdges() {
        return edges;
    }
}


