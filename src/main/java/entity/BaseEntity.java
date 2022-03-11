package entity;

import java.util.ArrayList;
import util.Tuple;

public class BaseEntity {

    protected String qualifiedName;
    protected String name;
    protected int id = -1;
    protected int parentId = -1;
    protected ArrayList<Integer> childrenIds = new ArrayList<Integer>();
    //protected String codeSnippet;
    //String is the relation type , and Integer is corresponding entity's id
    protected ArrayList<Tuple<String, Integer>> relation = new ArrayList<Tuple<String, Integer>>();

    protected ArrayList<String> annotations = new ArrayList<>();

    protected ArrayList<String> reflects = new ArrayList<>();

    /**
     * check whether the entities are hidden
     * 3 situations
     * package " com.android.internal"
     * last line of a comment "@hide"
     * annotate "UnsupportedAppUsage(maxTargetSdk=)"
     */
    protected boolean hidden = false;

    protected int maxTargetSdk = 0;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String name) {
        this.qualifiedName = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void addChildId (int id){
        this.childrenIds.add(id);
    }

    public void addChildrenIds (ArrayList<Integer> ids){
        this.childrenIds.addAll(ids);
    }

    public ArrayList<Integer> getChildrenIds() {
        return childrenIds;
    }

//    public String getCodeSnippet() {
//        return codeSnippet;
//    }
//
//    public void setCodeSnippet(String codeSnippet) {
//        this.codeSnippet = codeSnippet;
//    }

    public void addRelation(String relation, int relatedId){
        this.relation.add(new Tuple<String, Integer>(relation, relatedId));
    }

    public ArrayList<Tuple<String, Integer>> getRelation(){
        return this.relation;
    }

    public void addAnnotation(String annotation){
        this.annotations.add(annotation);
    }

    public ArrayList<String> getAnnotations(){
        return this.annotations;
    }

    public void addReflect(String reflect){
        this.reflects.add(reflect);
    }

    public ArrayList<String> getReflects(){
        return this.reflects;
    }

    public boolean getHidden(){
        return this.hidden;
    }

    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    public int getMaxTargetSdk(){
        return this.maxTargetSdk;
    }

    public void setMaxTargetSdk(int maxTargetSdk) {
        this.maxTargetSdk = maxTargetSdk;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", simpleName='" + name + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                ", childrenIds=" + childrenIds +
                //", codeSnippet='" + codeSnippet + '\'' +
                ", relation=" + relation +
                '}';
    }
}
