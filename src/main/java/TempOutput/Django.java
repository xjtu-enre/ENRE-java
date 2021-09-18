package TempOutput;

import entity.BaseEntity;
import org.json.JSONObject;
import util.Configure;
import util.SingleCollect;

import java.util.*;

public class Django {

    public static String nodeWriter(){
        JSONObject obj=new JSONObject(new LinkedHashMap<>());//创建JSONObject对象
        Iterator<BaseEntity> iterator = SingleCollect.getSingleCollectInstance().getEntities().iterator();
        List<JSONObject> var=new ArrayList<JSONObject>();//创建对象数组里的子对象
        while(iterator.hasNext()) {
            BaseEntity entity = iterator.next();
            JSONObject subObj=new JSONObject(new LinkedHashMap<>());//创建对象数组里的子对象
            String _key = String.valueOf(entity.getId());
            subObj.put("_key",_key);
            subObj.put("name",entity.getQualifiedName());
            var.add(subObj);
        }
        obj.put("variables",var);

        return obj.toString();
    }

    public static String edgeWriter(Map<Integer, Map<Integer, Map<String, Integer>>> relationMap){
        JSONObject obj=new JSONObject(new LinkedHashMap<>());//创建JSONObject对象
        String project_name = Configure.getConfigureInstance().getAnalyzedProjectName();
        for(int fromEntity:relationMap.keySet()) {
            for(int toEntity:relationMap.get(fromEntity).keySet()) {
                for(String type:relationMap.get(fromEntity).get(toEntity).keySet()) {
                    JSONObject subObj=new JSONObject(new LinkedHashMap<>());//创建对象数组里的子对象
                    String _from = project_name+"/"+fromEntity;
                    String _to = project_name+"/"+toEntity;
                    subObj.put("_from",_from);
                    subObj.put("_to",_to);

                    JSONObject reObj=new JSONObject(new LinkedHashMap<>());//创建对象数组里的子对象
                    reObj.put(type, 1);
                    subObj.accumulate("values",reObj);
                    obj.accumulate("cells",subObj);

                }

            }
        }

        return obj.toString();
    }
}
