package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.MethodEntity;
import entity.VariableEntity;
import util.Configure;

import java.util.*;

public class OverrideBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity : singleCollect.getEntities()){
            if(entity instanceof ClassEntity){
                if(((ClassEntity) entity).getSuperClassId() != -1){
                    HashMap<innerMeth, Integer> superMeths = getInnerMeth((ClassEntity) singleCollect.getEntityById(((ClassEntity) entity).getSuperClassId()));
                    for(int childId: entity.getChildrenIds()){
                        int overrideMeth = checkOverride(childId, superMeths);
                        if (overrideMeth != -1){
                            saveRelation( childId, overrideMeth, Configure.RELATION_OVERRIDE, Configure.RELATION_OVERRIDE_BY);
                        }
                    }
                }
            }
        }
    }

    protected static class innerMeth {
        String name;
        String returnType;
        ArrayList<String> para = new ArrayList<>();

        innerMeth(String name, String returnType){
            this.name = name;
            this.returnType = returnType;
        }

        String getName(){
            return this.name;
        }

        void setName(String name){
            this.name = name;
        }

        void setReturnType(String returnType){
            this.returnType = returnType;
        }

        String getReturnType(){
            return this.returnType;
        }

        void addPara(String paraType){
            this.para.add(paraType);
        }

        boolean comparePara(List<String> para){
            if (para.size() == 0 && this.para.size() == 0){
                return true;
            }
            else if (para.size() == this.para.size()){
                boolean flag = false;
                for (String s : this.para) {
                    for (String value : para) {
                        if (s.equals(value)) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                }
                return flag;
            }else {
                return false;
            }
        }
    }

    /**
     * check whether current method overrides super
     * @param MethId current method id
     * @param superMeths super class's method
     * @return if override, return super method id, else -1
     */
    protected int checkOverride(int MethId, HashMap<OverrideBf.innerMeth, Integer> superMeths){
        if(singleCollect.getEntityById(MethId) instanceof MethodEntity){
            MethodEntity currentMeth = (MethodEntity) singleCollect.getEntityById(MethId);
            ArrayList<String> currentParas = new ArrayList<>();
            for (int paraId : currentMeth.getParameters()){
                currentParas.add(singleCollect.getEntityById(paraId).getRawType());
            }
            for (OverrideBf.innerMeth superMeth : superMeths.keySet()){
                if (currentMeth.getName().equals(superMeth.getName())){
                    if(currentMeth.getReturnType()==null && superMeth.getReturnType()==null){
                        if(superMeth.comparePara(currentParas)){
                            return superMeths.get(superMeth);
                        }
                    }else if(currentMeth.getReturnType().equals(superMeth.getReturnType())){
                        if(superMeth.comparePara(currentParas)){
                            return superMeths.get(superMeth);
                        }
                    }
                }
            }
        }
        return -1;
    }


}
