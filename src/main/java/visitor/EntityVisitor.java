package visitor;

import entity.*;
import entity.properties.Location;
import entity.properties.QualifiedNameSite;
import entity.properties.ReflectSite;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.TypeParameter;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class EntityVisitor extends CKVisitor {

    //current file full path now visiting
    private String fileFullPath = null;
    //current complication unit
    private CompilationUnit cu;
    //current bin num, which means current project has multiple bins input
    private Tuple<String, Integer> currentBin;

    private final ProcessEntity processEntity = new ProcessEntity();
    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    //entity stack
    private final Stack<Integer> scopeStack = new Stack<>();
    private final Stack<Integer> entityStack = new Stack<>();

    //block stack
    private final Stack<Integer> blockStack = new Stack<Integer>();

    //anonymous class number
    private int anonymousCount = 0;

    private HashMap<String, String> importedType = new HashMap<>();

    private HashMap<String, String> importedStatic = new HashMap<>();
    public EntityVisitor (String fileFullPath, CompilationUnit compilationUnit, Tuple<String, Integer> currentBin){
        this.fileFullPath = fileFullPath;
        this.cu = compilationUnit;
        this.currentBin = currentBin;
    }

    private void updateAnonyCount(){
        this.anonymousCount++;
    }

    private int getAnonymousCount(){
        return this.anonymousCount;
    }

    /**
     * Visit a module entity
     * The information of module declaration could only be acquired from the module-info.java file.
     * And all the dependencies related to this module are all provided in it, it only has required modules and exported
     * packages, which are its children.
     * @param node ASTNode
     * @return true
     */
    @Override
    public boolean visit(ModuleDeclaration node){
        if(scopeStack.isEmpty()){
            int fileId = processEntity.processFile(fileFullPath,-1, currentBin);
            scopeStack.push(fileId);
        }

        return super.visit(node);
    }

    /**
     * Visit a package entity
     * Check whether it has been created and get the parent package from the ASTnode.name
     * @param node ASTNode
     * @return true
     */
    @Override
    public boolean visit(PackageDeclaration node){
        // current package
        String packagePath = PathUtil.deleteLastStrByPathDelimiter(fileFullPath);
        String packageName = PathUtil.getLastStrByPathDelimiter(packagePath);
        String current_pkg_full_name = node.getName().getFullyQualifiedName();
        int packageId;

        //check whether it has been created
        if(singleCollect.getCreatedPackage().containsKey(current_pkg_full_name)){
            //if it already is created
            packageId = singleCollect.getCreatedPackage().get(current_pkg_full_name);
        } else {
            //if it has not been created
            packageId = processEntity.processPackageDecl(current_pkg_full_name,packagePath);
            //add it into created package
            singleCollect.addCreatedPackage(packageId,current_pkg_full_name);
            //check parent package and supplement the parent
            if(current_pkg_full_name.contains(".")){
                processEntity.reslovePackageRelation(current_pkg_full_name,packagePath,packageId);
            }
        }

        //add package id into the top level of stack
        scopeStack.push(packageId);

        createAFile();

        return super.visit(node);
    }

    boolean isImport;
    /**
     *
     * @param node
     * @return true
     */
    @Override
    public boolean visit(ImportDeclaration node){
        //before process the import declaration, first created file entity
        //current file
        isImport = true;
        int fileId = createAFile();

        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        if(node.isStatic()){
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportStatic(node.getName().getFullyQualifiedName(), loc);
            importedStatic.put(PathUtil.getLastStrByDot(node.getName().toString()), node.getName().getFullyQualifiedName());
        }
        else if(node.isOnDemand()){
            //add package
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportOnDemand(node.getName().getFullyQualifiedName(), loc);
        } else {
            //add file
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportClass(node.getName().getFullyQualifiedName(), loc);
            importedType.put(PathUtil.getLastStrByDot(node.getName().toString()), node.getName().getFullyQualifiedName());
        }

        return super.visit(node);
    }

    @Override
    public void endVisit(ImportDeclaration node) {
        isImport = false;
        super.endVisit(node);
    }

    /**
     * Visit a class or interface entity
     * @param node ASTNode
     * @return true
     */
    @Override
    public boolean visit(TypeDeclaration node){
        //if current file doesn't nether import declaration nor package declaration
        //current file
        int fileId = createAFile();

        //current type entity's id
//        if(node.resolveBinding() == null){
//            return super.visit(node);
//        }

        int parentId;
        if(fileId == -1){
            //inner class or interface
            parentId = scopeStack.peek();
        }else {
            parentId = fileId;
        }
        int  typeId = processEntity.processType(node, parentId, this.cu, currentBin);

        if(singleCollect.getEntityById(parentId) instanceof TypeEntity){
            ((TypeEntity) singleCollect.getEntityById(parentId)).addInnerType(typeId);
        }

        //add class or interface id into the top of stack
        scopeStack.push(typeId);
        entityStack.push(typeId);

        return super.visit(node);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        //pop entity stack
        if(!scopeStack.isEmpty()){
            scopeStack.pop();
        }
        super.endVisit(node);
    }

    public String currentInstanceRawType;

    @Override
    public boolean visit(ClassInstanceCreation node) {
        try {
            currentInstanceRawType = node.getType().resolveBinding().getQualifiedName();
        }catch (NullPointerException e) {
            currentInstanceRawType = node.getType().toString();
        }
        IMethodBinding constructorBinding = node.resolveConstructorBinding();
        if (constructorBinding == null) {
            return super.visit(node);
        }
        String methodName = constructorBinding.getName();
        Location location = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        ArrayList<String> arguments = new ArrayList<>();
        for (Object par : node.arguments()) {
            arguments.add(par.toString());
        }
        Expression currentExpression = node.getExpression();
        while (currentExpression instanceof MethodInvocation) {
            currentExpression = ((MethodInvocation) currentExpression).getExpression();
        }
        while (currentExpression instanceof QualifiedName) {
            currentExpression = ((QualifiedName) currentExpression).getQualifier();
        }
        int bindVar;
        String bindVarName = "";
        if (currentExpression instanceof SimpleName) {
            bindVarName = currentExpression.toString();
            bindVar = processVarInMethod(currentExpression.toString(), scopeStack.peek());
        } else if (currentExpression instanceof FieldAccess) {
            bindVarName = ((FieldAccess) currentExpression).getName().getIdentifier();
            bindVar = processVarInMethod(((FieldAccess) currentExpression).getName().getIdentifier(), scopeStack.peek());
        } else if (currentExpression instanceof ArrayAccess) {
            bindVarName = ((ArrayAccess) currentExpression).getArray().toString();
            bindVar = processVarInMethod(((ArrayAccess) currentExpression).getArray().toString(), scopeStack.peek());
        } else {
            if (currentExpression != null) {
                bindVarName = currentExpression.toString();
            }
            bindVar = -1;
        }
        String declaringTypeQualifiedName;
        if (constructorBinding != null) {
            ITypeBinding declaringClass = constructorBinding.getDeclaringClass();
            declaringTypeQualifiedName = declaringClass.getQualifiedName();
            ArrayList<String> parTypes = new ArrayList<>();
            ITypeBinding[] calledMethParTypes = constructorBinding.getParameterTypes();
            for (ITypeBinding parType : calledMethParTypes) {
                parTypes.add(parType.getQualifiedName());
            }
            if (singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity) {
                if (bindVar != -1) {
                    ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCall(declaringTypeQualifiedName, methodName, location, bindVar, parTypes, arguments);
                } else {
                    ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCall(declaringTypeQualifiedName, methodName, location, bindVarName, parTypes, arguments);
                }
            }
            checkReflection(declaringTypeQualifiedName, methodName, bindVar, node.arguments(), location);
        } else {
            if (bindVar != -1) {
                declaringTypeQualifiedName = singleCollect.getEntityById(bindVar).getRawType();
            } else {
                declaringTypeQualifiedName = bindVarName;
            }
            if (singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity) {
                ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addExternalCall(declaringTypeQualifiedName, methodName, location, bindVarName, bindVar, arguments);
            }
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        updateAnonyCount();
        int classId = processEntity.processAnonymous(node, scopeStack.peek(), cu, currentInstanceRawType, currentBin, getAnonymousCount());

        //bindVar
        if (singleCollect.getEntityById(classId) instanceof ClassEntity){
            ((ClassEntity) singleCollect.getEntityById(classId)).setAnonymousBindVar(entityStack.peek());
        }

        scopeStack.push(classId);
        entityStack.push(classId);
        return super.visit(node);
    }

    @Override
    public void endVisit(AnonymousClassDeclaration node) {
        //pop entity stack
        if(!scopeStack.isEmpty()){
            scopeStack.pop();
        }
        entityStack.pop();
        super.endVisit(node);
    }


    /**
     * Visit an Enum Declaration node and create an enum entity
     * @param node
     * @return
     */
    @Override
    public boolean visit(EnumDeclaration node){

        int fileId = createAFile();

        int enumId = processEntity.processEnum(node, scopeStack.peek(), cu, currentBin);

        if(fileId == -1 && singleCollect.getEntityById(scopeStack.peek()) instanceof TypeEntity){
            ((TypeEntity) singleCollect.getEntityById(scopeStack.peek())).addInnerType(enumId);
        }

        scopeStack.push(enumId);
        entityStack.push(enumId);

        return super.visit(node);
    }

    @Override
    public void endVisit(EnumDeclaration node){
        scopeStack.pop();
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {

        processEntity.processEnumConstant(node, scopeStack.peek(), cu, currentBin);
        return super.visit(node);
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {

        if(scopeStack.isEmpty()){
            createAFile();
        }

        int annotationId = processEntity.processAnnotation(node, scopeStack.peek(), cu, currentBin);
        scopeStack.push(annotationId);
        entityStack.push(annotationId);

        return super.visit(node);
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {

        //Annotation type and retention policy record
        if(singleCollect.getEntityById(scopeStack.peek()) instanceof AnnotationEntity){
            switch (node.getTypeName().toString()){
                case "Target" :
                    ((AnnotationEntity) singleCollect.getEntityById(scopeStack.peek())).setTarget(node.getValue().toString());
                    break;
                case "Retention" :
                    ((AnnotationEntity) singleCollect.getEntityById(scopeStack.peek())).setRetention(node.getValue().toString());
                    break;
            }
        }

        String annotationName = node.getTypeName().toString();
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName, loc);

        return super.visit(node);
    }

    @Override
    public boolean visit(MarkerAnnotation node) {

        //Annotation documented and inherited record
        if(singleCollect.getEntityById(scopeStack.peek()) instanceof AnnotationEntity){
            switch (node.getTypeName().toString()){
                case "Documented" :
                    ((AnnotationEntity) singleCollect.getEntityById(scopeStack.peek())).setDocumented(true);
                    break;
                case "Inherited" :
                    ((AnnotationEntity) singleCollect.getEntityById(scopeStack.peek())).setInherited(true);
                    break;
            }
        }
        //add current entities' annotation
        String annotationName = node.getTypeName().toString();
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName, loc);

        //check hidden
//        if ("UnsupportedAppUsage".equals(annotationName)){
//            singleCollect.getEntityById(entityStack.peek()).setHidden(true);
//        }

        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {

        String annotationName = node.getTypeName().toString();
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName, loc);

        //check hidden
//        if ("UnsupportedAppUsage".equals(annotationName)){
//            singleCollect.getEntityById(entityStack.peek()).setHidden(true);
//            for (Object value : node.values()){
//                if (value instanceof MemberValuePair){
//                    if ("maxTargetSdk".equals(((MemberValuePair) value).getName().toString())){
//                        singleCollect.getEntityById(entityStack.peek()).setMaxTargetSdk(getMaxTargetSdk(((MemberValuePair) value).getValue().toString()));
//                    }
//                }
//            }
//        }

        return super.visit(node);
    }

    @Override
    public void endVisit(AnnotationTypeDeclaration node) {
        if(!scopeStack.isEmpty()){
            scopeStack.pop();
        }
        super.endVisit(node);
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        int annotationMember = processEntity.processAnnotationMember(node, scopeStack.peek(), cu, currentBin);
        scopeStack.push(annotationMember);
        entityStack.push(annotationMember);
        return super.visit(node);
    }

    @Override
    public void endVisit(AnnotationTypeMemberDeclaration node) {
        scopeStack.pop();
        entityStack.pop();
        super.endVisit(node);
    }

    /**
     * Visit a method declaration node
     * @param node
     * @return
     */
    @Override
    public boolean visit(MethodDeclaration node){

        //current method entity's id
        //situation?
        if (scopeStack.isEmpty()){
            createAFile();
        }
        int methodId = processEntity.processMethod(node, scopeStack.peek(), cu, currentBin);
        scopeStack.push(methodId);
        entityStack.push(methodId);

        //create a method block
        int localBlockId = createABlock(Configure.LOCAL_BLOCK_METHOD);
        blockStack.push(localBlockId);


        return super.visit(node);
    }

    @Override
    public void endVisit(MethodDeclaration node){
        //pop entity stack
        scopeStack.pop();

        entityStack.pop();

        //pop block stack
        blockStack.pop();

    }

    @Override
    public boolean visit(Block node) {
        if (isIf){
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_IF);
            blockStack.push(localBlockId);
        } else if (isEnhancedFor) {
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_ENHANCED_FOR);
            blockStack.push(localBlockId);
        } else if (isFor) {
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_FOR);
            blockStack.push(localBlockId);
        } else if (isCatch){
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_CATCH);
            blockStack.push(localBlockId);
        } else if (isLambda) {
            int localBlockId = createABlock(Configure.LAMBDA_BLOCK);
            blockStack.push(localBlockId);
        } else if (blockStack.isEmpty()){
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_STATIC);
            blockStack.push(localBlockId);
        } else {
            int localBlockId = createABlock(Configure.LOCAL_BLOCK_UNNAMED_BLOCK);
            blockStack.push(localBlockId);
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(Block node) {
        //pop block stack
//        if (!blockStack.isEmpty() && blockStack.peek() == -1){
            blockStack.pop();
//        }
        super.endVisit(node);
    }

    /**
     * Visit a method variable node
     * @param node
     * @return
     */
    @Override
    public boolean visit(VariableDeclarationStatement node){

        ArrayList<Integer> methodVarId = new ArrayList<Integer>();

        //get the var type
        String rawType;
        try {
            rawType = node.getType().resolveBinding().getQualifiedName();
        } catch (NullPointerException e){
            rawType = node.getType().toString();
        }

        if(blockStack.isEmpty()){
            /**
             * 1. static initial(finish)
             * 2. lambda expression contains if block
             */
            System.out.println("Variable declaration in an empty block !!");
        }else{
//            System.out.println(blockStackForMethod.peek());
//            int currentBlock = blockStackForMethod.peek();
            ArrayList<String> modifiers = new ArrayList<>();
            for(Object o : node.modifiers()){
                modifiers.add(o.toString());
            }
            methodVarId.addAll(processEntity.processVarDeclFragment(node.fragments(), scopeStack.peek(),rawType, blockStack.peek(), -1, false, modifiers, cu, currentBin));
        }

        //supplement method children's id
        singleCollect.getEntityById(scopeStack.peek()).addChildrenIds(methodVarId);

        for (int varId : methodVarId){
            entityStack.push(varId);
        }

        return super.visit(node);
    }


    @Override
    public void endVisit(VariableDeclarationStatement node) {
        int varNum = node.fragments().size();
        while (varNum != 0){
            entityStack.pop();
            varNum--;
        }
        super.endVisit(node);
    }

    /**
     * Visit a class variable node
     * @param node
     * @return
     */
    @Override
    public boolean visit(FieldDeclaration node){

        ArrayList<Integer> classVarId = new ArrayList<>();

        //get the var rawtype
        String rawType;
        try {
            rawType = node.getType().resolveBinding().getQualifiedName();
        } catch (NullPointerException e){
            rawType = node.getType().toString();
        }
        //the class var belong to type block, so the block id is typeId
        int typeId = scopeStack.peek();

        //check the static field
        int staticFlag = -1;

        ArrayList<String> modifiers = new ArrayList<>();
        for(Object o : node.modifiers()){
            modifiers.add(o.toString());
            if(o.toString().equals("static") &&  singleCollect.getEntityById(typeId) instanceof ClassEntity){
                staticFlag = 1;
            }
        }

        /**
         * change block id from type id to -1
         */
        classVarId.addAll(processEntity.processVarDeclFragment(node.fragments(),typeId,rawType, -1,staticFlag, true, modifiers, cu, currentBin));

        //supplement method children's id
        singleCollect.getEntityById(typeId).addChildrenIds(classVarId);

        for(int varId : classVarId){
            entityStack.push(varId);
        }

        return super.visit(node);
    }

    @Override
    public void endVisit(FieldDeclaration node) {
        int varNum = node.fragments().size();
        while (varNum != 0){
            entityStack.pop();
            varNum--;
        }
        super.endVisit(node);
    }

    @Override
    public boolean visit(VariableDeclarationExpression node) {

        ArrayList<String> modifiers = new ArrayList<>();
        for(Object o : node.modifiers()){
            modifiers.add(o.toString());
        }

        String rawType;
//        try {
//            rawType = node.getType().resolveBinding().getQualifiedName();
//        } catch (NullPointerException e){
            rawType = node.getType().toString();
//        }

        ArrayList<Integer> forVar = processEntity.processVarDeclFragment(node.fragments(), scopeStack.peek(), rawType,
                blockStack.peek(), -1, false, modifiers, cu, currentBin);

        //supplement method children's id
        singleCollect.getEntityById(scopeStack.peek()).addChildrenIds(forVar);

        for (int varId : forVar){
            entityStack.push(varId);
        }

        return super.visit(node);
    }

    boolean isParameterDeclaration;

    /**
     * Visit a method parameter node
     *
     * @param node
     * @return
     */
    @Override
    public boolean visit(SingleVariableDeclaration node) {

        int parId;
        isParameterDeclaration = true;

        //get the parameter's type
        String parType;
        try {
            parType = node.getType().resolveBinding().getQualifiedName();
        } catch (NullPointerException e){
            parType = node.getType().toString();
        }
        int methodId = scopeStack.peek();
        parId = processEntity.processSingleVar(node.getName().getFullyQualifiedName(), methodId, parType, currentBin);
        entityStack.push(parId);
        singleCollect.getEntityById(parId).setLocation(ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength()));
        /**
         * enhanced for-stmt init before method
         */
        if(!blockStack.isEmpty()){
            ((VariableEntity)singleCollect.getEntityById(parId)).setBlockId(blockStack.peek());
        }
        //current block is method
        if(blockStack.size() == 1){
            if (blockStack.peek() == -1){
                //methodId actually is Class, enhanced for in static block
                singleCollect.getEntityById(methodId).addChildId(methodId);
            } else {
                ((MethodEntity) singleCollect.getEntityById(methodId)).addParameter(parId);
                ((MethodEntity) singleCollect.getEntityById(methodId)).addParameterType(parType);
            }
        }

        return super.visit(node);
    }

    @Override
    public void endVisit(SingleVariableDeclaration node) {
        isParameterDeclaration = false;
        entityStack.pop();
        super.endVisit(node);
    }

    boolean isTypeParameterDeclaration;

    @Override
    public boolean visit(TypeParameter node) {
        isTypeParameterDeclaration = true;
        int tParId;

        //get the parameter's type
        String tParType;
        try {
            tParType = node.resolveBinding().getDeclaringClass().getQualifiedName();
        } catch (NullPointerException e){
            tParType = node.getName().getIdentifier();
        }
        int classId = scopeStack.peek();
        tParId = processEntity.processTypeParameter(node.getName().getFullyQualifiedName(), classId, tParType, currentBin);
        entityStack.push(tParId);
        singleCollect.getEntityById(tParId).setLocation(ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength()));

        return super.visit(node);
    }

    @Override
    public void endVisit(TypeParameter node) {
        isTypeParameterDeclaration = false;
        entityStack.pop();
        super.endVisit(node);
    }

    /**
     * Visit a return statement node
     * @param node
     * @return
     */
    @Override
    public boolean visit(ReturnStatement node){

        if(singleCollect.isMethod(scopeStack.peek()) && !singleCollect.isConstructor(scopeStack.peek()) && node.getExpression() != null){
            ((MethodEntity) singleCollect.getEntityById(scopeStack.peek())).setReturnExpression(node.getExpression().toString());
        }

        //CK
        singleCollect.addCk(Configure.RETURNS, 1);

        return super.visit(node);
    }

    /**
     * visit a method invocation node
     * resolve the method call dependency
     * @param node
     * @return
     */
    @Override
    public boolean visit(MethodInvocation node){
        String methodName = node.getName().toString();
        IMethodBinding methodBinding = node.resolveMethodBinding();

        //call location
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        //get argument(s) or input parameters
        ArrayList<String> arguments = new ArrayList<>();
        for (Object par : node.arguments()){
            arguments.add(par.toString());
        }

        //the var implement call
        Expression currentExpression = node.getExpression();
        while (currentExpression instanceof MethodInvocation){
            currentExpression = ((MethodInvocation) currentExpression).getExpression();
        }
        while (currentExpression instanceof QualifiedName){
            currentExpression = ((QualifiedName) currentExpression).getQualifier();
        }
        int bindVar;
        String bindVarName = "";
        if (currentExpression instanceof SimpleName){
            bindVarName = currentExpression.toString();
            bindVar = processVarInMethod(currentExpression.toString(), scopeStack.peek());
        }else if (currentExpression instanceof FieldAccess){
            bindVarName = ((FieldAccess) currentExpression).getName().getIdentifier();
            bindVar = processVarInMethod(((FieldAccess) currentExpression).getName().getIdentifier(), scopeStack.peek());
        } else if (currentExpression instanceof ArrayAccess){
            bindVarName = ((ArrayAccess) currentExpression).getArray().toString();
            bindVar = processVarInMethod(((ArrayAccess) currentExpression).getArray().toString(), scopeStack.peek());
        } else {
//            System.out.println(currentExpression);
            if (currentExpression != null){
                bindVarName = currentExpression.toString();
            }
            bindVar = -1;
        }

        String declaringTypeQualifiedName;

        if (methodBinding != null) {
            ITypeBinding declaringClass = methodBinding.getDeclaringClass();
            declaringTypeQualifiedName = declaringClass.getQualifiedName();

            ArrayList<String> parTypes = new ArrayList<>();
            ITypeBinding[] calledMethParTypes = methodBinding.getParameterTypes();
            for (ITypeBinding parType : calledMethParTypes){
                parTypes.add(parType.getQualifiedName());
            }

            if(singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity){
                if (bindVar != -1){
                    ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCall(declaringTypeQualifiedName, methodName, loc, bindVar, parTypes, arguments);
                } else {
                    ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCall(declaringTypeQualifiedName, methodName, loc, bindVarName, parTypes, arguments);
                }

            }

            //check reflection
            checkReflection(declaringTypeQualifiedName, methodName, bindVar, node.arguments(),loc);
        } else {
            if (bindVar != -1){
                declaringTypeQualifiedName = singleCollect.getEntityById(bindVar).getRawType();
            } else {
                declaringTypeQualifiedName = bindVarName;
            }
            if(singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity){
                ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addExternalCall(declaringTypeQualifiedName, methodName, loc, bindVarName, bindVar, arguments);
            }
        }

        return super.visit(node);
    }

    public void checkReflection(String declaringTypeQualifiedName, String methodName, int bindVar, List arguments, Location loc){
        String[] args = arguments.toString().replace("]", "").split(", ");
        switch (declaringTypeQualifiedName){
            case "java.lang.Class" :
                if(methodName.equals("forName") && arguments.size() == 1){
                    ReflectSite ref = new ReflectSite(arguments.get(0).toString().replace("\"", ""), getCurrentLeftSideVar());
                    ref.setLocation(loc);
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
                }
                if(methodName.equals("getMethod") || methodName.equals("getDeclaredMethod")){
                    String refMethName = args[0].replace("\"", "").substring(1);
                    ReflectSite ref = new ReflectSite(refMethName, args, getCurrentLeftSideVar(), bindVar);
                    ref.setLocation(loc);
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
                }
                if (methodName.equals("getField") || methodName.equals("getDeclaredField")){
                    String refFieldName = args[0].replace("\"", "").substring(1);
                    ReflectSite ref = new ReflectSite(refFieldName, getCurrentLeftSideVar(), bindVar);
                    ref.setLocation(loc);
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
                }
                break;
            case "java.lang.Object" :
                if (methodName.equals("getClass") && bindVar != -1){
                    ReflectSite ref = new ReflectSite(singleCollect.getEntityById(bindVar).getRawType(), getCurrentLeftSideVar());
                    ref.setLocation(loc);
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
                }
                break;
            case "java.lang.reflect.Method" :
                switch (methodName) {
                    case "setAccessible":
                        if (arguments.toString().contains("true")){
                            for (ReflectSite reflectSite : singleCollect.getEntityById(scopeStack.peek()).getReflects()){
                                if (reflectSite.getImplementVar() == bindVar && reflectSite.getKind().equals(Configure.REFLECT_METHOD)){
                                    reflectSite.setModifyAccessible(true);
                                    break;
                                }
                            }
                        }
                        break;
                    case "invoke":
                        for (ReflectSite reflectSite : singleCollect.getEntityById(scopeStack.peek()).getReflects()){
                            if (reflectSite.getImplementVar() == bindVar && reflectSite.getKind().equals(Configure.REFLECT_METHOD) && reflectSite.getLocation() == null){
                                reflectSite.setLocation(loc);
                                break;
                            }
                        }
                        break;
                }
        }
        if (declaringTypeQualifiedName.contains("<")){
            String kind = declaringTypeQualifiedName.split("<")[0];
            String declaringClass = declaringTypeQualifiedName.split("<")[1].replace(">","");
            if ("java.lang.Class".equals(kind)){
                if(methodName.equals("getMethod") || methodName.equals("getDeclaredMethod")){
                    String refMethName = args[0].replace("\"", "").substring(1);
                    ReflectSite ref = new ReflectSite(refMethName, args, declaringClass, getCurrentLeftSideVar());
                    ref.setLocation(loc);
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
                }
            }
        }
    }

    public int getCurrentLeftSideVar(){
        if (isAssignment){
            return currentLeftSideVar;
        } else {
            return entityStack.peek();
        }
    }


    /**
     * ( Type | void ) . class
     * check reflection
     * @param node
     * @return
     */
    @Override
    public boolean visit(TypeLiteral node) {
        String type;
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        try {
            type = node.getType().resolveBinding().getQualifiedName();
        }catch (NullPointerException e){
            type = node.getType().toString();
        }
        ReflectSite ref = new ReflectSite(type, getCurrentLeftSideVar());
        ref.setLocation(loc);
        singleCollect.getEntityById(scopeStack.peek()).addReflect(ref);
        return super.visit(node);
    }

    /**
     * visit a super method call, recorded as call non-dynamic
     * @param node
     * @return
     */
    @Override
    public boolean visit(SuperMethodInvocation node){
        String methodName = node.getName().toString();
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        if(singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity){
            ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCallNondynamic(methodName, loc);
        }
        return super.visit(node);
    }

    boolean isAssignment;
    int currentLeftSideVar;

    /**
     * record the assignment information
     * @param node
     * @return
     */
    @Override
    public boolean visit(Assignment node){
        isAssignment = true;
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        //for a local var
        if(singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
            int methodId = scopeStack.peek();
            String varName;
            int leftId = -1;
            if(node.getLeftHandSide() instanceof SimpleName){
                varName = node.getLeftHandSide().toString();
                leftId = processVarInMethod(varName ,methodId);
            }else if(node.getLeftHandSide() instanceof FieldAccess){
                varName = ((FieldAccess)node.getLeftHandSide()).getName().getIdentifier();
                int typeId = singleCollect.getEntityById(methodId).getParentId();
                leftId = searchVarInType(typeId, varName);
            }
            if(leftId != -1){
                currentLeftSideVar = leftId;
//                    if(!((MethodEntity)singleCollect.getEntityById(methodId)).getParameters().contains(varId)){
//                        if (((VariableEntity)singleCollect.getEntityById(varId)).getValue() == null){
                ((VariableEntity)singleCollect.getEntityById(leftId)).setSetBy(methodId);
                ((VariableEntity)singleCollect.getEntityById(leftId)).setValue(node.getRightHandSide().toString());
                ((MethodEntity)singleCollect.getEntityById(methodId)).addId2Usage(leftId, "set", loc);
            }
            int rightId = -1;
            if(node.getRightHandSide() instanceof SimpleName){
                varName = node.getRightHandSide().toString();
                rightId = processVarInMethod(varName ,methodId);
            } else if (node.getRightHandSide() instanceof FieldAccess){
                varName = ((FieldAccess)node.getRightHandSide()).getName().getIdentifier();
                int typeId = singleCollect.getEntityById(methodId).getParentId();
                rightId = searchVarInType(typeId, varName);
            } else if (node.getRightHandSide() instanceof QualifiedName) {
                QualifiedName qualifiedName = (QualifiedName) node.getRightHandSide();
                if (qualifiedName.getQualifier() instanceof SimpleName){
                    String qualifier = ((SimpleName) qualifiedName.getQualifier()).getIdentifier();
                    String name = qualifiedName.getName().getIdentifier();
                    // Imported Class
                    String createdType = null;
                    if (importedType.containsKey(qualifier)){
                        createdType = importedType.get(qualifier);
                    }
                    // var in method
                    if (singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
                        int var = processVarInMethod(qualifier, scopeStack.peek());
                        if (var != -1) createdType = singleCollect.getEntityById(var).getRawType();
                    }
                    // static var
                    if (importedStatic.containsKey(name)){
                        createdType = importedStatic.get(name);
                    }
                    if (createdType != null){
                        ((ScopeEntity)singleCollect.getEntityById(scopeStack.peek())).addQualifiedNameSite(loc, createdType, name);
                    }
                }
            }
            if(rightId != -1) {
                ((MethodEntity) singleCollect.getEntityById(methodId)).addId2Usage(rightId, "use", loc);
            }
        }

        //ck
        singleCollect.addCk(Configure.ASSIGNMENTS, 1);
        return super.visit(node);
    }

    @Override
    public void endVisit(Assignment node) {
        isAssignment = false;
        super.endVisit(node);
    }

    @Override
    public boolean visit(InfixExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node) {

        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        if (node.getOperand() instanceof SimpleName && singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
            int methodId = scopeStack.peek();
            String varName = ((SimpleName) node.getOperand()).getIdentifier();
            int varId = processVarInMethod(varName ,methodId);
            if(!((MethodEntity)singleCollect.getEntityById(methodId)).getParameters().contains(varId)){
                ((MethodEntity)singleCollect.getEntityById(methodId)).addId2Usage(varId, "modify", loc);
            }
        } else {
            // not a var
            return super.visit(node);
        }

        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node) {
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        if (node.getOperand() instanceof SimpleName && singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
            int methodId = scopeStack.peek();
            String varName = ((SimpleName) node.getOperand()).getIdentifier();
            int varId = processVarInMethod(varName ,methodId);
            if(!((MethodEntity)singleCollect.getEntityById(methodId)).getParameters().contains(varId)){
                ((MethodEntity)singleCollect.getEntityById(methodId)).addId2Usage(varId, "modify", loc);
            }
        } else {
            // not a var
            return super.visit(node);
        }

        return super.visit(node);
    }

    boolean isFor = false;
    boolean isEnhancedFor = false;
    boolean isCatch = false;
    boolean isIf = false;

    @Override
    public boolean visit(ForStatement node){
//        if(scopeStack.peek() != -1){
//            if(singleCollect.isMethod(scopeStack.peek())){
//                int localBlockId = createABlock(Configure.LOCAL_BLOCK_FOR);
//                blockStack.push(localBlockId);
//            }
//        }
        isFor = true;

        //CK
        singleCollect.addCk(Configure.LOOPS, 1);

        return super.visit(node);
    }

    @Override
    public void endVisit(ForStatement node){
//        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
//            blockStack.pop();
//        }
        isFor = false;
    }

    @Override
    public boolean visit(EnhancedForStatement node){
//        if(scopeStack.peek() != -1){
//            if(singleCollect.isMethod(scopeStack.peek())){
//                int localBlockId = createABlock(Configure.LOCAL_BLOCK_ENHANCED_FOR);
//                blockStack.push(localBlockId);
//            }
//        }
        isEnhancedFor = true;
        //CK
        singleCollect.addCk(Configure.LOOPS, 1);

        return super.visit(node);
    }

    @Override
    public void endVisit(EnhancedForStatement node){
//        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
//            blockStack.pop();
//        }
        isEnhancedFor = false;
    }

    @Override
    public boolean visit(CatchClause node) {
//        if(scopeStack.peek() != -1){
//            if(singleCollect.isMethod(scopeStack.peek())){
//                int localBlockId = createABlock(Configure.LOCAL_BLOCK_CATCH);
//                blockStack.push(localBlockId);
//            }
//        }
        isCatch = true;
        return super.visit(node);
    }

    @Override
    public void endVisit(CatchClause node) {
//        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
//            blockStack.pop();
//        }
        isCatch = false;
        super.endVisit(node);
    }

    @Override
    public boolean visit(IfStatement node) {
//        if(scopeStack.peek() != -1){
//            if(singleCollect.isMethod(scopeStack.peek())){
//                int localBlockId = createABlock(Configure.LOCAL_BLOCK_IF);
//                blockStack.push(localBlockId);
//            }
//        }
        isIf = true;
        return super.visit(node);
    }

    @Override
    public void endVisit(IfStatement node){
//        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
//            blockStack.pop();
//        }
        isIf = false;
    }

    @Override
    public boolean visit(CastExpression node){
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        if(scopeStack.peek() != -1 && !scopeStack.isEmpty() && (singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity)){
            ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCastype(node.getType().toString(), loc);
        }
        return super.visit(node);
    }


