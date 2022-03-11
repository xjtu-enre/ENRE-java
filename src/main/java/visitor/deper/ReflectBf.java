package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import util.Configure;

import java.util.ArrayList;
import java.util.HashMap;

public class ReflectBf extends DepBackfill{

    @Override
    public void setDep() {
        for (BaseEntity entity: singleCollect.getEntities()){
            if(!entity.getReflects().isEmpty()){
                int reflectClass = -1;
                for(String reflect: entity.getReflects()){
                    //forname reflect class
                    if(singleCollect.getCreatedType().containsKey(reflect)){
                        reflectClass = singleCollect.getCreatedType().get(reflect);
                        saveRelation(entity.getId(), reflectClass, Configure.RELATION_REFLECT, Configure.RELATION_REFLECTED_BY);
                    }
                    //getMethod reflect method
                    else if (reflect.contains(",") && reflectClass != -1){
                        String[] args = reflect.replace("]", "").split(", ");
                        String refMethName = args[0].replace("\"", "").substring(1);
                        OverrideBf.innerMeth reflectMeth = new OverrideBf.innerMeth(refMethName, null);
                        for(int i=1; i<args.length; i++){
                            if(args[i].equals("null")){
                                break;
                            }
                            else {
                                reflectMeth.addPara(args[i].split("\\.")[0]);
                            }
                        }
                        HashMap<OverrideBf.innerMeth, Integer> reflectMeths = getInnerMeth((ClassEntity) singleCollect.getEntityById(reflectClass));
                        int reflectId = -1;
                        for (OverrideBf.innerMeth classMeth : reflectMeths.keySet()){
                            if (reflectMeth.getName().equals(classMeth.getName()) && classMeth.comparePara(reflectMeth.para)){
                                reflectId = reflectMeths.get(classMeth);
                                break;
                            }
                        }
                        if(reflectId != -1){
                            saveRelation(entity.getId(), reflectId, Configure.RELATION_REFLECT, Configure.RELATION_REFLECTED_BY);
                        }
                    }
                }
            }
        }
    }
}
