package visitor;

import entity.*;
import entity.properties.Block;
import entity.properties.Location;
import org.eclipse.jdt.core.dom.*;
import util.*;

import java.util.ArrayList;
import java.util.List;

public class ProcessEntity {

    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    /**
     * supplement the entities' position
     * @param cu CompilationUnit
     * @param start_position the start char position of node
     * @param length the char length of node
     * @return location
     */
    public Location supplement_location (CompilationUnit cu, int start_position, int length){
        Location location = new Location();

        location.setStartLine(cu.getLineNumber(start_position));
        location.setEndLine(cu.getLineNumber(start_position+length));

        location.setStartColumn(cu.getColumnNumber(start_position));
        location.setEndColumn(cu.getColumnNumber(start_position+length));

        location.setStartOffset(start_position);
        location.setEndOffset(start_position+length);

        return location;
    }

    /**
     * process the package declaration in current file.
     * Its parent is a package or none.
     *
     * @param packageName current last package name
     * @param packagePath pkg_1/pkg_2/pkg_3(current)
     * @return pkg_3's id
     */
    public int processPackageDecl(String packageName, String packagePath){
        //new package entity and get the package Id
        int packageId = singleCollect.getCurrentIndex();

        PackageEntity currentPackageEntity = new PackageEntity(packageId, packageName, packagePath);
        currentPackageEntity.setSimpleName();
        currentPackageEntity.setParentId(-1);
        singleCollect.addEntity(currentPackageEntity);
        return packageId;
    }

    /**
     * Resolve current package declaration binding and supplement the parent pkg
     *
     * @param pkgQualifiedName the ASTNode's Qualified name
     * @param packagePath Current package full path eg. .../pkg_1/pkg_2/pkg_3(current)
     * @param currentPkgId pkg3's id
     */
    public void reslovePackageRelation(String pkgQualifiedName,String packagePath, int currentPkgId){
        String[] allPkgName = pkgQualifiedName.split("\\.");
        int currentId = currentPkgId;
        int correspondId;
        String correspondPath = packagePath;
        String correspondQualifiedName = pkgQualifiedName;

        for (int i = allPkgName.length-2; i>=0; i--){
            //.../pkg_1/pkg_2
            correspondQualifiedName = PathUtil.deleteLastStrByDot(correspondQualifiedName);
            //check whether it has been created
            if(singleCollect.getCreatedPackage().containsKey(correspondQualifiedName)){
                //if pkg_2 has been created, get pkg_2 id
                correspondId = singleCollect.getCreatedPackage().get(correspondQualifiedName);

            } else {
                //pkg_2 is not be created, get pkg_2's path
                correspondPath = PathUtil.deleteLastStrByPathDelimiter(correspondPath);
                //create pkg_2 entity and get its id
                correspondId = processPackageDecl(correspondQualifiedName,correspondPath);
                //add pkg_2 into created package
                singleCollect.addCreatedPackage(correspondId,correspondQualifiedName);
            }
            //now the currentId is pkg_3, correspondId is pkg_2, pkg_3 is pkg_2's children
            if(!singleCollect.getEntityById(correspondId).getChildrenIds().contains(currentId)){
                singleCollect.getEntityById(correspondId).addChildId(currentId);
            }
            //pkg_2 is pkg_3‘s parent
            singleCollect.getEntityById(currentId).setParentId(correspondId);
            //turn currentId to pkg_2
            currentId = correspondId;
        }
    }

    /**
     * process the file and save it into fileEnitiy
     * its parent is a package or none. after finishing all files, we should set the parentId for each file
     * save into singlecollect.entity.
     * @param fileFullPath packageIndex
     * @return moduleId
     */
    public int processFile(String fileFullPath, int packageIndex) {

        int fileId = singleCollect.getCurrentIndex();

        FileEntity fileEntity = new FileEntity(fileId, fileFullPath);
        //set the parent id
        fileEntity.setParentId(packageIndex);
        if(packageIndex != -1){
            fileEntity.setQualifiedName(singleCollect.getEntityById(packageIndex).getQualifiedName()+"."+fileEntity.getName());
        } else {
            fileEntity.setQualifiedName(fileEntity.getName());
        }
        singleCollect.addEntity(fileEntity);

        //add package's children id if package exists
        if(packageIndex != -1){
            singleCollect.getEntityById(packageIndex).addChildId(fileId);
        }

        return fileId;
    }

