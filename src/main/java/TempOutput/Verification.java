package TempOutput;

import entity.BaseEntity;
import entity.FileEntity;
import entity.properties.Relation;
import org.json.JSONObject;
import util.Configure;
import util.SingleCollect;
import util.Tuple;

import java.util.*;

public class Verification {
    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    /**
     * import
     * @return
     */
    public HashMap<String, HashSet<String>> getRela(){
        HashMap<String, HashSet<String>> importRela = new HashMap<>();

        for(BaseEntity entity : singleCollect.getEntities()){
            HashSet<String> imports = new HashSet<>();
            if(entity instanceof FileEntity){
                for(Relation relation : entity.getRelation()) {
                    if (relation.getKind().equals(Configure.RELATION_IMPORT)){
                        imports.add(singleCollect.getEntityById(relation.getToEntity()).getQualifiedName());
                    }
                }
                if(!imports.isEmpty()){
                    importRela.put(((FileEntity) entity).getFullPath(), imports);
                }
             }
        }
        return importRela;
    }

    public static String JSONWriteRela(HashMap<String, HashSet<String>> rela) throws Exception {

        JSONObject obj=new JSONObject();//创建JSONObject对象

        for(String filename : rela.keySet()){
            JSONObject onecell = new JSONObject();
            onecell.put("src", filename);
            //创建对象数组里的子对象
            List<String> dest = new ArrayList<String>(rela.get(filename));
            onecell.put("dest", dest);
            obj.accumulate("cells", onecell);
        }

        return obj.toString();
    }

}
