package entity.dto;

import org.json.JSONPropertyName;

public class BaseClassEntityDTO extends InternalEntityDTO {

  private String rawType;
  private LocationDTO location;
  private String modifiers;
  private String File;
  private ComponentDTO component;
  private String hidden;
  private AdditionalBinDTO additionalBin;

  public BaseClassEntityDTO() {
  }

  public BaseClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      ComponentDTO component,
      AdditionalBinDTO additionalBin) {
    super(parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.component = component;
    this.additionalBin = additionalBin;
  }

  public BaseClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      ComponentDTO component,
      AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.component = component;
    this.additionalBin = additionalBin;
  }

  public BaseClassEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String rawType,
          LocationDTO location,
          String modifiers,
          String file,
          String hidden,
          ComponentDTO component,
          AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.hidden = hidden;
    this.component = component;
    this.additionalBin = additionalBin;
  }



  public BaseClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin) {
    super(parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.additionalBin = additionalBin;
  }

  public BaseClassEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String rawType,
          LocationDTO location,
          String modifiers,
          String file,
          String hidden,
          AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.hidden = hidden;
    this.additionalBin = additionalBin;
  }

  public BaseClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.additionalBin = additionalBin;
  }

  public String getRawType() {
    return rawType;
  }

  public void setRawType(String rawType) {
    this.rawType = rawType;
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

  public ComponentDTO getComponent() {
    return component;
  }

  public void setComponent(ComponentDTO component) {
    this.component = component;
  }

  public String getHidden() {
    return hidden;
  }

  public void setHidden(String hidden) {
    this.hidden = hidden;
  }
}
