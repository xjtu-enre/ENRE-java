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
    public static Location supplement_location (CompilationUnit cu, int start_position, int length){
        Location location = new Location();

        location.setStartLine(cu.getLineNumber(start_position));
        location.setEndLine(cu.getLineNumber(start_position+length-1));

        location.setStartColumn(cu.getColumnNumber(start_position));
        location.setEndColumn(cu.getColumnNumber(start_position+length-1));

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

//        if (getHidden()){
//            currentPackageEntity.setHidden(true);
//        }

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
    public int processFile(String fileFullPath, int packageIndex, int currentBin) {

        int fileId = singleCollect.getCurrentIndex();

        FileEntity fileEntity = new FileEntity(fileId, fileFullPath);
        //set the parent id
        fileEntity.setParentId(packageIndex);
        if(packageIndex != -1){
            fileEntity.setQualifiedName(singleCollect.getEntityById(packageIndex).getQualifiedName()+"."+fileEntity.getName());
        } else {
            fileEntity.setQualifiedName(fileEntity.getName());
        }

//        if (getHidden()){
//            fileEntity.setHidden(true);
//        }

        fileEntity.setBinNum(currentBin);
        singleCollect.addEntity(fileEntity);
        singleCollect.addFileId(fileId);

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
    public int processType(TypeDeclaration node, int parentId, CompilationUnit cu, int currentBin){

        int typeId = singleCollect.getCurrentIndex();
        String typeName = node.getName().getIdentifier();
        ITypeBinding iTypeBinding = node.resolveBinding();
        String qualifiedName;
        try{
            qualifiedName = iTypeBinding.getQualifiedName();
            if (qualifiedName.equals("")){
                qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName()+"."+typeName;
            }
        } catch (NullPointerException e){
            if (singleCollect.isFile(parentId)){
                if (singleCollect.getEntityById(parentId).getParentId() != -1){
                    qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentId).getParentId()).getQualifiedName()+"."+typeName;
                } else {
                    qualifiedName = typeName;
                }
            } else {
                qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName()+"."+typeName;
            }

        }

        if(node.isInterface()){
            InterfaceEntity interfaceEntity = new InterfaceEntity(typeId, typeName, qualifiedName, parentId);
            interfaceEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
            interfaceEntity.setRawType(qualifiedName);
            //supplement the super interface name
            if(node.superInterfaceTypes() != null){
                List<Type> superType = node.superInterfaceTypes();
                for(Type superInter : superType){
                    interfaceEntity.addExtendsName(superInter.resolveBinding().getQualifiedName());
                }
            }
            for(Object o : node.modifiers()) {
                interfaceEntity.addModifier(o.toString());
            }

//            if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//                interfaceEntity.setHidden(true);
//            }
            interfaceEntity.setBinNum(currentBin);
            singleCollect.addEntity(interfaceEntity);
        }else{
            ClassEntity classEntity = new ClassEntity(typeId, typeName, qualifiedName, parentId);
            classEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
            classEntity.setRawType(qualifiedName);
            //superclass
            try {
                Type superType = node.getSuperclassType();
                if(superType != null){
                    classEntity.setSuperClassName(superType.resolveBinding().getQualifiedName());
                }
            } catch (NullPointerException e){
//                e.printStackTrace();
            }

            //interface, default id is -1
//            if(iTypeBinding != null){
//                ITypeBinding[] interfaces = iTypeBinding.getInterfaces();
//                for( ITypeBinding temp : interfaces) {
//                    if(temp.getName().equals(node.getName().toString())){
//                        classEntity.addInterface(temp.getQualifiedName(),-1);
//                    }else{
//                        classEntity.addInterface(temp.getName(),-1);
//                    }
//                }
//            }

            //superclass
            try {
                List superInterfaces = node.superInterfaceTypes();
                for (Object temp : superInterfaces){
                     if (temp instanceof Type){
                         classEntity.addInterface(((Type) temp).resolveBinding().getQualifiedName(), -1);
                     }
                }
            } catch (NullPointerException e){
//                e.printStackTrace();
            }

            for(Object o : node.modifiers()) {
                classEntity.addModifier(o.toString());
            }

//            if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//                classEntity.setHidden(true);
//            }
            classEntity.setBinNum(currentBin);
            singleCollect.addEntity(classEntity);
        }
        //add file's children id
        singleCollect.getEntityById(parentId).addChildId(typeId);

        //add created type
        singleCollect.addCreatedType(typeId, qualifiedName);

        return typeId;
    }

    public int processAnonymous(AnonymousClassDeclaration node, int parentId, CompilationUnit cu, String rawType, int currentBin){
        int classId = singleCollect.getCurrentIndex();
        String typeName = "Anonymous_Class";
        String qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName()+"."+typeName;
        ClassEntity classEntity = new ClassEntity(classId, typeName, qualifiedName, parentId);
        classEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        classEntity.setRawType(rawType);

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            classEntity.setHidden(true);
//        }
        classEntity.setBinNum(currentBin);
        singleCollect.addEntity(classEntity);
        singleCollect.getEntityById(parentId).addChildId(classId);

        return classId;
    }

    /**
     * process an Enum declaration node
     * @param node
     * @param parentId
     * @param cu
     * @return
     */
    public int processEnum(EnumDeclaration node, int parentId, CompilationUnit cu, int currentBin){
        int enumId = singleCollect.getCurrentIndex();
        String enumName = node.getName().getIdentifier();
        String qualifiedName;
        try{
            qualifiedName = node.resolveBinding().getQualifiedName();
        } catch (NullPointerException e){
            if (singleCollect.isFile(parentId)){
                if (singleCollect.getEntityById(parentId).getParentId() == -1){
                    qualifiedName = enumName;
                }else {
                    qualifiedName = singleCollect.getEntityById(singleCollect.getEntityById(parentId).getParentId()).getQualifiedName()+"."+enumName;
                }
            } else {
                qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName()+"."+enumName;
            }

        }

        EnumEntity enumEntity = new EnumEntity(enumId, enumName, qualifiedName, parentId);
        enumEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        enumEntity.setRawType(qualifiedName);
        enumEntity.setBinNum(currentBin);

        //interface, default id is -1
        if(!node.superInterfaceTypes().isEmpty()){
            for(Object type: node.superInterfaceTypes()){
                enumEntity.addInterface(type.toString(), -1);
            }
        }

        for(Object o : node.modifiers()) {
            enumEntity.addModifier(o.toString());
        }

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            enumEntity.setHidden(true);
//        }

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
    public int processEnumConstant(EnumConstantDeclaration node, int parentId, CompilationUnit cu, int currentBin){
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
        enumConstantEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));

        for(Object o : node.modifiers()) {
            enumConstantEntity.addModifier(o.toString());
        }

        enumConstantEntity.setBinNum(currentBin);

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            enumConstantEntity.setHidden(true);
//        }

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
    public int processAnnotation(AnnotationTypeDeclaration node, int parentId, CompilationUnit cu, int currentBin){
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
            annotationEntity.setRawType(node.resolveBinding().getQualifiedName());
        }
        catch (NullPointerException e){
            annotationEntity = new AnnotationEntity(annotationId, annotationName, qualifiedName, parentId);
            annotationEntity.setRawType(qualifiedName);
        }

        annotationEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        annotationEntity.setBinNum(currentBin);

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            annotationEntity.setHidden(true);
//        }

        for(Object o : node.modifiers()) {
            annotationEntity.addModifier(o.toString());
        }

        singleCollect.addEntity(annotationEntity);
        //add parent's children Id
        singleCollect.getEntityById(parentId).addChildId(annotationId);

        singleCollect.addCreatedAnt(annotationId, annotationName);

        return annotationId;
    }

    public int processAnnotationMember(AnnotationTypeMemberDeclaration node, int parentId, CompilationUnit cu, int currentBin){
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
        try {
            annotationTypeMember.setRawType(node.getType().resolveBinding().getQualifiedName());
        }catch (NullPointerException e){
            annotationTypeMember.setRawType(node.getType().toString());
        }
        annotationTypeMember.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));

        for(Object o : node.modifiers()) {
            annotationTypeMember.addModifier(o.toString());
        }

        if(node.getDefault() != null){
            annotationTypeMember.setDefault_value(node.getDefault().toString());
        }

        annotationTypeMember.setBinNum(currentBin);

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            annotationTypeMember.setHidden(true);
//        }

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
    public int processMethod(MethodDeclaration node,int parentTypeId, CompilationUnit cu, int currentBin){

        int methodId = singleCollect.getCurrentIndex();
        String methodName = node.getName().getIdentifier();
        String methodQualifiedName = singleCollect.getEntityById(parentTypeId).getQualifiedName() + "." + methodName;


        MethodEntity methodEntity = new MethodEntity(methodId,methodName);
        methodEntity.setQualifiedName(methodQualifiedName);
        methodEntity.setParentId(parentTypeId);
        methodEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        //methodEntity.setCodeSnippet(node.toString());

        if(node.isConstructor()){
            methodEntity.getMethodProperties().setConstructor(true);
            methodEntity.setRawType(methodQualifiedName);
        }else{
            if(node.getReturnType2() != null){
                methodEntity.setReturnType(node.getReturnType2().toString());
                try {
                    methodEntity.setRawType(node.getReturnType2().resolveBinding().getQualifiedName());
                }catch (NullPointerException e){

                }
            }
        }
        methodEntity.setBinNum(currentBin);

//        if (getHidden() || singleCollect.getEntityById(parentTypeId).getHidden()){
//            methodEntity.setHidden(true);
//        }
        singleCollect.addEntity(methodEntity);

        //add type's children id
        singleCollect.getEntityById(parentTypeId).addChildId(methodId);


        //supplement static method
        for(Object o : node.modifiers()){
            methodEntity.addModifier(o.toString());
            if(o.toString().equals("static") ){
                if(singleCollect.getEntityById(parentTypeId) instanceof ClassEntity)
                    ((ClassEntity) singleCollect.getEntityById(parentTypeId)).addStaticMap(methodQualifiedName, methodId);
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
     * @param rawType the type of all of these var
     * @return ArrayList of vars' ids
     */
    public ArrayList<Integer> processVarDeclFragment(List<VariableDeclarationFragment> fragment, int parentId, String rawType, int blockId,
                                                     int staticFlag, boolean globalFlag, ArrayList<String> modifiers, CompilationUnit cu, int currentBin){

        ArrayList<Integer> variableIds = new ArrayList<Integer>();
        ArrayList<VariableEntity> vars = new ArrayList<>();

        //iterate the fragment
        for(VariableDeclarationFragment frag : fragment){
            vars.add(processVarDeclFragment(frag, parentId, rawType, blockId, staticFlag, globalFlag, modifiers, cu, currentBin));
        }

        for (VariableEntity var : vars){
            variableIds.add(var.getId());
        }
        return variableIds;
    }

    public VariableEntity processVarDeclFragment(VariableDeclarationFragment frag, int parentId, String varType, int blockId,
                                      int staticFlag, boolean globalFlag, ArrayList<String> modifiers, CompilationUnit cu, int currentBin){
        String varName = frag.getName().getIdentifier();
        int varId = singleCollect.getCurrentIndex();
        VariableEntity varEntity = new VariableEntity(varId,varName,varType);
        varEntity.setParentId(parentId);
        varEntity.setQualifiedName(singleCollect.getEntityById(parentId).getQualifiedName()+"."+varName);
        varEntity.setBlockId(blockId);
        varEntity.addModifiers(modifiers);
        varEntity.setGlobal(globalFlag);
        varEntity.setLocation(supplement_location(cu, frag.getStartPosition(), frag.getLength()));
        varEntity.setBinNum(currentBin);

        //set init
        if(frag.getInitializer() != null){
            varEntity.setSetBy(parentId);
            varEntity.setValue(frag.getInitializer().toString());
        }

//        if (getHidden() || singleCollect.getEntityById(parentId).getHidden()){
//            varEntity.setHidden(true);
//        }

        varEntity.setLocation(supplement_location(cu, frag.getStartPosition(), frag.getLength()));

        singleCollect.addEntity(varEntity);

        //supplement parent method
        if(singleCollect.isMethod(parentId)){
            ((MethodEntity)singleCollect.getEntityById(parentId)).addLocalVar(varEntity);
        }

        //supplement static
        if (staticFlag == 1){
            ((ClassEntity) singleCollect.getEntityById(parentId)).addStaticMap(singleCollect.getEntityById(parentId).getQualifiedName()+"."+varName, varId);
        }

        return varEntity;
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
    public int processSingleVar (String name, int parentMethodId, String parType, int currentBin){

        int parId = singleCollect.getCurrentIndex();

        VariableEntity parameterEntity = new VariableEntity(parId,name,parType);
        parameterEntity.setQualifiedName(singleCollect.getEntityById(parentMethodId).getQualifiedName()+"."+name);
        parameterEntity.setParentId(parentMethodId);
        parameterEntity.setBinNum(currentBin);

//        if (getHidden() || singleCollect.getEntityById(parentMethodId).getHidden()){
//            parameterEntity.setHidden(true);
//        }

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
    public int processLocalBlock(int functionIndex, int parentBlockId, int depth, String blockName) {
        int blockId;
        if (singleCollect.isMethod(functionIndex)){
            MethodEntity functionEntity = (MethodEntity) singleCollect.getEntityById(functionIndex);
            blockId = functionEntity.getBlocks().size();
            Block localBlock = new Block(blockId, blockName, parentBlockId, depth);
            ((MethodEntity) singleCollect.getEntityById(functionIndex)).addBlock(localBlock);
        } else {
            // static block's id
            blockId = -1;
        }

        return blockId;
    }


}
