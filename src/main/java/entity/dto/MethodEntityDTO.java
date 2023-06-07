package entity.dto;

import org.json.JSONPropertyName;

public class MethodEntityDTO extends InternalEntityDTO {

  private String File;
  private AdditionalBinDTO additionalBin;
  private EnhancementDTO enhancement;
  private LocationDTO location;
  private String modifiers;
  private ParameterDTO parameter;
  private String rawType;

  private String hidden;
  private ICCMethodAttributeDTO iccMethodAttribute;

  public MethodEntityDTO() {
  }

  public MethodEntityDTO(
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      EnhancementDTO enhancement,
      LocationDTO location,
      String modifiers,
      ParameterDTO parameter,
      String rawType) {
    super(parentId, "Method");
    this.File = file;
    this.additionalBin = additionalBin;
    this.enhancement = enhancement;
    this.location = location;
    this.modifiers = modifiers;
    this.parameter = parameter;
    this.rawType = rawType;
  }

  public MethodEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      EnhancementDTO enhancement,
      LocationDTO location,
      String modifiers,
      ParameterDTO parameter,
      String rawType,
      ICCMethodAttributeDTO iccMethodAttribute) {
    super(id, name, qualifiedName, parentId, "Method");
    this.File = file;
    this.additionalBin = additionalBin;
    this.enhancement = enhancement;
    this.location = location;
    this.modifiers = modifiers;
    this.parameter = parameter;
    this.rawType = rawType;
    this.iccMethodAttribute = iccMethodAttribute;
  }

  public MethodEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String file,
          AdditionalBinDTO additionalBin,
          EnhancementDTO enhancement,
          LocationDTO location,
          String modifiers,
          ParameterDTO parameter,
          String rawType,
          String hidden,
          ICCMethodAttributeDTO iccMethodAttribute) {
    super(id, name, qualifiedName, parentId, "Method");
    this.File = file;
    this.additionalBin = additionalBin;
    this.enhancement = enhancement;
    this.location = location;
    this.modifiers = modifiers;
    this.parameter = parameter;
    this.rawType = rawType;
    this.hidden = hidden;
    this.iccMethodAttribute = iccMethodAttribute;
  }

  public String getHidden() {
    return hidden;
  }

  public void setHidden(String hidden) {
    this.hidden = hidden;
  }

  @JSONPropertyName("File")
  public String getFile() {
    return File;
  }

  public void setFile(String file) {
    this.File = file;
  }

  public AdditionalBinDTO getAdditionalBin() {
    return additionalBin;
  }

  public void setAdditionalBin(AdditionalBinDTO additionalBin) {
    this.additionalBin = additionalBin;
  }

  public EnhancementDTO getEnhancement() {
    return enhancement;
  }

  public void setEnhancement(EnhancementDTO enhancement) {
    this.enhancement = enhancement;
  }

  public LocationDTO getLocation() {
    return location;
  }

  public void setLocation(LocationDTO location) {
    this.location = location;
  }

  public String getModifiers() {
    return modifiers;
  }

  public void setModifiers(String modifiers) {
    this.modifiers = modifiers;
  }

  public ParameterDTO getParameter() {
    return parameter;
  }

  public void setParameter(ParameterDTO parameter) {
    this.parameter = parameter;
  }

  public String getRawType() {
    return rawType;
  }

  public void setRawType(String rawType) {
    this.rawType = rawType;
  }

  public static class EnhancementDTO {

    private Boolean isGetter;
    private Boolean isRecursive;
    private Boolean isStatic;
    private Boolean isConstructor;
    private Boolean isOverride;
    private Boolean isSetter;
    private Boolean isPublic;
    private Boolean isDelegator;
    private Boolean isSynchronized;
    private Boolean isAbstract;

    public EnhancementDTO() {}

    public EnhancementDTO(
        Boolean isGetter,
        Boolean isRecursive,
        Boolean isStatic,
        Boolean isConstructor,
        Boolean isOverride,
        Boolean isSetter,
        Boolean isPublic,
        Boolean isDelegator,
        Boolean isSynchronized,
        Boolean isAbstract) {
      this.isGetter = isGetter;
      this.isRecursive = isRecursive;
      this.isStatic = isStatic;
      this.isConstructor = isConstructor;
      this.isOverride = isOverride;
      this.isSetter = isSetter;
      this.isPublic = isPublic;
      this.isDelegator = isDelegator;
      this.isSynchronized = isSynchronized;
      this.isAbstract = isAbstract;
    }

    public Boolean getGetter() {
      return isGetter;
    }

    public void setGetter(Boolean getter) {
      isGetter = getter;
    }

    public Boolean getRecursive() {
      return isRecursive;
    }

    public void setRecursive(Boolean recursive) {
      isRecursive = recursive;
    }

    public Boolean getStatic() {
      return isStatic;
    }

    public void setStatic(Boolean aStatic) {
      isStatic = aStatic;
    }

    public Boolean getConstructor() {
      return isConstructor;
    }

    public void setConstructor(Boolean constructor) {
      isConstructor = constructor;
    }

    public Boolean getOverride() {
      return isOverride;
    }

    public void setOverride(Boolean override) {
      isOverride = override;
    }

    public Boolean getSetter() {
      return isSetter;
    }

    public void setSetter(Boolean setter) {
      isSetter = setter;
    }

    public Boolean getPublic() {
      return isPublic;
    }

    public void setPublic(Boolean aPublic) {
      isPublic = aPublic;
    }

    public Boolean getDelegator() {
      return isDelegator;
    }

    public void setDelegator(Boolean delegator) {
      isDelegator = delegator;
    }

    public Boolean getSynchronized() {
      return isSynchronized;
    }

    public void setSynchronized(Boolean aSynchronized) {
      isSynchronized = aSynchronized;
    }

    public Boolean getAbstract() {
      return isAbstract;
    }

    public void setAbstract(Boolean anAbstract) {
      isAbstract = anAbstract;
    }
  }

  public static class ParameterDTO {

    private String types;
    private String names;

    public ParameterDTO() {}

    public ParameterDTO(String types, String names) {
      this.types = types;
      this.names = names;
    }

    public String getTypes() {
      return types;
    }

    public void setTypes(String types) {
      this.types = types;
    }

    public String getNames() {
      return names;
    }

    public void setNames(String names) {
      this.names = names;
    }
  }

  public ICCMethodAttributeDTO getIccMethodAttribute() {
    return iccMethodAttribute;
  }

  public void setIccMethodAttribute(ICCMethodAttributeDTO iccMethodAttribute) {
    this.iccMethodAttribute = iccMethodAttribute;
  }
}
