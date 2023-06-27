package util;

import entity.properties.Relation;

public class RelationObj {
    private int fromEntityId;
    private int toEntityId;
    private Relation relation;

    public RelationObj(int fromEntityId, int toEntityId, Relation relation) {
        this.fromEntityId = fromEntityId;
        this.toEntityId = toEntityId;
        this.relation = relation;
    }

    public int getToEntityId() {
        return toEntityId;
    }

    public void setToEntityId(int toEntityId) {
        this.toEntityId = toEntityId;
    }

    public int getFromEntityId() {
        return fromEntityId;
    }

    public void setFromEntityId(int fromEntityId) {
        this.fromEntityId = fromEntityId;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}
