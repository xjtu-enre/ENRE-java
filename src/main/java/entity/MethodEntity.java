package entity;

import entity.properties.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodEntity extends ScopeEntity{

    protected ArrayList<Integer> parameters = new ArrayList<Integer>();
    protected String returnType = null;
    protected String returnExpression = null;
    protected boolean isConstructor = false;

    //record the declared class's qualified name of called method and its name
    //like declared class-called method
    protected ArrayList<String> call = new ArrayList<>();
    //record the called super method name
    protected ArrayList<String> callNondynamic = new ArrayList<>();

    //record the id of blocks in method
    protected ArrayList<Block> blocks = new ArrayList<>();

    //record the initial local var name
    protected ArrayList<VariableEntity> localVars = new ArrayList<>();

    //record var information
    protected HashMap<String, Integer> name2Id = new HashMap<>();
    protected HashMap<String, String> name2Role = new HashMap<>();
    protected HashMap<String, ArrayList<String>> name2Usage = new HashMap<>();

    public MethodEntity(int methodId, String methodName){
        this.id = methodId;
        this.name = methodName;
    }

    public int searchLocalVar(String varName, int localBlockId){
        if(localBlockId == -1) {
            return -1;
        }
        for(VariableEntity var : this.getLocalVars()){
            if(var.getName().equals(varName) && isCurrentVarCover(var.getBlockId(), localBlockId)){
                return var.getId();
            }
        }
        return -1;
    }

    public boolean isCurrentVarCover(int currentBlockId, int inputBlockId){
        if(currentBlockId == -1 || inputBlockId ==-1){
            return false;
        }
        if(currentBlockId == inputBlockId){
            return true;
        }
        if(this.getBlocks().get(currentBlockId).getDepth() < this.getBlocks().get(inputBlockId).getDepth()){
            return true;
        }
        return false;
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

    public void addCall(String className){
        this.call.add(className);
    }

    public  ArrayList<String> getCall() {
        return this.call;
    }

    public void addCallNondynamic(String name){
        this.callNondynamic.add(name);
    }

    public  ArrayList<String> getCallNondynamic() {
        return this.callNondynamic;
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

    public HashMap<String, Integer> getName2Id(){
        return this.name2Id;
    }

    public void addName2Id(String name, int id){
        if(!this.name2Id.containsKey(name)){
            this.name2Id.put(name, id);
        }
    }

    public HashMap<String, String> getName2Role(){ return this.name2Role; }

    public void addName2Role (String name, String role){
        if(!this.name2Role.containsKey(name)){
            this.name2Role.put(name, role);
        }
    }

    public HashMap<String, ArrayList<String>> getName2Usage() {
        return name2Usage;
    }

    public void addName2Usage(String name, String usage){
        if (!name2Usage.containsKey(name)) {
            name2Usage.put(name, new ArrayList<String>());
        }
        if(!name2Usage.get(name).contains(usage)) {
            name2Usage.get(name).add(usage);
        }
    }


    @Override
    public String toString() {
        return "MethodEntity{" +
                "parameters=" + parameters +
                ", returnType='" + returnType + '\'' +
                ", returnExpression='" + returnExpression + '\'' +
                ", isConstructor=" + isConstructor +
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
