package visitor.deper;

import entity.*;
import util.Configure;
import util.PathUtil;

public class ImportBf extends DepBackfill {

    @Override
    public void setDep() {
        int tmpId = -1;
        for(BaseEntity entity : singleCollect.getEntities()){
            if (entity instanceof FileEntity){
                //import package
                for (String pkg : ((FileEntity) entity).getImportOnDemand().keySet()){
                    if(singleCollect.getCreatedPackage().containsKey(pkg)){
                        tmpId = singleCollect.getCreatedPackage().get(pkg);
                        ((FileEntity) entity).getImportOnDemand().replace(pkg, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof PackageEntity){
                            ((PackageEntity) singleCollect.getEntityById(tmpId)).addImportBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY);
                    }
                    /*
                     * External pkg
                     */
                    ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), pkg);
                    singleCollect.addEntity(externalEntity);
                }

                //import class or annotation
                for (String file : ((FileEntity) entity).getImportClass().keySet()){
                    tmpId = findTypeWithFullname(file);
                    if(tmpId != -1){
                        ((FileEntity) entity).getImportClass().replace(file, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof TypeEntity){
                            ((TypeEntity) singleCollect.getEntityById(tmpId)).addImportedBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY);
                    }
                    /*
                     * External class or annotation
                     */
                    ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), file);
                    singleCollect.addEntity(externalEntity);
                }

                //import static method or var
                for(String MethOrVar : ((FileEntity) entity).getImportStatic().keySet()){
                    int typeId = findTypeWithFullname(PathUtil.deleteLastStrByDot(MethOrVar));
                    if (typeId != -1){
                        //if enum constant
                        if(singleCollect.getEntityById(typeId) instanceof EnumEntity){
                            for(String varName : ((EnumEntity) singleCollect.getEntityById(typeId)).getConstants().keySet()){
                                if(varName.equals(MethOrVar)){
                                    tmpId = ((EnumEntity) singleCollect.getEntityById(typeId)).getConstants().get(varName);
                                    break;
                                }
                            }
                        }
                        //if class static method
                        if (singleCollect.getEntityById(typeId) instanceof ClassEntity){
                            for(String methName : ((ClassEntity) singleCollect.getEntityById(typeId)).getStaticMap().keySet()){
                                if(methName.equals(MethOrVar)){
                                    tmpId = ((ClassEntity) singleCollect.getEntityById(typeId)).getStaticMap().get(methName);
                                    break;
                                }
                            }
                        }
                        if(tmpId == -1){
                            /*
                             * External method or var
                             */
                            ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), MethOrVar);
                            singleCollect.addEntity(externalEntity);
                            //System.out.println(MethOrVar);
                        } else {
                            saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY);
                        }
                    }
                }
            }
        }
    }

//superclass.isEqualTo(cu.getAST().resolveWellKnownType("java.lang.Object")) need to supplement

}
