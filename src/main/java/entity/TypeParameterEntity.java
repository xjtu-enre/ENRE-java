package entity;

public class TypeParameterEntity extends BaseEntity{

    protected String declaredClass;

    protected int declaredClassId = -1;

    public TypeParameterEntity(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.rawType = type;
    }

    public String getDeclaredClass() {
        return declaredClass;
    }

    public void setDeclaredClass(String declaredClass) {
        this.declaredClass = declaredClass;
    }

    public int getDeclaredClassId() {
        return declaredClassId;
    }

    public void setDeclaredClassId(int declaredClassId) {
        this.declaredClassId = declaredClassId;
    }
}