//    /**
//     * check Hidden API
//     * Entities with the javadoc attribute '@hide', itself and all children node are hidden
//     * @param node Javadoc node
//     * @return
//     */
//    @Override
//    public boolean visit(Javadoc node) {
//        for ( Object tag : node.tags()){
//            if ("@hide".equals(((TagElement) tag).getTagName()) && ((TagElement) tag).getTagName() != null){
//                singleCollect.getEntityById(entityStack.peek()).setHidden(true);
//            }
//            for ( Object sub : ((TagElement) tag).fragments()){
//                if ( sub instanceof TagElement && "@hide".equals(((TagElement) sub).getTagName())){
//                    singleCollect.getEntityById(entityStack.peek()).setHidden(true);
//                }
//            }
//        }
//        return super.visit(node);
//    }

    //-----------------------------------------------------------------------------

    private boolean isFieldAccess;
    private boolean isQualifiedName;
    /**
     * This part is identifying the usage of field in method
     * @param node
     * @return
     */
    @Override
    public boolean visit(FieldAccess node) {
        isFieldAccess = true;
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        String fieldName = node.getName().getIdentifier();
        int method = scopeStack.peek();
        int typeId = -1;
        if ((singleCollect.getEntityById(method) instanceof MethodEntity)){
            typeId = singleCollect.getEntityById(method).getParentId();
            int fieldId = searchVarInType(typeId, fieldName);
            if (fieldId != -1)((MethodEntity) singleCollect.getEntityById(method)).addId2Usage(fieldId, "use", loc);
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(FieldAccess node) {
        isFieldAccess = false;
        super.endVisit(node);
    }

    @Override
    public boolean visit(QualifiedName node) {
        isQualifiedName = true;
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());
        if (!isImport && node.getQualifier() instanceof SimpleName){
//            System.out.println(((SimpleName) node.getQualifier()).getIdentifier());
//            System.out.println(node.getName());
            String qualifier = ((SimpleName) node.getQualifier()).getIdentifier();
            String name = node.getName().getIdentifier();
            // Imported Class
            String createdType = null;
            if (importedType.containsKey(qualifier)){
                 createdType = importedType.get(qualifier);
            }
            // var in method
            if (singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
                int var = processVarInMethod(qualifier, scopeStack.peek());
                if (var != -1) createdType = singleCollect.getEntityById(var).getRawType();
            }
            // static var
            if (importedStatic.containsKey(name)){
                createdType = importedStatic.get(name);
            }
            if (createdType != null){
                ((ScopeEntity)singleCollect.getEntityById(scopeStack.peek())).addQualifiedNameSite(loc, createdType, name);
            }
        }

        return super.visit(node);
    }

    @Override
    public void endVisit(QualifiedName node) {
        isQualifiedName = false;
        super.endVisit(node);
    }

    @Override
    public boolean visit(SimpleName node) {
        String varName = node.getIdentifier();
        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        if (!scopeStack.isEmpty() && (singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity) && !isQualifiedName && !isParameterDeclaration && !isTypeParameterDeclaration){
            int varId = processVarInMethod(varName, scopeStack.peek());
            int methodId = scopeStack.peek();
            if (((MethodEntity)singleCollect.getEntityById(methodId)).getId2Usage().containsKey(varId)) {
                for (Tuple<String, Location> usage : ((MethodEntity) singleCollect.getEntityById(methodId)).getId2Usage().get(varId)){
                    if (usage.getR().getStartLine() == loc.getStartLine() && usage.getR().getStartColumn() == loc.getStartColumn()){
                        return super.visit(node);
                    }
                }
                ((MethodEntity) singleCollect.getEntityById(methodId)).addId2Usage(varId, "use", loc);
            }
        }

        return super.visit(node);
    }

    boolean isLambda = false;
    @Override
    public boolean visit(LambdaExpression node){
//        if (blockStack.isEmpty()){
//            int localBlockId = createABlock(Configure.Lambda_BLOCK);
//            blockStack.push(localBlockId);
//        }
        isLambda = true;
        return super.visit(node);
    }
    @Override
    public void endVisit(LambdaExpression node){
//        if (!blockStack.isEmpty() && blockStack.peek() == -1){
//            blockStack.pop();
//        }
        isLambda = false;
    }


    /**
     * For the file entity do not have the corresponding AST node
     * So we need to check the entity stack and create a file entity
     * 4 situations we need to check: Package Declaration, Import Declaration, Type and Method Declaration
     * @return current File Id
     */
    private int createAFile(){
        int fileId;
        if(scopeStack.isEmpty()){
            //if current file do not have package declaration (in unknown pkg)
            fileId = processEntity.processFile(fileFullPath,-1, currentBin);
            //add file id into the top of stack
            scopeStack.push(fileId);
        }else if(singleCollect.isPackage(scopeStack.peek())){
            //if current file is not created and has pkg declaration
            fileId = processEntity.processFile(fileFullPath, scopeStack.peek(), currentBin);
            //add file id into the top of stack
            scopeStack.push(fileId);
        }else if(singleCollect.isFile(scopeStack.peek())){
            //current file has already been created
            fileId = scopeStack.peek();
        }else {
            //inner class or enum
            fileId = -1;
        }

        return fileId;
    }


    private int searchVarInType(int typeId, String varName){
//        int typeId = singleCollect.getEntityById(scopeStack.peek()).getParentId();
        for(int id : singleCollect.getEntityById(typeId).getChildrenIds()){
            if(singleCollect.getEntityById(id) instanceof VariableEntity
                    && varName.equals(singleCollect.getEntityById(id).getName())){
                return id;
            }
        }
        return -1;
    }

    private int searchVarInPara(String varName, int methodId){
        int varId = -1;
        for(int id : ((MethodEntity)singleCollect.getEntityById(methodId)).getParameters()){
            if(singleCollect.getEntityById(id).getName().equals(varName)){
                varId = id;
                break;
            }
        }
        return varId;
    }

    private int createABlock (String blockName){
        int parentBlockId = -1;
        if (!blockStack.isEmpty()) {
            parentBlockId = blockStack.peek();
        }
        int depth = blockStack.size();

        return processEntity.processLocalBlock(scopeStack.peek(), parentBlockId, depth, blockName);
    }

    public int processVarInMethod(String name, int entityId){
        int varId = -1;
        String role = "Local";
        //search in method block, local var
        if (singleCollect.getEntityById(entityId) instanceof MethodEntity) {
            varId = ((MethodEntity) singleCollect.getEntityById(entityId)).searchLocalVar(name, blockStack.peek());
        }
        //parameter
        if(varId == -1 && singleCollect.getEntityById(entityId) instanceof MethodEntity){
            varId = searchVarInPara(name, entityId);
            role = "parameter";
        }
        //global var
        if(varId == -1){
            int tmpId = entityId;
            while (singleCollect.getEntityById(tmpId).getParentId() != -1 && varId == -1){
                tmpId = singleCollect.getEntityById(tmpId).getParentId();
                if (singleCollect.getEntityById(tmpId) instanceof TypeEntity){
                    varId = searchVarInType(tmpId, name);
                }
            }
            role = "global";
        }
        if(varId == -1){
            //Static
//            System.out.println(name);
        }else {
            ((ScopeEntity)singleCollect.getEntityById(entityId)).addName2Id(name, varId);
            ((ScopeEntity)singleCollect.getEntityById(entityId)).addName2Role(name, role);
        }
        return varId;
    }



//    public int getMaxTargetSdk(String maxTargetSdk){
//        switch (maxTargetSdk){
//            case "Build.VERSION_CODES.BASE":
//                return Configure.MAX_TARGETSDK_BASE;
//            case "Build.VERSION_CODES.BASE_1_1":
//                return Configure.MAX_TARGETSDK_BASE_1_1;
//            case "Build.VERSION_CODES.CUPCAKE":
//                return Configure.MAX_TARGETSDK_CUPCAKE;
//            case "Build.VERSION_CODES.CUR_DEVELOPMENT":
//                return Configure.MAX_TARGETSDK_CUR_DEVELOPMENT;
//            case "Build.VERSION_CODES.DONUT":
//                return Configure.MAX_TARGETSDK_DONUT;
//            case "Build.VERSION_CODES.ECLAIR":
//                return Configure.MAX_TARGETSDK_ECLAIR;
//            case "Build.VERSION_CODES.ECLAIR_0_1":
//                return Configure.MAX_TARGETSDK_ECLAIR_0_1;
//            case "Build.VERSION_CODES.ECLAIR_MR1":
//                return Configure.MAX_TARGETSDK_ECLAIR_MR1;
//            case "Build.VERSION_CODES.FROYO":
//                return Configure.MAX_TARGETSDK_FROYO;
//            case "Build.VERSION_CODES.GINGERBREAD":
//                return Configure.MAX_TARGETSDK_GINGERBREAD;
//            case "Build.VERSION_CODES.GINGERBREAD_MR1":
//                return Configure.MAX_TARGETSDK_GINGERBREAD_MR1;
//            case "Build.VERSION_CODES.HONEYCOMB":
//                return Configure.MAX_TARGETSDK_HONEYCOMB;
//            case "Build.VERSION_CODES.HONEYCOMB_MR1":
//                return Configure.MAX_TARGETSDK_HONEYCOMB_MR1;
//            case "Build.VERSION_CODES.HONEYCOMB_MR2":
//                return Configure.MAX_TARGETSDK_HONEYCOMB_MR2;
//            case "Build.VERSION_CODES.ICE_CREAM_SANDWICH":
//                return Configure.MAX_TARGETSDK_ICE_CREAM_SANDWICH;
//            case "Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1":
//                return Configure.MAX_TARGETSDK_ICE_CREAM_SANDWICH_MR1;
//            case "Build.VERSION_CODES.JELLY_BEAN":
//                return Configure.MAX_TARGETSDK_JELLY_BEAN;
//            case "Build.VERSION_CODES.JELLY_BEAN_MR1":
//                return Configure.MAX_TARGETSDK_JELLY_BEAN_MR1;
//            case "Build.VERSION_CODES.JELLY_BEAN_MR2":
//                return Configure.MAX_TARGETSDK_JELLY_BEAN_MR2;
//            case "Build.VERSION_CODES.KITKAT":
//                return Configure.MAX_TARGETSDK_KITKAT;
//            case "Build.VERSION_CODES.KITKAT_WATCH":
//                return Configure.MAX_TARGETSDK_KITKAT_WATCH;
//            case "Build.VERSION_CODES.LOLLIPOP":
//                return Configure.MAX_TARGETSDK_LOLLIPOP;
//            case "Build.VERSION_CODES.LOLLIPOP_MR1":
//                return Configure.MAX_TARGETSDK_LOLLIPOP_MR1;
//            case "Build.VERSION_CODES.M":
//                return Configure.MAX_TARGETSDK_M;
//            case "Build.VERSION_CODES.N":
//                return Configure.MAX_TARGETSDK_N;
//            case "Build.VERSION_CODES.N_MR1":
//                return Configure.MAX_TARGETSDK_N_MR1;
//            case "Build.VERSION_CODES.O":
//                return Configure.MAX_TARGETSDK_O;
//            case "Build.VERSION_CODES.O_MR1":
//                return Configure.MAX_TARGETSDK_O_MR1;
//            case "Build.VERSION_CODES.P":
//                return Configure.MAX_TARGETSDK_P;
//            case "Build.VERSION_CODES.Q":
//                return Configure.MAX_TARGETSDK_Q;
//            case "Build.VERSION_CODES.R":
//                return Configure.MAX_TARGETSDK_R;
//            case "Build.VERSION_CODES.S":
//                return Configure.MAX_TARGETSDK_S;
//            case "Build.VERSION_CODES.S_V2":
//                return Configure.MAX_TARGETSDK_S_V2;
//        }
//        return 0;
//    }

//    public boolean isCurrentEntityVar(int id){
//        return (singleCollect.getEntityById(id) instanceof VariableEntity)
//                || (singleCollect.getEntityById(id) instanceof EnumConstantEntity);
//    }
}
