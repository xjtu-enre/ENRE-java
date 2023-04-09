package entity.dto;

import org.json.JSONPropertyName;

public class BaseClassEntityDTO extends InternalEntityDTO {

  private String rawType;
  private LocationDTO location;
  private String modifiers;
  private String File;
  private AdditionalBinDTO additionalBin;
  private String hidden;

  public BaseClassEntityDTO() {
  }

  public BaseClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      String hidden) {
    super(parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.additionalBin = additionalBin;
    this.hidden = hidden;
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
      AdditionalBinDTO additionalBin,
      String hidden) {
    super(id, name, qualifiedName, parentId, "Class");
    this.rawType = rawType;
    this.location = location;
    this.modifiers = modifiers;
    this.File = file;
    this.additionalBin = additionalBin;
    this.hidden = hidden;
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

  public String getHidden() {
    return hidden;
  }

  public void setHidden(String hidden) {
    this.hidden = hidden;
  }
}
