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

    public boolean getIsOverride(){
        return isOverride;
    }

    public void setIsOverride(boolean a){
        this.isOverride = a;
    }

    public boolean getIsSetter(){
        return isSetter;
    }

    public void setIsSetter(boolean a){
        this.isSetter = a;
    }

    public boolean getIsGetter(){
        return isGetter;
    }

    public void setIsGetter(boolean a){
        this.isGetter = a;
    }

    public boolean getIsDelegator(){
        return isDelegator;
    }

    public void setIsDelegator(boolean a){
        this.isDelegator = a;
    }

    public boolean getIsRecursive(){
        return isRecursive;
    }

    public void setIsRecursive(boolean a){
        this.isRecursive = a;
    }

    public boolean getIsPublic(){
        return isPublic;
    }

    public void setIsPublic(boolean a){
        this.isPublic = a;
    }

    public boolean getIsStatic(){
        return isStatic;
    }

    public void setIsStatic(boolean a){
        this.isStatic = a;
    }

    public boolean getIsSynchronized(){
        return isSynchronized;
    }

    public void setIsSynchronized(boolean a){
        this.isSynchronized = a;
    }

    public boolean isConstructor(){
        return isConstructor;
    }

    public void setIsConstructor(boolean a){
        this.isConstructor = a;
    }

    public boolean getMethodIsAbstract(){
        return methodIsAbstract;
    }

    public void setMethodIsAbstract(boolean a){
        this.methodIsAbstract = a;
    }

    public void setIsAssign(boolean a){
        this.isAssign = a;
    }

    public void setIsCallSuper(boolean a){
        this.isCallSuper = a;
    }
}
