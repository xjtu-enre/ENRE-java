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
                    } else {
                        /*
                         * External pkg
                         */
                        ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), pkg.getL());
                        singleCollect.addEntity(externalEntity);
                    }
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
                    } else {
                        /*
                         * External class or annotation
                         */
                        ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), file.getL());
                        singleCollect.addEntity(externalEntity);
//                        if (file.getL().equals("helloJDT.ClassPreamble")) {
//                            System.out.println("ATTENTION");
//                            System.out.println(((FileEntity) entity).getFullPath());
//                        }
                    }
                }

                //import static method or var
                for (Tuple<String, Location> methOrVar : ((FileEntity) entity).getImportStatic().keySet()) {
                    String fullName = methOrVar.getL();
                    String typeFullName = PathUtil.deleteLastStrByDot(fullName);
                    String memberName = PathUtil.getLastStrByDot(fullName);

                    int typeId = findTypeWithFullname(typeFullName);

                    if (typeId != -1) {
                        BaseEntity typeEnt = singleCollect.getEntityById(typeId);
                        int memberId = -1;

                       if(!typeFullName.equals(fullName)) {
                           for (int childId : typeEnt.getChildrenIds()) {
                               BaseEntity child = singleCollect.getEntityById(childId);
                               if(memberName.equals(child.getName())) {
                                   memberId = child.getId();
                                   break;
                               }
                           }
                        } else {
                           memberId = typeId;
                       }

                        if (memberId != -1) {
                            ((FileEntity) entity).getImportStatic().replace(methOrVar, memberId);
                            saveRelation(entity.getId(), memberId, Configure.RELATION_IMPORT, Configure.RELATION_IMPORTED_BY, methOrVar.getR());
                        } else {
                            /*
                             * External method or var
                             */
                            ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), entity.getId(), methOrVar.getL());
                            singleCollect.addEntity(externalEntity);
                        }
                    }
                }
            }
        }
    }

//superclass.isEqualTo(cu.getAST().resolveWellKnownType("java.lang.Object")) need to supplement

}
