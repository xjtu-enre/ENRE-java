package entity.dto;

import org.json.JSONPropertyName;

public class VariableEntityDTO extends InternalEntityDTO {

  private String File;
  private AdditionalBinDTO additionalBin;
  private Boolean global;
  private LocationDTO location;
  private String modifiers;
  private String rawType;
  private String hidden;
  private ICCVariableAttributeDTO iccVariableAttribute;

  public VariableEntityDTO() {
  }

  public VariableEntityDTO(
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      Boolean global,
      LocationDTO location,
      String modifiers,
      String rawType) {
    super(parentId, "Variable");
    this.File = file;
    this.additionalBin = additionalBin;
    this.global = global;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
  }

  public VariableEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      Boolean global,
      LocationDTO location,
      String modifiers,
      String rawType,
      ICCVariableAttributeDTO iccVariableAttribute) {
    super(id, name, qualifiedName, parentId, "Variable");
    this.File = file;
    this.additionalBin = additionalBin;
    this.global = global;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
    this.iccVariableAttribute = iccVariableAttribute;
  }

  public VariableEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String file,
          AdditionalBinDTO additionalBin,
          Boolean global,
          LocationDTO location,
          String modifiers,
          String rawType,
          String hidden,
          ICCVariableAttributeDTO iccVariableAttribute) {
    super(id, name, qualifiedName, parentId, "Variable");
    this.File = file;
    this.additionalBin = additionalBin;
    this.global = global;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
    this.hidden = hidden;
    this.iccVariableAttribute = iccVariableAttribute;
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

  public Boolean getGlobal() {
    return global;
  }

  public void setGlobal(Boolean global) {
    this.global = global;
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

  public String getRawType() {
    return rawType;
  }

  public void setRawType(String rawType) {
    this.rawType = rawType;
  }

  public ICCVariableAttributeDTO getIccVariableAttribute() {
    return iccVariableAttribute;
  }

  public void setIccVariableAttribute(ICCVariableAttributeDTO iccVariableAttribute) {
    this.iccVariableAttribute = iccVariableAttribute;
  }
}
