package entity.properties;

public class MethodProperties {

    boolean isOverride = false;
    boolean isSetter = false;
    boolean isGetter = false;
    boolean isDelegator = false;
    boolean isRecursive = false;
    boolean isSynchronized = false;
    boolean isConstructor = false;

    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean override) {
        isOverride = override;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public void setSetter(boolean setter) {
        isSetter = setter;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public void setGetter(boolean getter) {
        isGetter = getter;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean recursive) {
        isRecursive = recursive;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }


    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public boolean isDelegator() {
        return isDelegator;
    }

    public void setDelegator(boolean delegator) {
        isDelegator = delegator;
    }
}
