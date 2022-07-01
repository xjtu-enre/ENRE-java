package entity;

import java.util.ArrayList;

import entity.properties.Location;
import entity.properties.ReflectSite;
import entity.properties.Relation;

public class BaseEntity {

    protected String qualifiedName;
    protected String name;
    protected int id = -1;
    protected int parentId = -1;
    protected ArrayList<Integer> childrenIds = new ArrayList<Integer>();
    //protected String codeSnippet;
    //String is the relation type , and Integer is corresponding entity's id
    protected ArrayList<Relation> relation = new ArrayList<>();
    protected Location location = new Location();

    protected ArrayList<String> annotations = new ArrayList<>();

    protected ArrayList<ReflectSite> reflects = new ArrayList<>();

    protected ArrayList<String> modifiers = new ArrayList<>();

    /**
     *
     */
    protected String rawType;

    /**
     * check whether the entities are hidden
     * 3 situations
     * package " com.android.internal"
     * last line of a comment "@hide"
     * annotate "UnsupportedAppUsage(maxTargetSdk=)"
     */
    protected boolean hidden = false;

    protected int maxTargetSdk = 0;

    protected String binPath = null;

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
        this.relation.add(new Relation(relation, relatedId));
    }

    public void addRelation(String relation, int relationId, Location relationLoc){
        this.relation.add(new Relation(relation, relationId, relationLoc));
    }

    public void addRelation(String relation, int relationId, Location relationLoc, int bindVar){
        this.relation.add(new Relation(relation, relationId, relationLoc, bindVar));
    }

    public void addRelation(String relation, int relationId, Location relationLoc, boolean refAccessible, boolean invoke){
        Relation ref = new Relation(relation, relationId, relationLoc);
        ref.setModifyAccessible(refAccessible);
        ref.setInvoke(invoke);
        this.relation.add(ref);

    }

    public ArrayList<Relation> getRelation(){
        return this.relation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addAnnotation(String annotation){
        this.annotations.add(annotation);
    }

    public ArrayList<String> getAnnotations(){
        return this.annotations;
    }

    public void addReflect(ReflectSite reflect){
        this.reflects.add(reflect);
    }

    public ArrayList<ReflectSite> getReflects(){
        return this.reflects;
    }

//    public boolean getHidden(){
//        return this.hidden;
//    }
//
//    public void setHidden(boolean hidden){
//        this.hidden = hidden;
//    }
//
//    public int getMaxTargetSdk(){
//        return this.maxTargetSdk;
//    }
//
//    public void setMaxTargetSdk(int maxTargetSdk) {
//        this.maxTargetSdk = maxTargetSdk;
//    }

    public String getBinPath(){
        return this.binPath;
    }

    public void setBinNum(String binNum){
        this.binPath = binNum;
    }

    public void addModifier (String modifier){
        this.modifiers.add(modifier);
    }

    public void addModifiers (ArrayList<String> modifiers){
        this.modifiers.addAll(modifiers);
    }

    public ArrayList<String> getModifiers(){
        return this.modifiers;
    }

    public void setRawType(String rawType) {
        this.rawType = rawType;
    }

    public String getRawType(){
        return this.rawType;
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
