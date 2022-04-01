package visitor;

import entity.*;
import entity.properties.Location;
import org.eclipse.jdt.core.dom.*;
import util.Configure;
import util.PathUtil;
import util.SingleCollect;

import java.util.ArrayList;
import java.util.Stack;

public class EntityVisitor extends ASTVisitor {

    //current file full path now visiting
    private String fileFullPath = null;
    //current complication unit
    private CompilationUnit cu;

    private final ProcessEntity processEntity = new ProcessEntity();
    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    //entity stack
    private final Stack<Integer> scopeStack = new Stack<>();
    private final Stack<Integer> entityStack = new Stack<>();

    //block stack
    private final Stack<Integer> blockStack = new Stack<Integer>();


    public EntityVisitor (String fileFullPath, CompilationUnit compilationUnit){
        this.fileFullPath = fileFullPath;
        this.cu = compilationUnit;
    }

    /**
     * Visit a module entity
     * The information of module declaration could only be acquired from the module-info.java file.
     * And all the dependencies related to this module are all provided in it, it only have required modules and exported
     * packages, which are its children.
     * @param node ASTNode
     * @return true
     */
    @Override
    public boolean visit(ModuleDeclaration node){
        if(scopeStack.isEmpty()){
            int fileId = processEntity.processFile(fileFullPath,-1);
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
            //if it already been created
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

    /**
     *
     * @param node
     * @return true
     */
    @Override
    public boolean visit(ImportDeclaration node){
        //before process the import declaration, first created file entity
        //current file
        int fileId = createAFile();

        Location loc = ProcessEntity.supplement_location(cu, node.getStartPosition(), node.getLength());

        if(node.isStatic()){
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportStatic(node.getName().getFullyQualifiedName(), loc);
        }
        else if(node.isOnDemand()){
            //add package
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportOnDemand(node.getName().getFullyQualifiedName(), loc);
        } else {
            //add file
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportClass(node.getName().getFullyQualifiedName(), loc);
        }

        return super.visit(node);
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
        int  typeId = processEntity.processType(node, parentId, this.cu);

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

    /**
     * Visit a Enum Declaration node and create an enum entity
     * @param node
     * @return
     */
    @Override
    public boolean visit(EnumDeclaration node){

        int fileId = createAFile();

        int enumId = processEntity.processEnum(node, scopeStack.peek(), cu);

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

        processEntity.processEnumConstant(node, scopeStack.peek());
        return super.visit(node);
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {

        if(scopeStack.isEmpty()){
            createAFile();
        }

        int annotationId = processEntity.processAnnotation(node, scopeStack.peek(), cu);
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
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName);

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
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName);

        //check hidden
        if ("UnsupportedAppUsage".equals(annotationName)){
            singleCollect.getEntityById(entityStack.peek()).setHidden(true);
        }

        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {

        String annotationName = node.getTypeName().toString();
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName);

        //check hidden
        if ("UnsupportedAppUsage".equals(annotationName)){
            singleCollect.getEntityById(entityStack.peek()).setHidden(true);
            for (Object value : node.values()){
                if (value instanceof MemberValuePair){
                    if ("maxTargetSdk".equals(((MemberValuePair) value).getName().toString())){
                        singleCollect.getEntityById(entityStack.peek()).setMaxTargetSdk(getMaxTargetSdk(((MemberValuePair) value).getValue().toString()));
                    }
                }
            }
        }

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
        int annotationMember = processEntity.processAnnotationMember(node, scopeStack.peek());
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
        int methodId = processEntity.processMethod(node, scopeStack.peek(), cu);
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

        //pop block stack
        blockStack.pop();

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
        String varType = node.getType().toString();
        if(blockStack.isEmpty()){
            /**
             * static initial
             */
//            System.out.println(node.toString());
//            System.out.println(fileFullPath);
        }else{
//            System.out.println(blockStackForMethod.peek());
//            int currentBlock = blockStackForMethod.peek();
            ArrayList<String> modifiers = new ArrayList<>();
            for(Object o : node.modifiers()){
                modifiers.add(o.toString());
            }
            methodVarId.addAll(processEntity.processVarDeclFragment(node.fragments(), scopeStack.peek(),varType, blockStack.peek(), -1, false, modifiers, cu));
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

        //get the var type
        String varType = node.getType().toString();
        //the class var belong to type block, so the block id is typeId
        int typeId = scopeStack.peek();

        //check the static field
        int staticFlag = -1;
        for(Object o : node.modifiers()){
            if(o.toString().equals("static") &&  singleCollect.getEntityById(typeId) instanceof ClassEntity){
                staticFlag = 1;
            }
        }

        ArrayList<String> modifiers = new ArrayList<>();
        for(Object o : node.modifiers()){
            modifiers.add(o.toString());
        }

        /**
         * change block id from type id to -1
         */
        classVarId.addAll(processEntity.processVarDeclFragment(node.fragments(),typeId,varType, -1,staticFlag, true, modifiers, cu));

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

        ArrayList<Integer> forVar = processEntity.processVarDeclFragment(node.fragments(), scopeStack.peek(), node.getType().toString(), blockStack.peek(), -1, false, modifiers, cu);

        //supplement method children's id
        singleCollect.getEntityById(scopeStack.peek()).addChildrenIds(forVar);

        for (int varId : forVar){
            entityStack.push(varId);
        }

        return super.visit(node);
    }



    /**
     * Visit a method parameter node
     *
     * @param node
     * @return
     */
    @Override
    public boolean visit(SingleVariableDeclaration node) {

        int parId;

        //get the parameter's type
        String parType = node.getType().toString();
        int methodId = scopeStack.peek();
        parId = processEntity.processSingleVar(node.getName().getFullyQualifiedName(), methodId, parType);
        entityStack.push(parId);
        /**
         * enhanced for-stmt init before method
         */
        if(!blockStack.isEmpty()){
            ((VariableEntity)singleCollect.getEntityById(parId)).setBlockId(blockStack.peek());
        }
        //current block is method
        if(blockStack.size() == 1){
            ((MethodEntity) singleCollect.getEntityById(methodId)).addParameter(parId);
        }

        return super.visit(node);
    }

    @Override
    public void endVisit(SingleVariableDeclaration node) {
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

        if (methodBinding != null) {
            ITypeBinding declaringClass = methodBinding.getDeclaringClass();
            String declaringTypeQualifiedName = declaringClass.getQualifiedName();

            if(singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity){
                ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCall(declaringTypeQualifiedName+"-"+methodName, loc);
            }

            //check reflection
            if(declaringTypeQualifiedName.equals("java.lang.Class") ){
                if(methodName.equals("forName") && node.arguments().size() == 1){
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(node.arguments().get(0).toString().replace("\"", ""));
                }
                if(methodName.equals("getMethod")){
                    singleCollect.getEntityById(scopeStack.peek()).addReflect(node.arguments().toString());
                }
            }
        }

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

    /**
     * record the assignment information
     * @param node
     * @return
     */
    @Override
    public boolean visit(Assignment node){
        //for a local var
        if(singleCollect.getEntityById(scopeStack.peek()) instanceof MethodEntity){
            int methodId = scopeStack.peek();
            String varName;
            if(node.getLeftHandSide() instanceof SimpleName){
                varName = node.getLeftHandSide().toString();
                processVarInMethod(varName ,methodId);
                if(((MethodEntity)singleCollect.getEntityById(methodId)).getName2Id().containsKey(varName)){
                    int varId = ((MethodEntity)singleCollect.getEntityById(methodId)).getName2Id().get(varName);
                    /**
                     * first set separately, not a parameter
                     */
                    if(!((MethodEntity)singleCollect.getEntityById(methodId)).getParameters().contains(varId) && varId != -1){
                        if (((VariableEntity)singleCollect.getEntityById(varId)).getValue() == null){
                            ((VariableEntity)singleCollect.getEntityById(varId)).setSetBy(methodId);
                            ((VariableEntity)singleCollect.getEntityById(varId)).setValue(node.getRightHandSide().toString());
                            ((MethodEntity)singleCollect.getEntityById(methodId)).addName2Usage(varName, "set");
                        }
                        else {
                            ((MethodEntity)singleCollect.getEntityById(methodId)).addName2Usage(varName, "modify");
                        }
                    }
                }
            }
            if(node.getRightHandSide() instanceof SimpleName){
                varName = node.getRightHandSide().toString();
                processVarInMethod(varName ,methodId);
                /**
                 * use, read only
                 */
                if(((MethodEntity)singleCollect.getEntityById(methodId)).getName2Id().containsKey(varName)) {
                    ((MethodEntity) singleCollect.getEntityById(methodId)).addName2Usage(varName, "use");
                }
            }
        }

        //ck
        singleCollect.addCk(Configure.ASSIGNMENTS, 1);
        return super.visit(node);
    }


    @Override
    public boolean visit(ForStatement node){
        if(scopeStack.peek() != -1){
            if(singleCollect.isMethod(scopeStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_FOR);
                blockStack.push(localBlockId);
            }
        }

        //CK
        singleCollect.addCk(Configure.LOOPS, 1);

        return super.visit(node);
    }

    @Override
    public void endVisit(ForStatement node){
        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(EnhancedForStatement node){
        if(scopeStack.peek() != -1){
            /**
             * static initial
             */
            if(singleCollect.isMethod(scopeStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_ENHANCED_FOR);
                blockStack.push(localBlockId);
            }
        }

        //CK
        singleCollect.addCk(Configure.LOOPS, 1);

        return super.visit(node);
    }

    @Override
    public void endVisit(EnhancedForStatement node){
        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(IfStatement node) {
        if(scopeStack.peek() != -1){
            if(singleCollect.isMethod(scopeStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_IF);
                blockStack.push(localBlockId);
            }
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(IfStatement node){
        if(scopeStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(CastExpression node){
        if(scopeStack.peek() != -1 && !scopeStack.isEmpty() && (singleCollect.getEntityById(scopeStack.peek()) instanceof ScopeEntity)){
            ((ScopeEntity) singleCollect.getEntityById(scopeStack.peek())).addCastype(node.getType().toString());
        }
        return super.visit(node);
    }


    /**
     * check Hidden API
     * Entities with the javadoc attribute '@hide', itself and all children node are hidden
     * @param node Javadoc node
     * @return
     */
    @Override
    public boolean visit(Javadoc node) {
        for ( Object tag : node.tags()){
            if ("@hide".equals(((TagElement) tag).getTagName()) && ((TagElement) tag).getTagName() != null){
                singleCollect.getEntityById(entityStack.peek()).setHidden(true);
            }
            for ( Object sub : ((TagElement) tag).fragments()){
                if ( sub instanceof TagElement && "@hide".equals(((TagElement) sub).getTagName())){
                    singleCollect.getEntityById(entityStack.peek()).setHidden(true);
                }
            }
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(TryStatement node) {
        //ck
        singleCollect.addCk(Configure.TRY_CATCHES, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(NumberLiteral node){
        //ck
        singleCollect.addCk(Configure.NUMBER, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node){

        return super.visit(node);
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
            fileId = processEntity.processFile(fileFullPath,-1);
            //add file id into the top of stack
            scopeStack.push(fileId);
        }else if(singleCollect.isPackage(scopeStack.peek())){
            //if current file is not created and has pkg declaration
            fileId = processEntity.processFile(fileFullPath, scopeStack.peek());
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


    private int searchVarInType(String varName){
        int typeId = singleCollect.getEntityById(scopeStack.peek()).getParentId();
        for(int id : singleCollect.getEntityById(typeId).getChildrenIds()){
            if(singleCollect.getEntityById(id) instanceof VariableEntity
                    && singleCollect.getEntityById(id).getName().equals(varName)){
                return id;
            }
        }
        return -1;
    }

    private int searchVarInPara(String varName){
        int methodId = scopeStack.peek();
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

    public void processVarInMethod(String name, int methodId){
        int varId = -1;
        String role = "Local";
        //search in method block, local var
//        System.out.println(blockStackForMethod.peek());
        varId = ((MethodEntity)singleCollect.getEntityById(methodId)).searchLocalVar(name, blockStack.peek());
        //global var
        if(varId == -1){
            varId = searchVarInType(name);
            role = "global";
        }
        //parameter
        if(varId == -1){
            varId = searchVarInPara(name);
            role = "parameter";
        }
        if(varId == -1){
            /**
             * FINAL STATIC
             */
        }else {
            ((MethodEntity)singleCollect.getEntityById(methodId)).addName2Id(name, varId);
            ((MethodEntity)singleCollect.getEntityById(methodId)).addName2Role(name, role);
        }
    }

    public int getMaxTargetSdk(String maxTargetSdk){
        switch (maxTargetSdk){
            case "Build.VERSION_CODES.BASE":
                return Configure.MAX_TARGETSDK_BASE;
            case "Build.VERSION_CODES.BASE_1_1":
                return Configure.MAX_TARGETSDK_BASE_1_1;
            case "Build.VERSION_CODES.CUPCAKE":
                return Configure.MAX_TARGETSDK_CUPCAKE;
            case "Build.VERSION_CODES.CUR_DEVELOPMENT":
                return Configure.MAX_TARGETSDK_CUR_DEVELOPMENT;
            case "Build.VERSION_CODES.DONUT":
                return Configure.MAX_TARGETSDK_DONUT;
            case "Build.VERSION_CODES.ECLAIR":
                return Configure.MAX_TARGETSDK_ECLAIR;
            case "Build.VERSION_CODES.ECLAIR_0_1":
                return Configure.MAX_TARGETSDK_ECLAIR_0_1;
            case "Build.VERSION_CODES.ECLAIR_MR1":
                return Configure.MAX_TARGETSDK_ECLAIR_MR1;
            case "Build.VERSION_CODES.FROYO":
                return Configure.MAX_TARGETSDK_FROYO;
            case "Build.VERSION_CODES.GINGERBREAD":
                return Configure.MAX_TARGETSDK_GINGERBREAD;
            case "Build.VERSION_CODES.GINGERBREAD_MR1":
                return Configure.MAX_TARGETSDK_GINGERBREAD_MR1;
            case "Build.VERSION_CODES.HONEYCOMB":
                return Configure.MAX_TARGETSDK_HONEYCOMB;
            case "Build.VERSION_CODES.HONEYCOMB_MR1":
                return Configure.MAX_TARGETSDK_HONEYCOMB_MR1;
            case "Build.VERSION_CODES.HONEYCOMB_MR2":
                return Configure.MAX_TARGETSDK_HONEYCOMB_MR2;
            case "Build.VERSION_CODES.ICE_CREAM_SANDWICH":
                return Configure.MAX_TARGETSDK_ICE_CREAM_SANDWICH;
            case "Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1":
                return Configure.MAX_TARGETSDK_ICE_CREAM_SANDWICH_MR1;
            case "Build.VERSION_CODES.JELLY_BEAN":
                return Configure.MAX_TARGETSDK_JELLY_BEAN;
            case "Build.VERSION_CODES.JELLY_BEAN_MR1":
                return Configure.MAX_TARGETSDK_JELLY_BEAN_MR1;
            case "Build.VERSION_CODES.JELLY_BEAN_MR2":
                return Configure.MAX_TARGETSDK_JELLY_BEAN_MR2;
            case "Build.VERSION_CODES.KITKAT":
                return Configure.MAX_TARGETSDK_KITKAT;
            case "Build.VERSION_CODES.KITKAT_WATCH":
                return Configure.MAX_TARGETSDK_KITKAT_WATCH;
            case "Build.VERSION_CODES.LOLLIPOP":
                return Configure.MAX_TARGETSDK_LOLLIPOP;
            case "Build.VERSION_CODES.LOLLIPOP_MR1":
                return Configure.MAX_TARGETSDK_LOLLIPOP_MR1;
            case "Build.VERSION_CODES.M":
                return Configure.MAX_TARGETSDK_M;
            case "Build.VERSION_CODES.N":
                return Configure.MAX_TARGETSDK_N;
            case "Build.VERSION_CODES.N_MR1":
                return Configure.MAX_TARGETSDK_N_MR1;
            case "Build.VERSION_CODES.O":
                return Configure.MAX_TARGETSDK_O;
            case "Build.VERSION_CODES.O_MR1":
                return Configure.MAX_TARGETSDK_O_MR1;
            case "Build.VERSION_CODES.P":
                return Configure.MAX_TARGETSDK_P;
            case "Build.VERSION_CODES.Q":
                return Configure.MAX_TARGETSDK_Q;
            case "Build.VERSION_CODES.R":
                return Configure.MAX_TARGETSDK_R;
            case "Build.VERSION_CODES.S":
                return Configure.MAX_TARGETSDK_S;
            case "Build.VERSION_CODES.S_V2":
                return Configure.MAX_TARGETSDK_S_V2;
        }
        return 0;
    }

//    public boolean isCurrentEntityVar(int id){
//        return (singleCollect.getEntityById(id) instanceof VariableEntity)
//                || (singleCollect.getEntityById(id) instanceof EnumConstantEntity);
//    }
}
