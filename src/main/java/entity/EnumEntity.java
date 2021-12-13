package entity;

import java.util.HashMap;

public class EnumEntity extends TypeEntity{

    protected HashMap<String, Integer> interfaces = new HashMap<String, Integer>();
    protected HashMap<String, Integer> constants = new HashMap<String, Integer>();

    public EnumEntity(int id, String name, String qualifiedName, int parentId){
        this.id = id;
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.parentId = parentId;
    }

    /**
     *
     * @param name qualified name
     * @param id initial is -1, supplement when back fill
     */
    public void addInterface(String name, int id){
        this.interfaces.put(name,id);
    }

    public HashMap<String, Integer> getInterfaces(){
        return this.interfaces;
    }

    /**
     *
     * @param name Simple name
     * @param id initial is -1, supplement when back fill
     */
    public void addConstant(String name, int id){
        this.constants.put(name, id);
    }

    public HashMap<String, Integer> getConstants(){
        return this.constants;
    }

}
