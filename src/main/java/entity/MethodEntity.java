package entity;

import entity.properties.Block;
import entity.properties.Index;
import entity.properties.Location;
import util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodEntity extends ScopeEntity{

    protected ArrayList<Integer> parameters = new ArrayList<Integer>();

    protected ArrayList<String> parameterTypes = new ArrayList<>();
    protected String returnType = null;
    protected String returnExpression = null;
    protected boolean isConstructor = false;
    protected boolean isGenerics = false;
    protected boolean isLambda = false;

    public boolean isLambda() {
        return isLambda;
    }

    public void setLambda(boolean lambda) {
        isLambda = lambda;
    }

    //record the id of blocks in method
    protected ArrayList<Block> blocks = new ArrayList<>();

    Location blockLoc = new Location();

    //record the initial local var name
    protected ArrayList<VariableEntity> localVars = new ArrayList<>();

    //record var information
//    protected HashMap<String, Integer> name2Id = new HashMap<>();
    protected HashMap<String, String> name2Role = new HashMap<>();
    
    protected HashMap<Integer, ArrayList<Tuple<String, Location>>> id2Usage = new HashMap<>();

    protected Index indices;
    public MethodEntity(int methodId, String methodName){
        this.id = methodId;
        this.name = methodName;
    }

    public int searchLocalVar(String varName, int localBlockId){
        if(localBlockId == -1) {
            return -1;
        }
        for(VariableEntity var : this.getLocalVars()){
//            System.out.println(varName);
//            if ("info".equals(varName)){
//                if(var.getName().equals(varName) && isCurrentVarCover(var.getBlockId(), localBlockId)){
//                    return var.getId();
//                }
//            }
//            if(var.getName().equals(varName) && isCurrentVarCover(var.getBlockId(), localBlockId)){
            if(var.getName().equals(varName)){
                return var.getId();
            }
        }
        return -1;
    }

    public boolean isCurrentVarCover(int currentBlockId, int inputBlockId){
//        System.out.println(currentBlockId+" "+inputBlockId);
        if(currentBlockId == -1 || inputBlockId ==-1){
            return false;
        }
        if(currentBlockId == inputBlockId){
            return true;
        }
        return this.getBlocks().get(currentBlockId).getDepth() < this.getBlocks().get(inputBlockId).getDepth();
    }

    public ArrayList<Integer> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Integer> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(int parameterId) {
        parameters.add(parameterId);
    }

    public void addParameterType(String type){
        this.parameterTypes.add(type);
    }

    public ArrayList<String> getParameterTypes(){
        return this.parameterTypes;
    }

    public void setReturnType(String type){
        this.returnType = type;
    }

    public String getReturnType(){
        return returnType;
    }

    public void setReturnExpression(String expression){
        this.returnExpression = expression;
    }

    public String getReturnExpression(){
        return returnExpression;
    }

    public void setConstructor(boolean isCons){
        this.isConstructor = isCons;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setGenerics(boolean isGenerics){
        this.isGenerics = isGenerics;
    }

    public boolean isGenerics() {
        return isGenerics;
    }

    public void addBlock (Block block){
        this.blocks.add(block);
    }

    public ArrayList<Block> getBlocks(){
        return this.blocks;
    }

    public void addLocalVar(VariableEntity var){
        this.localVars.add(var);
    }

    public ArrayList<VariableEntity> getLocalVars(){
        return this.localVars;
    }

//    public HashMap<String, Integer> getName2Id(){
//        return this.name2Id;
//    }

//    public void addName2Id(String name, int id){
//        if(!this.name2Id.containsKey(name)){
//            this.name2Id.put(name, id);
//        }
//    }

    public Location getBlockLoc(){
        return this.blockLoc;
    }

    public void setBlockLoc(Location loc){
        this.blockLoc = loc;
    }

    public HashMap<String, String> getName2Role(){ return this.name2Role; }

    public void addName2Role (String name, String role){
        if(!this.name2Role.containsKey(name)){
            this.name2Role.put(name, role);
        }
    }

    public HashMap<Integer, ArrayList<Tuple<String, Location>>> getId2Usage() {
        return id2Usage;
    }

    public void addId2Usage(int id, String usage, Location loc){
        if (!id2Usage.containsKey(id)) {
            id2Usage.put(id, new ArrayList<>());
        }
        id2Usage.get(id).add(new Tuple<>(usage, loc));
    }

    public void setIndices(Index indices){
        this.indices = indices;
    }

    public Index getIndices(){
        return this.indices;
    }

    @Override
    public String toString() {
        return "MethodEntity{" +
                "parameters=" + parameters +
                ", returnType='" + returnType + '\'' +
                ", returnExpression='" + returnExpression + '\'' +
//                ", isConstructor=" + isConstructor +
                ", location=" + location +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", simpleName='" + name + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                ", childrenIds=" + childrenIds +
                //", codeSnippet='" + codeSnippet + '\'' +
                ", relation=" + relation +
                '}';
    }
}
