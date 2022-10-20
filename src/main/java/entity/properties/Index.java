package entity.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Index {

    private Boolean isOverride;
    private Boolean isSetter;
    private Boolean isGetter = false;
    private Boolean isDelegator = false;
    private Boolean isRecursive = false;
    private String extendClassType;
    private Boolean typeIsAbstract = false;

    private String extendVarType;
    private Boolean varIsPrivate = false;

    private Boolean isAssign = false;
    private Boolean isPublic = false;
    private Boolean isStatic = false;
    private Boolean isSynchronized = false;
    private Boolean isConstructor = false;
    private Boolean isCallSuper = false;
    private Boolean methodIsAbstract = false;

}
