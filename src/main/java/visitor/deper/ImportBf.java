package visitor.deper;

import entity.*;
import entity.properties.Location;
import util.Configure;
import util.PathUtil;
import util.Tuple;

public class ImportBf extends DepBackfill {

    @Override
    public void setDep() {
        int tmpId = -1;
        for(BaseEntity entity : singleCollect.getEntities()){
            if (entity instanceof FileEntity){
                //import package
                for (Tuple<String, Location> pkg : ((FileEntity) entity).getImportOnDemand().keySet()){
                    if(singleCollect.getCreatedPackage().containsKey(pkg.getL())){
                        tmpId = singleCollect.getCreatedPackage().get(pkg.getL());
                        ((FileEntity) entity).getImportOnDemand().replace(pkg, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof PackageEntity){
                            ((PackageEntity) singleCollect.getEntityById(tmpId)).addImportBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY, pkg.getR());
                    }
                    /*
                     * External pkg
                     */
                    ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), pkg.getL());
                    singleCollect.addEntity(externalEntity);
                }

                //import class or annotation
                for (Tuple<String, Location> file : ((FileEntity) entity).getImportClass().keySet()){
                    tmpId = findTypeWithFullname(file.getL());
                    if(tmpId != -1){
                        ((FileEntity) entity).getImportClass().replace(file, tmpId);
                        if(singleCollect.getEntityById(tmpId) instanceof TypeEntity){
                            ((TypeEntity) singleCollect.getEntityById(tmpId)).addImportedBy(entity.getId());
                        }
                        saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY, file.getR());
                    }
                    /*
                     * External class or annotation
                     */
                    ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), file.getL());
                    singleCollect.addEntity(externalEntity);
                }

                //import static method or var
                for(Tuple<String, Location> MethOrVar : ((FileEntity) entity).getImportStatic().keySet()){
                    int typeId = findTypeWithFullname(PathUtil.deleteLastStrByDot(MethOrVar.getL()));
                    if (typeId != -1){
                        //if enum constant
                        if(singleCollect.getEntityById(typeId) instanceof EnumEntity){
                            for(String varName : ((EnumEntity) singleCollect.getEntityById(typeId)).getConstants().keySet()){
                                if(varName.equals(MethOrVar.getL())){
                                    tmpId = ((EnumEntity) singleCollect.getEntityById(typeId)).getConstants().get(varName);
                                    break;
                                }
                            }
                        }
                        //if class static method
                        if (singleCollect.getEntityById(typeId) instanceof ClassEntity){
                            for(String methName : ((ClassEntity) singleCollect.getEntityById(typeId)).getStaticMap().keySet()){
                                if(methName.equals(MethOrVar.getL())){
                                    tmpId = ((ClassEntity) singleCollect.getEntityById(typeId)).getStaticMap().get(methName);
                                    break;
                                }
                            }
                        }
                        if(tmpId == -1){
                            /*
                             * External method or var
                             */
                            ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), MethOrVar.getL());
                            singleCollect.addEntity(externalEntity);
                            //System.out.println(MethOrVar);
                        } else {
                            saveRelation(entity.getId(),tmpId, Configure.RELATION_IMPORT,Configure.RELATION_IMPORTED_BY, MethOrVar.getR());
                        }
                    }
                }
            }
        }
    }

//superclass.isEqualTo(cu.getAST().resolveWellKnownType("java.lang.Object")) need to supplement

}
