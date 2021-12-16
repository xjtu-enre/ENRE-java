package visitor.deper;

import entity.BaseEntity;
import entity.ClassEntity;
import entity.MethodEntity;
import entity.VariableEntity;
import util.Configure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
            if (para.size() == this.para.size()){
                this.para.sort(Comparator.comparing(String::hashCode));
                para.sort(Comparator.comparing(String::hashCode));
                return this.para.toString().equals(para.toString());
            }else {
                return false;
            }
        }
    }

    protected HashMap<innerMeth, Integer> getInnerMeth(ClassEntity classEntity){
        HashMap<innerMeth, Integer> iMeths = new HashMap<>();
        for(int childId : classEntity.getChildrenIds()){
            if(singleCollect.getEntityById(childId) instanceof MethodEntity){
                MethodEntity methodEntity = (MethodEntity) singleCollect.getEntityById(childId);
                innerMeth iMeth = new innerMeth(methodEntity.getName(), methodEntity.getReturnType());
                for(int paraId : methodEntity.getParameters()){
                    iMeth.addPara(((VariableEntity) singleCollect.getEntityById(paraId)).getType());
                }
                iMeths.put(iMeth, methodEntity.getId());
            }
        }
        return iMeths;
    }

    protected int checkOverride(int MethId, HashMap<innerMeth, Integer> superMeths){
        if(singleCollect.getEntityById(MethId) instanceof MethodEntity){
            MethodEntity currentMeth = (MethodEntity) singleCollect.getEntityById(MethId);
            ArrayList<String> currentParas = new ArrayList<>();
            for (int paraId : currentMeth.getParameters()){
                currentParas.add(((VariableEntity) singleCollect.getEntityById(paraId)).getType());
            }
            for (innerMeth superMeth : superMeths.keySet()){
                if (currentMeth.getName().equals(superMeth.getName())
                        && currentMeth.getReturnType().equals(superMeth.getReturnType())
                        && superMeth.comparePara(currentParas)){
                    return superMeths.get(superMeth);
                }
            }
        }
        return -1;
    }
}
