package entity;

import org.json.JSONPropertyName;

public class RecordEntity extends BaseEntity {

    protected String Modifier;

    public RecordEntity(String modifier) {
        Modifier = modifier;
    }

    @JSONPropertyName("Modifier")
    public String getModifier() {
        return Modifier;
    }

    public void setModifier(String modifier) {
        Modifier = modifier;
    }
}
