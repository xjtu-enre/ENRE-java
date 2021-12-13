package entity;

import java.util.List;

public class EnumConstantEntity <V> extends BaseEntity{

    private List<V> arguments = null;

    public EnumConstantEntity(int id, String name, String qualifiedName, int parentId){
        this.id = id;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    public void setArguments(List<V> arguments) {
        this.arguments = arguments;
    }

    public List<V> getArguments(){
        return this.arguments;
    }
}
