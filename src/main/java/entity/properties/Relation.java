package entity.properties;

public class Relation {

    String kind;
    int toEntity;
    Location location;



    public Relation(String kind, int toEntity){
        this.kind = kind;
        this.toEntity = toEntity;
    }

    public Relation(String kind, int toEntity, Location location){
        this.kind = kind;
        this.toEntity = toEntity;
        this.location = location;
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

}
