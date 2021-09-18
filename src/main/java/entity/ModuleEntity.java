package entity;

public class ModuleEntity extends BaseEntity{
    protected String modulePath;

    public ModuleEntity(int moduleId, String moduleName, String modulePath){
        this.id = moduleId;
        this.name = moduleName;
        this.modulePath = modulePath;
    }

}
