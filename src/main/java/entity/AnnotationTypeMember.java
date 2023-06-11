package entity;

public class AnnotationTypeMember extends ScopeEntity{

    private String type;
    private String default_value = null;

    public AnnotationTypeMember(int id, String type, String name, String qualifiedName, int parentId){
        this.id = id;
        this.type = type;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

}
