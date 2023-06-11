package visitor.deper;

import entity.BaseEntity;
import entity.MethodEntity;
import entity.ScopeEntity;
import entity.VariableEntity;
import entity.properties.Location;
import entity.properties.QualifiedNameSite;
import util.Configure;
import util.Tuple;

public class VarInfoBf extends DepBackfill{

    @Override
    public void setDep() {
        for(BaseEntity entity :singleCollect.getEntities()){
            if(entity instanceof VariableEntity){
                //set
//                if(((VariableEntity) entity).getSetBy() != 0)
//                    saveRelation(((VariableEntity) entity).getSetBy(),entity.getId(), Configure.RELATION_SET, Configure.RELATION_SETED_BY, entity.getLocation());
                //Typed
                if (singleCollect.getCreatedType().containsKey(entity.getRawType())){
                    saveRelation(entity.getId(), singleCollect.getCreatedType().get(entity.getRawType()), Configure.RELATION_TYPED, Configure.RELATION_TYPED_BY, entity.getLocation());
                }
            }
            //use & modify
            if(entity instanceof MethodEntity){
                for(int varId : ((MethodEntity) entity).getId2Usage().keySet()){
                    for(Tuple<String, Location> usage : ((MethodEntity) entity).getId2Usage().get(varId)){
                        switch (usage.getL()) {
                            case "use":
                                try {
                                    saveRelation(entity.getId(), varId, Configure.RELATION_USE, Configure.RELATION_USED_BY, usage.getR());
                                } catch (NullPointerException e) {
//                                System.out.println("NULL ID VAR: "+varName);
                                } catch (IndexOutOfBoundsException e){
//                                    System.out.println(entity.getQualifiedName());
//                                    System.out.println(usage.getR().getStartLine());
                                }
                                break;
                            case "modify":
                                try {
                                    saveRelation(entity.getId(), varId, Configure.RELATION_MODIFY, Configure.RELATION_MODIFIED_BY, usage.getR());
                                } catch (NullPointerException e) {
//                                System.out.println("NULL ID VAR: "+varName);
                                } catch (IndexOutOfBoundsException e){
//                                    System.out.println(entity.getQualifiedName());
//                                    System.out.println(usage.getR().getStartLine());
                                }
                                break;
                            case "set":
                                saveRelation(entity.getId(), varId, Configure.RELATION_SET, Configure.RELATION_SETED_BY, usage.getR());
                                break;
                        }
                    }
                }
            }
            if (entity instanceof ScopeEntity && !((ScopeEntity) entity).getQualifiedNameSite().isEmpty()){
                for (QualifiedNameSite qualifiedNameSite : ((ScopeEntity) entity).getQualifiedNameSite()){
                    int typeId = findTypeWithFullname(qualifiedNameSite.getCreatedTypeQualifiedName());
                    if (typeId != -1){
                        for (int child : singleCollect.getEntityById(typeId).getChildrenIds()){
                            if (singleCollect.getEntityById(child).getName().equals(qualifiedNameSite.getVarName()) &&
                                    (singleCollect.isVariable(child)||singleCollect.isEnumCont(child))){
                                saveRelation(entity.getId(), child, Configure.RELATION_USE, Configure.RELATION_USED_BY, qualifiedNameSite.getLocation());
                            }
                        }
                    }
                }
            }
        }
    }
}
