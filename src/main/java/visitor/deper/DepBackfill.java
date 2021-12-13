package visitor.deper;

import entity.BaseEntity;
import entity.FileEntity;
import util.PathUtil;
import util.SingleCollect;

import java.util.HashMap;

public abstract class DepBackfill{

    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    /**
     * relationType1: entityId1 -> entityId2
     * relationType2: entityId2 -> entityId1
     * @param entityId1
     * @param entityId2
     * @param relationType1
     * @param relationType2
     */
    protected void saveRelation(int entityId1, int entityId2, String relationType1, String relationType2) {

        singleCollect.getEntityById(entityId1).addRelation(relationType1, entityId2);
        singleCollect.getEntityById(entityId2).addRelation(relationType2, entityId1);
    }

    /**
     * Get the type id according to the file full qualified name
     * @param fileFullName the full qualified name of file
     * @return the type id
     */
    public int findTypeWithFullname(String fileFullName) {
        String pkgFullName = PathUtil.deleteLastStrByDot(fileFullName);
        int typeId = -1;
        if(singleCollect.getCreatedPackage().containsKey(pkgFullName)){
            int pkgId = singleCollect.getCreatedPackage().get(pkgFullName);
            for (int id : singleCollect.getEntityById(pkgId).getChildrenIds()) {
                if(singleCollect.isFile(id)){
                    for(int childId : singleCollect.getEntityById(id).getChildrenIds()){
                        if(singleCollect.getEntityById(childId).getQualifiedName().equals(fileFullName)){
                            typeId = childId;
                            break;
                        }
                    }
                }
                if(typeId != -1){
                    break;
                }
            }

        }
        // if this file is well-known the fileId is -1
        // Otherwise, fileId cannot be -1
        return typeId;
    }

    /**
     *
     * @param name the simple name of type
     * @param importedClass the imported class in file
     * @param importedPackage the imported package in file
     * @return
     */
    public int findTypeInImport(String name, HashMap<String, Integer> importedClass, HashMap<String, Integer> importedPackage){
        int typeId = -1;
        for(String fileFullName : importedClass.keySet()){
            if(PathUtil.getLastStrByDot(fileFullName).equals(name)){
                typeId = importedClass.get(fileFullName);
                break;
            }
        }
        if(typeId == -1){
            int pkgId;
            //means it is not be found in file, we need to traverse package
            for(String pkgFullName : importedPackage.keySet()){
                if(singleCollect.getCreatedPackage().containsKey(pkgFullName)){
                    pkgId = singleCollect.getCreatedPackage().get(pkgFullName);
                    for (int id : singleCollect.getEntityById(pkgId).getChildrenIds()) {
                        if (singleCollect.isFile(id)) {
                            for (int childId : singleCollect.getEntityById(id).getChildrenIds()) {
                                if (singleCollect.getEntityById(childId).getName().equals(name)) {
                                    typeId = childId;
                                    break;
                                }
                            }
                        }
                        if (typeId != -1) {
                            break;
                        }
                    }
                }
            }
        }
        return typeId;
    }

    /**
     * find type in the same package which means it does not need import statement
     * @param pkgId
     * @param typeName
     * @return
     */
    public int findTypeInPackage (int pkgId, String typeName){
        int typeId = -1;
        if(pkgId != -1){
            for(int id : singleCollect.getEntityById(pkgId).getChildrenIds()){
                if(singleCollect.isFile(id)){
                    for (int childId : singleCollect.getEntityById(id).getChildrenIds()) {
                        if (singleCollect.getEntityById(childId).getName().equals(typeName)) {
                            typeId = childId;
                            break;
                        }
                    }
                }
                if(typeId != -1)
                    break;
            }
        } else {
            for(BaseEntity baseEntity :singleCollect.getEntities()){
                if(baseEntity instanceof FileEntity && baseEntity.getParentId() == -1){
                    for(int childId : baseEntity.getChildrenIds()){
                        if(singleCollect.getEntityById(childId).getName().equals(typeName)){
                            typeId = childId;
                        }
                    }
                }
                if (typeId != -1)
                    break;
            }
        }
        return typeId;
    }

    /**
     * get current entity's file id
     * @param id
     * @return
     */
    public int getCurrentFileId(int id){
        if(singleCollect.getEntityById(id) instanceof FileEntity){
            return id;
        }
        else if (singleCollect.getEntityById(singleCollect.getEntityById(id).getParentId()) instanceof FileEntity){
            return singleCollect.getEntityById(id).getParentId();
        }
        else {
            return getCurrentFileId(singleCollect.getEntityById(id).getParentId());
        }
    }

    public abstract void setDep();
}
