package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.properties.ReflectSite;
import util.Configure;

import java.util.ArrayList;
import java.util.HashMap;

public class ReflectBf extends DepBackfill{

    @Override
    public void setDep() {
        for (BaseEntity entity: singleCollect.getEntities()){
            if(!entity.getReflects().isEmpty()){
                for(ReflectSite reflect: entity.getReflects()){
                    //forname reflect class
                    if (reflect.getKind().equals(Configure.REFLECT_CLASS)){
                        int reflectClass = -1;
                        if(singleCollect.getCreatedType().containsKey(reflect.getReflectObj())){
                            reflectClass = singleCollect.getCreatedType().get(reflect.getReflectObj());
                            reflect.setReflectObjId(reflectClass);
                            saveRelation(entity.getId(), reflectClass, Configure.RELATION_REFLECT, Configure.RELATION_REFLECTED_BY);
                        }
                    }
                    //getMethod reflect method
                    else if (reflect.getKind().equals(Configure.REFLECT_METHOD)){
                        String[] args = reflect.getArguments();
                        String refMethName = reflect.getReflectObj();
                        OverrideBf.innerMeth reflectMeth = new OverrideBf.innerMeth(refMethName, null);
                        for(int i=1; i<args.length; i++){
                            if(args[i].equals("null")){
                                break;
                            }
                            else {
                                reflectMeth.addPara(args[i].split("\\.")[0]);
                            }
                        }
                        if (findReflectMethodClass(entity.getReflects(), reflect.getBindVar()) != -1){
                            HashMap<OverrideBf.innerMeth, Integer> reflectMeths = getInnerMeth((ClassEntity) singleCollect.getEntityById(findReflectMethodClass(entity.getReflects(), reflect.getBindVar())));
                            int reflectId = -1;
                            for (OverrideBf.innerMeth classMeth : reflectMeths.keySet()){
                                if (reflectMeth.getName().equals(classMeth.getName()) && classMeth.comparePara(reflectMeth.para)){
                                    reflectId = reflectMeths.get(classMeth);
                                    break;
                                }
                            }
                            if(reflectId != -1){
                                saveRelation(entity.getId(), reflectId, Configure.RELATION_REFLECT, Configure.RELATION_REFLECTED_BY, reflect.getInvoke(), reflect.getModifyAccessible(), true);
                            }
                        }
                    }
                }
            }
        }
    }

    public int findReflectMethodClass(ArrayList<ReflectSite> reflectSites, int refBindVar){
        for (ReflectSite reflectSite : reflectSites){
            if (reflectSite.getKind().equals(Configure.REFLECT_CLASS) && reflectSite.getImplementVar() == refBindVar){
                return reflectSite.getReflectObjId();
            }
        }
        return -1;
    }

}
