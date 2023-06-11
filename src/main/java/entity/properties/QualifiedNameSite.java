package entity.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QualifiedNameSite {
    private Location location;
    private String createdTypeQualifiedName;
    private String varName;

    public QualifiedNameSite(Location loc, String createdTypeQualifiedName, String varName){
        this.location = loc;
        this.createdTypeQualifiedName = createdTypeQualifiedName;
        this.varName = varName;
    }

}
