package visitor;

import entity.*;
import entity.properties.Block;
import entity.properties.Index;
import entity.properties.Location;
import org.eclipse.jdt.core.dom.*;
import util.*;

import java.util.*;

import static org.eclipse.jdt.core.dom.Modifier.*;
import static org.eclipse.jdt.core.dom.Modifier.isStatic;

public class ProcessEntity {

    //hidden flag
    private boolean hidden = false;

    private Index indices = new Index();

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
    public int processFile(String fileFullPath, int packageIndex, Tuple<String, Integer> currentBin) {

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
    public int processType(TypeDeclaration node, int parentId, CompilationUnit cu, Tuple<String, Integer> currentBin){

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
//                    ANTI-PROGRAMMING(CERT)
//                    classEntity.setSuperClassName(superType.toString());
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

            //interfaces
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

    public int processAnonymous(AnonymousClassDeclaration node, int parentId, CompilationUnit cu, String rawType, Tuple<String, Integer> currentBin, int rank){
        int classId = singleCollect.getCurrentIndex();
        String typeName = "Anonymous_Class";
        String qualifiedName = singleCollect.getEntityById(parentId).getQualifiedName()+"."+typeName;
        ClassEntity classEntity = new ClassEntity(classId, typeName, qualifiedName, parentId);
        classEntity.setLocation(supplement_location(cu, node.getStartPosition(), node.getLength()));
        classEntity.setRawType(rawType);
        classEntity.setAnonymousRank(rank);
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
    public int processEnum(EnumDeclaration node, int parentId, CompilationUnit cu, Tuple<String, Integer> currentBin){
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

        //interfaces
        try {
            List superInterfaces = node.superInterfaceTypes();
            for (Object temp : superInterfaces){
                if (temp instanceof Type){
                    enumEntity.addInterface(((Type) temp).resolveBinding().getQualifiedName(), -1);
                }
            }
        } catch (NullPointerException e){
//                e.printStackTrace();
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
        //add created type
        singleCollect.addCreatedType(enumId, qualifiedName);

        return enumId;
    }

    /**
     * process an enum constant
     * @param node
     * @param parentId
     * @return
     */
    public int processEnumConstant(EnumConstantDeclaration node, int parentId, CompilationUnit cu, Tuple<String, Integer> currentBin){
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
    public int processAnnotation(AnnotationTypeDeclaration node, int parentId, CompilationUnit cu, Tuple<String, Integer> currentBin){
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

    public int processAnnotationMember(AnnotationTypeMemberDeclaration node, int parentId, CompilationUnit cu, Tuple<String, Integer> currentBin){
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
    public int processMethod(MethodDeclaration node,int parentTypeId, CompilationUnit cu, Tuple<String, Integer> currentBin){

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
            methodEntity.setRawType(methodQualifiedName);
        }else{
            if(node.getReturnType2() != null){
                try {
                    methodEntity.setRawType(node.getReturnType2().resolveBinding().getQualifiedName());
                }catch (NullPointerException e){
                    methodEntity.setReturnType(node.getReturnType2().toString());
                }
            }
        }
        methodEntity.setBinNum(currentBin);

        //dependency enhancement
        IMethodBinding iMethodBinding = node.resolveBinding();
        if (iMethodBinding != null) {
//            System.out.println("Error in parsing [funcImpl name] : " + indices.getObject() + ", [location] : " + indices.getLocation().getLine() + "," + indices.getLocation().getRow());
            indices.setIsOverride(false);
            indices.setIsSetter(false);
            indices.setIsGetter(false);
            indices.setIsDelegator(false);
            indices.setIsRecursive(false);
            indices.setIsAssign(false);
            indices.setIsCallSuper(false);

            judgeMethodIsConstructor(node);
            judgeMethodIsStatic(node);
            judgeMethodIsSynchronized(node);
            judgeMethodIsPublic(node);
            judgeMethodIsAbstract(node);
            judgeMethodIsOverride(node, iMethodBinding);

            org.eclipse.jdt.core.dom.Block methodBody = node.getBody();
            List<SingleVariableDeclaration> parameters = node.parameters();
            if (methodBody != null) {
                List<Statement> statements = methodBody.statements();
                judgeMethodIsGetter(parameters, statements);
                judgeMethodIsSetter(node, parameters, statements);
                judgeMethodIsDelegator(node, statements);
            }

            judgeMethodIsRecursive(node);
            judgeMethodIsAssign(node);
            judgeMethodIsCallSuper(node);
            methodEntity.setIndices(indices);

            //Generics
            if (iMethodBinding.getTypeParameters().length != 0){
                methodEntity.setGenerics(true);
            }
        }


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
                                                     int staticFlag, boolean globalFlag, ArrayList<String> modifiers, CompilationUnit cu, Tuple<String, Integer> currentBin){

        ArrayList<Integer> variableIds = new ArrayList<Integer>();
        ArrayList<VariableEntity> vars = new ArrayList<>();

        //iterate the fragment
        for(VariableDeclarationFragment frag : fragment){
            vars.add(processVarFragment(frag, parentId, rawType, blockId, staticFlag, globalFlag, modifiers, cu, currentBin));
        }

        for (VariableEntity var : vars){
            variableIds.add(var.getId());
        }
        return variableIds;
    }

    public VariableEntity processVarFragment(VariableDeclarationFragment frag, int parentId, String varType, int blockId,
                                      int staticFlag, boolean globalFlag, ArrayList<String> modifiers, CompilationUnit cu, Tuple<String, Integer> currentBin){
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
    public int processSingleVar (String name, int parentMethodId, String parType, Tuple<String, Integer> currentBin){

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

    public int processTypeParameter(String name, int parentClassId, String parType, Tuple<String, Integer> currentBin){

        int tParId = singleCollect.getCurrentIndex();

        TypeParameterEntity parameterEntity = new TypeParameterEntity(tParId,name,parType);
        parameterEntity.setQualifiedName(singleCollect.getEntityById(parentClassId).getQualifiedName()+"."+name);
        parameterEntity.setParentId(parentClassId);
        parameterEntity.setBinNum(currentBin);

        singleCollect.addEntity(parameterEntity);

        return tParId;
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


    private void judgeMethodIsOverride(MethodDeclaration node, IMethodBinding iMethodBinding) {
        for (IAnnotationBinding iAnnotationBinding : iMethodBinding.getAnnotations()) {
            if (iAnnotationBinding.getName().equals("Override")) {
                indices.setIsOverride(true);
            }
        }

        if (!indices.getMethodIsAbstract() & !node.isConstructor() && !indices.getIsOverride() && !(node.getParent() instanceof AnonymousClassDeclaration)) {
            indices.setIsOverride(overridesMethod(node));
        }
    }

    private void judgeMethodIsConstructor(MethodDeclaration node) {
        indices.setIsConstructor(node.isConstructor());
    }

    private void judgeMethodIsCallSuper(MethodDeclaration node) {
        node.accept(new ASTVisitor() {
            public boolean visit(SuperConstructorInvocation superConstructorInvocation) {
                indices.setIsCallSuper(true);
                return true;
            }
        });

        node.accept(new ASTVisitor() {
            public boolean visit(SuperMethodInvocation superMethodInvocation) {
                indices.setIsCallSuper(true);
                return true;
            }
        });

        node.accept(new ASTVisitor() {
            public boolean visit(SuperFieldAccess superFieldAccess) {
                indices.setIsCallSuper(true);
                return true;
            }
        });
    }

    private void judgeMethodIsAssign(MethodDeclaration node) {
        node.accept(new ASTVisitor() {
            public boolean visit(Assignment assignment) {
                String assignFor = assignment.getLeftHandSide().toString();
                if (assignFor.contains("this")) {
                    indices.setIsAssign(true);
                }
                return true;
            }
        });
    }

    private void judgeMethodIsRecursive(MethodDeclaration node) {
        node.accept(new ASTVisitor() {
            public boolean visit(MethodInvocation methodInvocation) {
                if (node.resolveBinding().equals(methodInvocation.resolveMethodBinding())) {
                    indices.setIsRecursive(true);
                }
                return true;
            }
        });
    }

    private void judgeMethodIsAbstract(MethodDeclaration node) {
        indices.setMethodIsAbstract(isAbstract(node.getModifiers()));
    }

    private void judgeMethodIsPublic(MethodDeclaration node) {
        indices.setIsPublic(isPublic(node.getModifiers()));
    }

    private void judgeMethodIsSynchronized(MethodDeclaration node) {
        indices.setIsSynchronized(isSynchronized(node.getModifiers()));
    }

    private void judgeMethodIsStatic(MethodDeclaration node) {
        indices.setIsStatic(isStatic(node.getModifiers()));
    }

    private void judgeMethodIsGetter(List<SingleVariableDeclaration> parameters, List<Statement> statements) {
        if (statements.size() == 1 && parameters.size() == 0) {
            Statement statement = statements.get(0);
            if (statement instanceof ReturnStatement) {
                ReturnStatement returnStatement = (ReturnStatement) statement;
                Expression returnStatementExpression = returnStatement.getExpression();
                if (returnStatementExpression instanceof SimpleName) {
//                                indices.setGetterFor(((SimpleName) returnStatementExpression).getIdentifier());
                    indices.setIsGetter(true);
                } else if (returnStatementExpression instanceof FieldAccess) {
//                                FieldAccess fieldAccess = (FieldAccess) returnStatementExpression;
//                                indices.setGetterFor(fieldAccess.getName().getIdentifier());
                    indices.setIsGetter(true);
                }
            }
        }
    }

    private void judgeMethodIsSetter(MethodDeclaration node, List<SingleVariableDeclaration> parameters, List<Statement> statements) {
        if (statements.size() == 1 && parameters.size() == 1) {
            if (!node.isConstructor()) { // except Constructor
                Statement statement = statements.get(0);
                if (statement instanceof ExpressionStatement) {
                    ExpressionStatement expressionStatement = (ExpressionStatement) statement;
                    Expression expressionStatementExpression = expressionStatement.getExpression();
                    if (expressionStatementExpression instanceof Assignment) {
                        Assignment assignment = (Assignment) expressionStatementExpression;
                        Expression rightHandSide = assignment.getRightHandSide();
                        if (rightHandSide instanceof SimpleName) {
                            SimpleName rightHandSideSimpleName = (SimpleName) rightHandSide;
                            if (rightHandSideSimpleName.resolveBinding() != null) {
                                if (rightHandSideSimpleName.resolveBinding()
                                        .isEqualTo(parameters.get(0).resolveBinding())) {
                                    Expression leftHandSide = assignment.getLeftHandSide();
                                    if (leftHandSide instanceof SimpleName) {
//                                                    indices.setSetterFor(((SimpleName) leftHandSide).getIdentifier());
                                        indices.setIsSetter(true);
                                    } else if (leftHandSide instanceof FieldAccess) {
//                                                    FieldAccess fieldAccess = (FieldAccess) leftHandSide;
//                                                    indices.setSetterFor(fieldAccess.getName().getIdentifier());
                                        indices.setIsSetter(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void judgeMethodIsDelegator(MethodDeclaration node, List<Statement> statements) {
        if (!(node.getParent() instanceof AnonymousClassDeclaration)) {
            AbstractTypeDeclaration parentClass = (AbstractTypeDeclaration) node.getParent();
            if (statements.size() == 1) {
                Statement statement = statements.get(0);
                MethodInvocation methodInvocation = null;
                if (statement instanceof ReturnStatement) {
                    ReturnStatement returnStatement = (ReturnStatement) statement;
                    if (returnStatement.getExpression() instanceof MethodInvocation) {
                        methodInvocation = (MethodInvocation) returnStatement.getExpression();
                    }
                } else if (statement instanceof ExpressionStatement) {
                    ExpressionStatement expressionStatement = (ExpressionStatement) statement;
                    if (expressionStatement.getExpression() instanceof MethodInvocation) {
                        methodInvocation = (MethodInvocation) expressionStatement.getExpression();
                    }
                }
                if (methodInvocation != null) {
                    Expression methodInvocationExpression = methodInvocation.getExpression();
                    if (methodInvocationExpression instanceof MethodInvocation) {
                        MethodInvocation previousChainedMethodInvocation = (MethodInvocation) methodInvocationExpression;
                        List<MethodDeclaration> parentClassMethods = new ArrayList<>();
                        if (parentClass instanceof TypeDeclaration) {
                            MethodDeclaration[] parentClassMethodArray = ((TypeDeclaration) parentClass).getMethods();
                            parentClassMethods.addAll(Arrays.asList(parentClassMethodArray));
                        } else if (parentClass instanceof EnumDeclaration) {
                            EnumDeclaration enumDeclaration = (EnumDeclaration) parentClass;
                            List<BodyDeclaration> bodyDeclarations = enumDeclaration.bodyDeclarations();
                            for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
                                if (bodyDeclaration instanceof MethodDeclaration) {
                                    parentClassMethods.add((MethodDeclaration) bodyDeclaration);
                                }
                            }
                        }
                        boolean isDelegationChain = false;
                        boolean foundInParentClass = false;
                        for (MethodDeclaration parentClassMethod : parentClassMethods) {
                            if (parentClassMethod.resolveBinding().isEqualTo(previousChainedMethodInvocation.resolveMethodBinding())) {
                                foundInParentClass = true;
                                SimpleName getterField = judgeMethodIsGetter(parentClassMethod);
                                if (getterField == null)
                                    isDelegationChain = true;
                                break;
                            }
                        }
                        if (!isDelegationChain && foundInParentClass) {
                            indices.setIsDelegator(true);
                        }
                    } else if (methodInvocationExpression instanceof FieldAccess) {
                        FieldAccess fieldAccess = (FieldAccess) methodInvocationExpression;
                        IVariableBinding variableBinding = fieldAccess.resolveFieldBinding();
                        if (variableBinding.getDeclaringClass().isEqualTo(parentClass.resolveBinding()) || parentClass.resolveBinding().isSubTypeCompatible(variableBinding.getDeclaringClass())) {
                            indices.setIsDelegator(true);
                        }
                    } else if (methodInvocationExpression instanceof SimpleName) {
                        SimpleName simpleName = (SimpleName) methodInvocationExpression;
                        IBinding binding = simpleName.resolveBinding();
                        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
                            IVariableBinding variableBinding = (IVariableBinding) binding;
                            if (variableBinding.isField() || variableBinding.isParameter()) {
                                indices.setIsDelegator(true);
                            }
                        }
                    } else if (methodInvocationExpression instanceof ThisExpression) {
                        indices.setIsDelegator(true);
                    } else if (methodInvocationExpression == null) {
                        indices.setIsDelegator(true);
                    }
                }
            }
        }
    }


    public static SimpleName judgeMethodIsGetter(MethodDeclaration methodDeclaration) {
        org.eclipse.jdt.core.dom.Block methodBody = methodDeclaration.getBody();
        List<SingleVariableDeclaration> parameters = methodDeclaration.parameters();
        if (methodBody != null) {
            List<Statement> statements = methodBody.statements();
            if (statements.size() == 1 && parameters.size() == 0) {
                Statement statement = statements.get(0);
                if (statement instanceof ReturnStatement) {
                    ReturnStatement returnStatement = (ReturnStatement) statement;
                    Expression returnStatementExpression = returnStatement.getExpression();
                    if (returnStatementExpression instanceof SimpleName) {
                        return (SimpleName) returnStatementExpression;
                    } else if (returnStatementExpression instanceof FieldAccess) {
                        FieldAccess fieldAccess = (FieldAccess) returnStatementExpression;
                        return fieldAccess.getName();
                    }
                }
            }
        }
        return null;
    }

    public boolean overridesMethod(MethodDeclaration methodDeclaration) {
        try {
            IMethodBinding methodBinding = methodDeclaration.resolveBinding();
            ITypeBinding declaringClassTypeBinding = methodBinding.getDeclaringClass();
            Set<ITypeBinding> typeBindings = new LinkedHashSet<>();
            ITypeBinding superClassTypeBinding = declaringClassTypeBinding.getSuperclass();
            if (superClassTypeBinding != null) {
                if (!superClassTypeBinding.getQualifiedName().equals("java.lang.Object")) {
                    typeBindings.add(superClassTypeBinding);
                }
            }
            ITypeBinding[] interfaceTypeBindings = declaringClassTypeBinding.getInterfaces();
            for (ITypeBinding interfaceTypeBinding : interfaceTypeBindings) {
                if (!interfaceTypeBinding.getQualifiedName().equals("java.io.Serializable")) {
                    typeBindings.add(interfaceTypeBinding);
                }
            }
            return overridesMethod(methodDeclaration, typeBindings);
        } catch (NullPointerException nullPointerException) {
//            System.out.println("error in overridesMethod(MethodDeclaration methodDeclaration) while resolving typeBindings," + "[funcImpl name] : " + indices.getObject() + ", [location] : " + indices.getLocation().getLine() + "," + indices.getLocation().getRow());
        }
        return false;
    }

    private boolean overridesMethod(MethodDeclaration methodDeclaration, Set<ITypeBinding> typeBindings) {
        try {
            IMethodBinding methodBinding = methodDeclaration.resolveBinding();
            Set<ITypeBinding> superTypeBindings = new LinkedHashSet<>();
            for (ITypeBinding typeBinding : typeBindings) {
                ITypeBinding superClassTypeBinding = typeBinding.getSuperclass();
                if (superClassTypeBinding != null)
                    superTypeBindings.add(superClassTypeBinding);
                ITypeBinding[] interfaceTypeBindings = typeBinding.getInterfaces();
                superTypeBindings.addAll(Arrays.asList(interfaceTypeBindings));
                if (typeBinding.isInterface()) {
                    IMethodBinding[] interfaceMethodBindings = typeBinding.getDeclaredMethods();
                    for (IMethodBinding interfaceMethodBinding : interfaceMethodBindings) {
                        if (methodBinding.overrides(interfaceMethodBinding)
                                || methodBinding.toString().equals(interfaceMethodBinding.toString()))
                            return true;
                    }
                } else {
                    IMethodBinding[] superClassMethodBindings = typeBinding.getDeclaredMethods();
                    for (IMethodBinding superClassMethodBinding : superClassMethodBindings) {
                        if (methodBinding.overrides(superClassMethodBinding)
                                || (methodBinding.toString().equals(superClassMethodBinding.toString())
                                && (superClassMethodBinding.getModifiers() & Modifier.PRIVATE) == 0))
                            return true;
                    }
                }
            }
            if (!superTypeBindings.isEmpty()) {
                return overridesMethod(methodDeclaration, superTypeBindings);
            } else
                return false;
        } catch (NullPointerException nullPointerException) {
//            System.out.println("error in (MethodDeclaration methodDeclaration, Set<ITypeBinding> typeBindings) while judging" + "[funcImpl name] : " + indices.getObject() + ", [location] : " + indices.getLocation().getLine() + "," + indices.getLocation().getRow());
        }
        return false;
    }

}
