package visitor;

import entity.*;
import entity.properties.Block;
import entity.properties.Location;
import org.eclipse.jdt.core.dom.*;
import util.*;

import java.util.ArrayList;
import java.util.List;

public class ProcessEntity {

    //hidden flag
    private boolean hidden = false;

    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    public boolean getHidden(){
        return this.hidden;
    }

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

        if (packageName.contains("com.android.internal")){
            setHidden(true);
        }

        PackageEntity currentPackageEntity = new PackageEntity(packageId, packageName, packagePath);
        currentPackageEntity.setSimpleName();
        currentPackageEntity.setParentId(-1);

        if (getHidden()){
            currentPackageEntity.setHidden(true);
        }

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

        if (getHidden()){
            fileEntity.setHidden(true);
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
     * @param parentId the file id
     * @param cu compilation unit
     * @return class id
     */
    public int processType(TypeDeclaration node, int parentId, CompilationUnit cu){

        int typeId = singleCollect.getCurrentIndex();
        String typeName = node.getName().getIdentifier();
        ITypeBinding iTypeBinding = node.resolveBinding();
        //String qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentId).getParentId()).getQualifiedName() + typeName;

        if(node.isInterface()){
            InterfaceEntity interfaceEntity = new InterfaceEntity(typeId, typeName, iTypeBinding.getQualifiedName(), parentId);
            interfaceEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
            //supplement the super interface name
            if(node.superInterfaceTypes() != null){
                List<Type> superType = node.superInterfaceTypes();
                for(Type superInter : superType){
                    //if the super's name is same with the current, we well record the qualified name
                    interfaceEntity.addExtendsName(superInter.resolveBinding().getQualifiedName());
                }
            }
            if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
                interfaceEntity.setHidden(true);
            }
            singleCollect.addEntity(interfaceEntity);
        }else{
            ClassEntity classEntity = new ClassEntity(typeId, typeName, iTypeBinding.getQualifiedName(), parentId);
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
            if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
                classEntity.setHidden(true);
            }
            singleCollect.addEntity(classEntity);
        }
        //add file's children id
        singleCollect.getEntityById(parentId).addChildId(typeId);

        //add created type
        singleCollect.addCreatedType(typeId, iTypeBinding.getQualifiedName());

        return typeId;
    }

    /**
     * process an Enum declaration node
     * @param node
     * @param parentId
     * @param cu
     * @return
     */
    public int processEnum(EnumDeclaration node, int parentId, CompilationUnit cu){
        int enumId = singleCollect.getCurrentIndex();
        String enumName = node.getName().getIdentifier();
//        if(singleCollect.isFile(parentId)){
            String qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentId).getParentId()).getQualifiedName() +"."+ enumName;
