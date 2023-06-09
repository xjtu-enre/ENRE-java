package entity.dto;

import org.json.JSONPropertyName;

public class AnnotationEntityDTO extends InternalEntityDTO {

  private String File;
  private AdditionalBinDTO additionalBin;
  private LocationDTO location;
  private String modifiers;
  private String rawType;
  private String hidden;

  public AnnotationEntityDTO() {
  }

  public AnnotationEntityDTO(
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      LocationDTO location,
      String modifiers,
      String rawType) {
    super(parentId, "Annotation");
    this.File = file;
    this.additionalBin = additionalBin;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
  }

  public AnnotationEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String file,
          AdditionalBinDTO additionalBin,
          LocationDTO location,
          String modifiers,
          String hidden,
          String rawType) {
    super(id, name, qualifiedName, parentId, "Annotation");
    this.File = file;
    this.additionalBin = additionalBin;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
    this.hidden = hidden;
  }

  public AnnotationEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      LocationDTO location,
      String modifiers,
      String rawType) {
    super(id, name, qualifiedName, parentId, "Annotation");
    this.File = file;
    this.additionalBin = additionalBin;
    this.location = location;
    this.modifiers = modifiers;
    this.rawType = rawType;
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

  public String getHidden() {
    return hidden;
  }

  public void setHidden(String hidden) {
    this.hidden = hidden;
  }
}
