package entity;

public class VariableEntity extends BaseEntity{

    private String type;
    private String value = null;
    //for local variable
    private int blockId;
    private int setBy;
    private int useBy;
    private int modifyBy;
    protected String accessibility;

    public VariableEntity(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBlockId(int id){
        this.blockId = id;
    }

    public int getBlockId(){
        return this.blockId;
    }

    public void setSetBy(int id){
        this.setBy = id;
    }

    public int getSetBy(){
        return this.setBy;
    }

    public void setAccessibility (String accessibility){
        this.accessibility = accessibility;
    }

    public String getAccessibility(){
        return this.accessibility;
    }

    @Override
    public String toString() {
        return "VariableEntity{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
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