//        }
        EnumEntity enumEntity;
        try {
            enumEntity = new EnumEntity(enumId, enumName, node.resolveBinding().getQualifiedName(), parentId);
        }
        catch (NullPointerException e){
            enumEntity = new EnumEntity(enumId, enumName, qualifiedName, parentId);
        }


        enumEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));

        //interface, default id is -1
        if(!node.superInterfaceTypes().isEmpty()){
            for(Object type: node.superInterfaceTypes()){
                enumEntity.addInterface(type.toString(), -1);
            }
        }

        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
            enumEntity.setHidden(true);
        }

        singleCollect.addEntity(enumEntity);
        //add parent's children Id
        singleCollect.getEntityById(parentId).addChildId(enumId);

        return enumId;
    }

    /**
     * process an enum constant
     * @param node
     * @param parentId
     * @return
     */
    public int processEnumConstant(EnumConstantDeclaration node, int parentId){
        int constantId = singleCollect.getCurrentIndex();
        String constantName = node.getName().getIdentifier();
        String qualifiedName;
        try {
            qualifiedName = node.resolveVariable().getType().getQualifiedName()+"."+constantName;
        }
        catch (NullPointerException e){
            qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName() +"."+ constantName;
        }

        EnumConstantEntity<String> enumConstantEntity = new EnumConstantEntity<String>(constantId, constantName, qualifiedName, parentId);

        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
            enumConstantEntity.setHidden(true);
        }

        singleCollect.addEntity(enumConstantEntity);
        singleCollect.getEntityById(parentId).addChildId(constantId);

        if(singleCollect.getEntityById(parentId) instanceof EnumEntity){
            ((EnumEntity) singleCollect.getEntityById(parentId)).addConstant(qualifiedName, constantId);
        }

        return constantId;
    }

    /**
     * process an Annotation declaration node
     * @param node
     * @param parentId
     * @param cu
     * @return
     */
    public int processAnnotation(AnnotationTypeDeclaration node, int parentId, CompilationUnit cu){
        int annotationId = singleCollect.getCurrentIndex();
        String annotationName = node.getName().getIdentifier();
        // if parent is file
        String qualifiedName = null;
        if(singleCollect.getEntityById(parentId).getParentId() != -1){
            qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentId).getParentId()).getQualifiedName() +"."+ annotationName;
        } else {
            qualifiedName = annotationName;
        }

        AnnotationEntity annotationEntity;
        try {
            annotationEntity = new AnnotationEntity(annotationId, annotationName, node.resolveBinding().getQualifiedName(), parentId);
        }
        catch (NullPointerException e){
            annotationEntity = new AnnotationEntity(annotationId, annotationName, qualifiedName, parentId);
        }

        annotationEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));

        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
            annotationEntity.setHidden(true);
        }

        singleCollect.addEntity(annotationEntity);
        //add parent's children Id
        singleCollect.getEntityById(parentId).addChildId(annotationId);

        singleCollect.addCreatedAnt(annotationId, annotationName);

        return annotationId;
    }

    public int processAnnotationMember(AnnotationTypeMemberDeclaration node, int parentId){
        int memberId = singleCollect.getCurrentIndex();
        String memberName = node.getName().getIdentifier();
        String qualifiedName;
        try {
            qualifiedName = node.resolveBinding().getDeclaringClass().getQualifiedName()+"."+memberName;
        }
        catch (NullPointerException e){
            qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName() +"."+ memberName;
        }

        AnnotationTypeMember annotationTypeMember = new AnnotationTypeMember(memberId, node.getType().toString(), memberName, qualifiedName, parentId);

        if(node.getDefault() != null){
            annotationTypeMember.setDefault_value(node.getDefault().toString());
        }

        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
            annotationTypeMember.setHidden(true);
        }

        singleCollect.addEntity(annotationTypeMember);
        singleCollect.getEntityById(parentId).addChildId(memberId);

        if(singleCollect.getEntityById(parentId) instanceof AnnotationEntity){
            ((AnnotationEntity) singleCollect.getEntityById(parentId)).addMember(qualifiedName, memberId);
        }

        return memberId;
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
        String methodQualifiedName = singleCollect.getEntityById(parentTypeId).getQualifiedName() + "." + methodName;


        MethodEntity methodEntity = new MethodEntity(methodId,methodName);
        methodEntity.setQualifiedName(methodQualifiedName);
        methodEntity.setParentId(parentTypeId);
        methodEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        //methodEntity.setCodeSnippet(node.toString());

        if(node.isConstructor()){
            methodEntity.setConstructor(true);
        }else{
            if(node.getReturnType2() != null){
                methodEntity.setReturnType(node.getReturnType2().toString());
            }
        }

        if (getHidden() || singleCollect.getEntityById(parentTypeId).getHidden()){
            methodEntity.setHidden(true);
        }
        singleCollect.addEntity(methodEntity);

        //add type's children id
        singleCollect.getEntityById(parentTypeId).addChildId(methodId);


        //supplement static method
        for(Object o : node.modifiers()){
            if(o.toString().equals("static") ){
                methodEntity.setStatic(true);
                if(singleCollect.getEntityById(parentTypeId) instanceof ClassEntity)
                    ((ClassEntity) singleCollect.getEntityById(parentTypeId)).addStaticMap(methodQualifiedName, methodId);
            }
            switch (o.toString()) {
                case "public":
                    methodEntity.setAccessibility("Public");
                    break;
                case "protected":
                    methodEntity.setAccessibility("Protected");
                    break;
                case "private":
                    methodEntity.setAccessibility("Private");
                    break;
            }
        }

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
    public ArrayList<Integer> processVarDeclFragment(List<VariableDeclarationFragment> fragment, int parentId, String varType, int blockId, int staticFlag, boolean globalFlag, String accessibility){

        ArrayList<Integer> variableIds = new ArrayList<Integer>();

        String varName;
        int varId;

        //iterate the fragment
        for(VariableDeclarationFragment frag : fragment){
            varName = frag.getName().getIdentifier();
//            if(varName.equals())
            varId = singleCollect.getCurrentIndex();
            VariableEntity varEntity = new VariableEntity(varId,varName,varType);
            varEntity.setParentId(parentId);
            varEntity.setQualifiedName(singleCollect.getEntityById(parentId).getQualifiedName()+"."+varName);
            varEntity.setBlockId(blockId);
            varEntity.setAccessibility(accessibility);
            varEntity.setGlobal(globalFlag);
            //set init
            if(frag.getInitializer() != null){
                varEntity.setSetBy(parentId);
                varEntity.setValue(frag.getInitializer().toString());
            }

            //variableEntity.setCodeSnippet(frag.toString());
            variableIds.add(varId);

            if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
                varEntity.setHidden(true);
            }

            singleCollect.addEntity(varEntity);

            //supplement parent method
            if(singleCollect.isMethod(parentId)){
                ((MethodEntity)singleCollect.getEntityById(parentId)).addLocalVar(varEntity);
            }

            //supplement static
            if (staticFlag == 1){
                ((ClassEntity) singleCollect.getEntityById(parentId)).addStaticMap(singleCollect.getEntityById(parentId).getQualifiedName()+"."+varName, varId);
            }

//            //supplement global or local
//            if (globalFlag){
//                varEntity.setAccessibility("Global");
//            }else {
//                varEntity.setAccessibility("Local");
//            }

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
        parameterEntity.setQualifiedName(singleCollect.getEntityById(parentMethodId).getQualifiedName()+"."+name);
        parameterEntity.setParentId(parentMethodId);

        if (getHidden() || singleCollect.getEntityById(parentMethodId).getHidden()){
            parameterEntity.setHidden(true);
        }

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
