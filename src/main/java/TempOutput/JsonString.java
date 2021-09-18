package TempOutput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import entity.BaseEntity;
import util.SingleCollect;

public class JsonString {

    public static String JSONWriteRelation(Map<Integer, Map<Integer, Map<String, Integer>>> relationMap) throws Exception {

        JSONObject obj=new JSONObject();//创建JSONObject对象

        obj.put("schemaVersion","1");
        Iterator<BaseEntity> iterator = SingleCollect.getSingleCollectInstance().getEntities().iterator();

        List<String> subObjVariable=new ArrayList<String>();//创建对象数组里的子对象
        while(iterator.hasNext()) {
            BaseEntity entity = iterator.next();
            subObjVariable.add(entity.getQualifiedName());
        }
        obj.put("variables",subObjVariable);


        for(int fromEntity:relationMap.keySet()) {
            for(int toEntity:relationMap.get(fromEntity).keySet()) {
                for(String type:relationMap.get(fromEntity).get(toEntity).keySet()) {
                    JSONObject subObj=new JSONObject();//创建对象数组里的子对象
                    subObj.put("src",fromEntity);
                    subObj.put("dest",toEntity);

                    JSONObject reObj=new JSONObject();//创建对象数组里的子对象
                    reObj.put(type, 1);
                    subObj.accumulate("values",reObj);
                    obj.accumulate("cells",subObj);

                }

            }
        }

        return obj.toString();
    }

    public static String JSONWriteEntity(List<BaseEntity> entityList) throws Exception {


        JSONObject obj=new JSONObject();//创建JSONObject对象

        obj.put("schemaVersion","1");

        List<String> subObjVariable=new ArrayList<String>();//创建对象数组里的子对象
        for(BaseEntity en:entityList) {
            subObjVariable.add(en.toString());
        }
        obj.put("variables",subObjVariable);

        return obj.toString();
    }

}