    /**
     * process the class and save it into classEntity
     * its parent is a file
     * save into singlecollect.entities
     * @param node the declaration node
     * @param parentFileId the file id
     * @param cu compilation unit
     * @return class id
     */
    public int processType(TypeDeclaration node, int parentFileId, CompilationUnit cu){

        int typeId = singleCollect.getCurrentIndex();
        String typeName = node.getName().getIdentifier();
        ITypeBinding iTypeBinding = node.resolveBinding();
        //String qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentFileId).getParentId()).getQualifiedName() + typeName;

        if(node.isInterface()){
            InterfaceEntity interfaceEntity = new InterfaceEntity(typeId, typeName, iTypeBinding.getQualifiedName(), parentFileId);
            interfaceEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
            //supplement the super interface name
            if(node.superInterfaceTypes() != null){
                List<Type> superType = node.superInterfaceTypes();
                for(Type superInter : superType){
                    //if the super's name is same with the current, we well record the qualified name
                    interfaceEntity.addExtendsName(superInter.resolveBinding().getQualifiedName());
                }
            }
            singleCollect.addEntity(interfaceEntity);
        }else{
            ClassEntity classEntity = new ClassEntity(typeId, typeName, iTypeBinding.getQualifiedName(), parentFileId);
            classEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
            //superclass
            Type superType = node.getSuperclassType();
            if(superType != null){
                classEntity.setSuperClassName(superType.resolveBinding().getQualifiedName());
            }
            //interface, default id is -1
            if(iTypeBinding != null){
                ITypeBinding[] interfaces = iTypeBinding.getInterfaces();
                for( ITypeBinding temp : interfaces) {
                    if(temp.getName().equals(node.getName().toString())){
                        classEntity.addInterfaces(temp.getQualifiedName(),-1);
                    }else{
                        classEntity.addInterfaces(temp.getName(),-1);
                    }
                }
            }
            singleCollect.addEntity(classEntity);
        }
        //add file's children id
        singleCollect.getEntityById(parentFileId).addChildId(typeId);

        //add created type
        singleCollect.addCreatedType(typeId, iTypeBinding.getQualifiedName());

        return typeId;
    }

    /**
     * process the method and save it into methodEntity
     * its parent is a type(class or interface)
     * save into singlecollect.entities
     * @param node methodDeclaration node
     * @param parentTypeId parent type id
     * @param cu compilation unit
     * @return method id
     */
    public int processMethod(MethodDeclaration node,int parentTypeId, CompilationUnit cu){

        int methodId = singleCollect.getCurrentIndex();
        String methodName = node.getName().getIdentifier();

        MethodEntity methodEntity = new MethodEntity(methodId,methodName);
        methodEntity.setQualifiedName(singleCollect.getEntityById(parentTypeId).getQualifiedName()+"."+methodName);
        methodEntity.setParentId(parentTypeId);
        methodEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        //methodEntity.setCodeSnippet(node.toString());

        if(node.isConstructor()){
            methodEntity.setConstructor(true);
        }else{
            methodEntity.setReturnType(node.getReturnType2().toString());
        }
        singleCollect.addEntity(methodEntity);

        //add type's children id
        singleCollect.getEntityById(parentTypeId).addChildId(methodId);

        return methodId;
    }

    /**
     * process the method or class variable list and save all of them into variableEntity
     * their parent is a type or method, depends on the which ASTNode we are visiting
     * finally, save all into singlecollect.entities
     * @param fragment the fragment under the declaration or statement
     * @param parentId the id of type or method
     * @param varType the type of all of these var
     * @return ArrayList of vars' ids
     */
    public ArrayList<Integer> processVarDeclFragment(List<VariableDeclarationFragment> fragment, int parentId, String varType, int blockId){

        ArrayList<Integer> variableIds = new ArrayList<Integer>();

        String varName;
        int varId;

        //iterate the fragment
        for(VariableDeclarationFragment frag : fragment){
            varName = frag.getName().getIdentifier();
            varId = singleCollect.getCurrentIndex();
            VariableEntity varEntity = new VariableEntity(varId,varName,varType);
            varEntity.setParentId(parentId);
            varEntity.setQualifiedName(singleCollect.getEntityById(parentId).getQualifiedName()+"."+varName);
            varEntity.setBlockId(blockId);
            //set init
            if(frag.getInitializer() != null){
                varEntity.setSetBy(parentId);
                varEntity.setValue(frag.getInitializer().toString());
            }

            //variableEntity.setCodeSnippet(frag.toString());
            variableIds.add(varId);
            singleCollect.addEntity(varEntity);

            //supplement parent method
            if(singleCollect.isMethod(parentId)){
                ((MethodEntity)singleCollect.getEntityById(parentId)).addLocalVar(varEntity);
            }
        }
        return variableIds;
    }

    /**
     * process the parameter and save it into variableEntity
     * its parent is a method, and their id also needs to add in the method's parameter list
     * finally, save it into singlecollect.entities
     * @param name parameter's name
     * @param parentMethodId its parent method id
     * @param parType the type of parameter
     * @return its id
     */
    public int processSingleVar (String name, int parentMethodId, String parType){

        int parId = singleCollect.getCurrentIndex();

        VariableEntity parameterEntity = new VariableEntity(parId,name,parType);
        parameterEntity.setQualifiedName(name);
        parameterEntity.setParentId(parentMethodId);
        singleCollect.addEntity(parameterEntity);

        return parId;
    }

    /**
     * create a local block for method or other statement, such as For-stmt.
     * @param functionIndex
     * @param parentBlockId
     * @param depth
     * @param blockName
     * @return
     */
    public int processLocalBlock(int functionIndex, int parentBlockId, int depth, String blockName)
    {
        MethodEntity functionEntity = (MethodEntity) singleCollect.getEntityById(functionIndex);
        int blockId = functionEntity.getBlocks().size();
        Block localBlock = new Block(blockId, blockName, parentBlockId, depth);
        ((MethodEntity) singleCollect.getEntityById(functionIndex)).addBlock(localBlock);
        return blockId;
    }


}
