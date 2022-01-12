package visitor;

import entity.*;
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
//    //current file's parent package id
//    private int packageId = -1;
//    //current file's id
//    private int fileId = -1;
//    //current type id
//    private int typeId = -1;
//    //current method id
//    private int methodId = -1;

    private final ProcessEntity processEntity = new ProcessEntity();
    SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    //entity stack
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
        if(entityStack.isEmpty()){
            int fileId = processEntity.processFile(fileFullPath,-1);
            entityStack.push(fileId);
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
        entityStack.push(packageId);

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

        if(node.isStatic()){
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportStatic(node.getName().getFullyQualifiedName());
        }
        else if(node.isOnDemand()){
            //add package
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportOnDemand(node.getName().getFullyQualifiedName());
        } else {
            //add file
            ((FileEntity) singleCollect.getEntityById(fileId)).addImportClass(node.getName().getFullyQualifiedName());
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
        if(node.resolveBinding() == null){
            return super.visit(node);
        }

        int parentId;
        if(fileId == -1){
            //inner class or interface
            parentId = entityStack.peek();
        }else {
            parentId = fileId;
        }
        int  typeId = processEntity.processType(node, parentId, this.cu);

        if(singleCollect.getEntityById(parentId) instanceof TypeEntity){
            ((TypeEntity) singleCollect.getEntityById(parentId)).addInnerType(typeId);
        }

        //supplement the entity's code snippet
        //singleCollect.getEntities().get(typeId).setCodeSnippet(node.toString());

        //add class or interface id into the top of stack
        entityStack.push(typeId);

        return super.visit(node);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        //pop entity stack
        if(!entityStack.isEmpty()){
            entityStack.pop();
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

        int enumId = processEntity.processEnum(node, entityStack.peek(), cu);

        if(fileId == -1 && singleCollect.getEntityById(entityStack.peek()) instanceof TypeEntity){
            ((TypeEntity) singleCollect.getEntityById(entityStack.peek())).addInnerType(enumId);
        }

        entityStack.push(enumId);

        return super.visit(node);
    }

    @Override
    public void endVisit(EnumDeclaration node){
        entityStack.pop();
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {

        processEntity.processEnumConstant(node, entityStack.peek());
        return super.visit(node);
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {

        createAFile();

        int annotationId = processEntity.processAnnotation(node, entityStack.peek(), cu);
        entityStack.push(annotationId);

        return super.visit(node);
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {

        //Annotation type and retention policy record
        if(singleCollect.getEntityById(entityStack.peek()) instanceof AnnotationEntity){
            switch (node.getTypeName().toString()){
                case "Target" :
                    ((AnnotationEntity) singleCollect.getEntityById(entityStack.peek())).setTarget(node.getValue().toString());
                    break;
                case "Retention" :
                    ((AnnotationEntity) singleCollect.getEntityById(entityStack.peek())).setRetention(node.getValue().toString());
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
        if(singleCollect.getEntityById(entityStack.peek()) instanceof AnnotationEntity){
            switch (node.getTypeName().toString()){
                case "Documented" :
                    ((AnnotationEntity) singleCollect.getEntityById(entityStack.peek())).setDocumented(true);
                    break;
                case "Inherited" :
                    ((AnnotationEntity) singleCollect.getEntityById(entityStack.peek())).setInherited(true);
                    break;
            }
        }

        //add current entities' annotation
        String annotationName = node.getTypeName().toString();
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName);

        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {

        String annotationName = node.getTypeName().toString();
        singleCollect.getEntityById(entityStack.peek()).addAnnotation(annotationName);

        return super.visit(node);
    }

    @Override
    public void endVisit(AnnotationTypeDeclaration node) {
        entityStack.pop();
        super.endVisit(node);
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        int annotationMember = processEntity.processAnnotationMember(node, entityStack.peek());
        entityStack.push(annotationMember);
        return super.visit(node);
    }

    @Override
    public void endVisit(AnnotationTypeMemberDeclaration node) {
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
        if (entityStack.isEmpty()){
            createAFile();
        }
        int methodId = processEntity.processMethod(node, entityStack.peek(), cu);
        entityStack.push(methodId);

        //create a method block
        int localBlockId = createABlock(Configure.LOCAL_BLOCK_METHOD);
        blockStack.push(localBlockId);


        return super.visit(node);
    }

    @Override
    public void endVisit(MethodDeclaration node){
        //pop entity stack
        entityStack.pop();

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
//            if(this.fileFullPath.equals("java/com/android/internal/app/AlertController.java")){
//                System.out.println(node.toString());
//                System.out.println(blockStackForMethod.peek());
//            }
            String accessibility = null;
            for(Object o : node.modifiers()){
                switch (o.toString()) {
                    case "public":
                        accessibility = "Public";
                        break;
                    case "protected":
                        accessibility = "Protected";
                        break;
                    case "private":
                        accessibility = "Private";
                        break;
                }
            }
            methodVarId.addAll(processEntity.processVarDeclFragment(node.fragments(),entityStack.peek(),varType, blockStack.peek(), -1, false, accessibility));
        }

        //supplement method children's id
        singleCollect.getEntityById(entityStack.peek()).addChildrenIds(methodVarId);

//        for (int varId : methodVarId){
//            entityStack.push(varId);
//        }

        return super.visit(node);
    }


    @Override
    public void endVisit(VariableDeclarationStatement node) {
//        int varNum = node.fragments().size();
//        while (varNum != 0){
//            entityStack.pop();
//            varNum--;
//        }
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
        int typeId = entityStack.peek();

        //check the static field
        int staticFlag = -1;
        for(Object o : node.modifiers()){
            if(o.toString().equals("static") &&  singleCollect.getEntityById(typeId) instanceof ClassEntity){
                staticFlag = 1;
            }
        }

        String accessibility = null;
        for(Object o : node.modifiers()){
            switch (o.toString()) {
                case "public":
                    accessibility = "Public";
                    break;
                case "protected":
                    accessibility = "Protected";
                    break;
                case "private":
                    accessibility = "Private";
                    break;
            }
        }

        /**
         * change block id from type id to -1
         */
        classVarId.addAll(processEntity.processVarDeclFragment(node.fragments(),typeId,varType, -1,staticFlag, true, accessibility));

        //supplement method children's id
        singleCollect.getEntityById(typeId).addChildrenIds(classVarId);

//        for(int varId : classVarId){
//            entityStack.push(varId);
//        }

        return super.visit(node);
    }

    @Override
    public void endVisit(FieldDeclaration node) {
//        int varNum = node.fragments().size();
//        while (varNum != 0){
//            entityStack.pop();
//            varNum--;
//        }
        super.endVisit(node);
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
        int methodId = entityStack.peek();
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

        if(singleCollect.isMethod(entityStack.peek()) && !singleCollect.isConstructor(entityStack.peek()) && node.getExpression() != null){
            ((MethodEntity) singleCollect.getEntityById(entityStack.peek())).setReturnExpression(node.getExpression().toString());
        }

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
        if (methodBinding != null) {
            ITypeBinding declaringClass = methodBinding.getDeclaringClass();
            String declaringClassQualifiedName = declaringClass.getQualifiedName();

            if(singleCollect.getEntityById(entityStack.peek()) instanceof MethodEntity){
                ((MethodEntity) singleCollect.getEntityById(entityStack.peek())).addCall(declaringClassQualifiedName+"-"+methodName);
            }

            //check reflection
            if(declaringClassQualifiedName.equals("java.lang.Class") && methodName.equals("forName")){
                if(node.arguments().size() == 1){
                    singleCollect.getEntityById(entityStack.peek()).addReflect(node.arguments().get(0).toString().replace("/\"", ""));
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
        if(singleCollect.getEntityById(entityStack.peek()) instanceof MethodEntity){
            ((MethodEntity) singleCollect.getEntityById(entityStack.peek())).addCallNondynamic(methodName);
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
        if(singleCollect.getEntityById(entityStack.peek()) instanceof MethodEntity){
            int methodId = entityStack.peek();
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
        return super.visit(node);
    }


    @Override
    public boolean visit(ForStatement node){
        if(entityStack.peek() != -1){
            if(singleCollect.isMethod(entityStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_FOR);
                blockStack.push(localBlockId);
            }
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(ForStatement node){
        if(entityStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(EnhancedForStatement node){
        if(entityStack.peek() != -1){
            /**
             * static initial
             */
            if(singleCollect.isMethod(entityStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_ENHANCED_FOR);
                blockStack.push(localBlockId);
            }
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(EnhancedForStatement node){
        if(entityStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(IfStatement node) {
        if(entityStack.peek() != -1){
            if(singleCollect.isMethod(entityStack.peek())){
                int localBlockId = createABlock(Configure.LOCAL_BLOCK_IF);
                blockStack.push(localBlockId);
            }
        }
        return super.visit(node);
    }

    @Override
    public void endVisit(IfStatement node){
        if(entityStack.peek() != -1 && !blockStack.isEmpty()) {
            blockStack.pop();
        }
    }

    @Override
    public boolean visit(CastExpression node){
        if(entityStack.peek() != -1 && !entityStack.isEmpty() && (singleCollect.getEntityById(entityStack.peek()) instanceof ScopeEntity)){
            ((ScopeEntity) singleCollect.getEntityById(entityStack.peek())).addCastype(node.getType().toString());
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node){

        return super.visit(node);
    }


    /**
     * For the file entity do not have the corresponding AST node
     * So we need to check the entity stack and create a file entity
     * @return
     */
    private int createAFile(){
        int fileId;
        if(entityStack.isEmpty()){
            //if current file do not have package declaration (in unknown pkg)
            fileId = processEntity.processFile(fileFullPath,-1);
            //add file id into the top of stack
            entityStack.push(fileId);
        }else if(singleCollect.isPackage(entityStack.peek())){
            //if current file is not created and has pkg declaration
            fileId = processEntity.processFile(fileFullPath,entityStack.peek());
            //add file id into the top of stack
            entityStack.push(fileId);
        }else if(singleCollect.isFile(entityStack.peek())){
            fileId = entityStack.peek();
        }else {
            //inner class or enum
            fileId = -1;
        }

        return fileId;
    }


    private int searchVarInType(String varName){
        int typeId = singleCollect.getEntityById(entityStack.peek()).getParentId();
        for(int id : singleCollect.getEntityById(typeId).getChildrenIds()){
            if(singleCollect.getEntityById(id) instanceof VariableEntity
                    && singleCollect.getEntityById(id).getName().equals(varName)){
                return id;
            }
        }
        return -1;
    }

    private int searchVarInPara(String varName){
        int methodId = entityStack.peek();
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

        return processEntity.processLocalBlock(entityStack.peek(), parentBlockId, depth, blockName);
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

//    public boolean isCurrentEntityVar(int id){
//        return (singleCollect.getEntityById(id) instanceof VariableEntity)
//                || (singleCollect.getEntityById(id) instanceof EnumConstantEntity);
//    }
}
