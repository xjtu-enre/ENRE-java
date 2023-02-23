package visitor.deper;

import entity.*;
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
                            saveRelation(childId, overrideMeth, Configure.RELATION_OVERRIDE, Configure.RELATION_OVERRIDE_BY, singleCollect.getEntityById(childId).getLocation());
                        }
                    }
                }
            }
            if (entity instanceof InterfaceEntity){
                if (!((InterfaceEntity) entity).getExtendsIds().isEmpty()){
                    for (int superInterfaceId : ((InterfaceEntity) entity).getExtendsIds()){
                        HashMap<innerMeth, Integer> superMeths = getInnerMeth((InterfaceEntity) singleCollect.getEntityById(superInterfaceId));
                        for(int childId: entity.getChildrenIds()){
                            int overrideMeth = checkOverride(childId, superMeths);
                            if (overrideMeth != -1){
                                saveRelation( childId, overrideMeth, Configure.RELATION_OVERRIDE, Configure.RELATION_OVERRIDE_BY, singleCollect.getEntityById(childId).getLocation());
                            }
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
//                System.out.println(para);
//                System.out.println(this.para);
                for (String s : this.para) {
                    for (String value : para) {
                        if (s.contains(value)) {
                            flag = true;
                            break;
                        } else if ((s.equals("Integer") && value.equals("int")) || (value.equals("Integer") && s.equals("int"))){
                            flag = true;
                            break;
                        } else if ((s.equals("Boolean") && value.equals("boolean")) || (value.equals("Boolean") && s.equals("boolean"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("Long") && value.equals("long")) || (value.equals("Long") && s.equals("long"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("Byte") && value.equals("byte")) || (value.equals("Byte") && s.equals("byte"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("Character") && value.equals("char")) || (value.equals("Character") && s.equals("char"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("double") && value.equals("Double")) || (value.equals("double") && s.equals("Double"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("float") && value.equals("Float")) || (value.equals("float") && s.equals("Float"))) {
                            flag = true;
                            break;
                        } else if ((s.equals("short") && value.equals("Short")) || (value.equals("short") && s.equals("Short"))) {
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
        //Constructor cannot be override
        if(singleCollect.getEntityById(MethId) instanceof MethodEntity && !singleCollect.isConstructor(MethId)){
            MethodEntity currentMeth = (MethodEntity) singleCollect.getEntityById(MethId);
            ArrayList<String> currentParas = new ArrayList<>();
            for (int paraId : currentMeth.getParameters()){
                currentParas.add(singleCollect.getEntityById(paraId).getRawType());
            }
            for (OverrideBf.innerMeth superMeth : superMeths.keySet()){
                if (currentMeth.getName().equals(superMeth.getName())){
                    if(currentMeth.getReturnType()==null){
                        if(superMeth.getReturnType()==null && superMeth.comparePara(currentParas)){
                            return superMeths.get(superMeth);
                        }
                    }  else if(currentMeth.getReturnType().equals(superMeth.getReturnType())){
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
