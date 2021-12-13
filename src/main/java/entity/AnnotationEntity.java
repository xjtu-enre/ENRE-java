package entity;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnotationEntity extends TypeEntity{

    //注解保留的位置和时间
    private String retention;
    //是否包含在用户文档中
    private boolean documented = false;
    //element type
    private String target;
    //是否具有继承性
    private boolean inherited = false;

    private HashMap<String, Integer> members = new HashMap<>();

    public AnnotationEntity(int id, String name, String qualifiedName, int parentId){
        this.id = id;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public boolean isDocumented() {
        return documented;
    }

    public void setDocumented(boolean documented) {
        this.documented = documented;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    public HashMap<String, Integer> getMembers() {
        return members;
    }

    public void addMember(String name, int id) {
        this.members.put(name, id);
    }

}
