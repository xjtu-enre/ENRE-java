package entity.properties;

public class Relation {

    String kind;
    int toEntity;
    Location location;

    /**
     * Record the produce call entity Id
     * eg. for var.meth, it records var Id
     * eg. for var.meth_1.meth_2, it records var Id for both.
     */
    int bindVar = -1;

    public Relation(String kind, int toEntity){
        this.kind = kind;
        this.toEntity = toEntity;
    }

    public Relation(String kind, int toEntity, Location location){
        this.kind = kind;
        this.toEntity = toEntity;
        this.location = location;
    }

    public Relation(String kind, int toEntity, Location location, int bindVar){
        this.kind = kind;
        this.toEntity = toEntity;
        this.location = location;
        this.bindVar = bindVar;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getToEntity() {
        return toEntity;
    }

    public void setToEntity(int toEntity) {
        this.toEntity = toEntity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getBindVar() {
        return this.bindVar;
    }

    public void setBindVar(int bindVar) {
        this.bindVar = bindVar;
    }
}
