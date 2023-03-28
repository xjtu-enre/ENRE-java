package entity.dto;

import org.json.JSONPropertyName;

public class TypeParameterEntityDTO extends InternalEntityDTO {

  private String File;
  private AdditionalBinDTO additionalBin;
  private LocationDTO location;
  private String rawType;

  public TypeParameterEntityDTO() {
  }

  public TypeParameterEntityDTO(
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      LocationDTO location,
      String rawType) {
    super(parentId, "Type Parameter");
    this.File = file;
    this.additionalBin = additionalBin;
    this.location = location;
    this.rawType = rawType;
  }

  public TypeParameterEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin,
      LocationDTO location,
      String rawType) {
    super(id, name, qualifiedName, parentId, "Type Parameter");
    this.File = file;
    this.additionalBin = additionalBin;
    this.location = location;
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

  public String getRawType() {
    return rawType;
  }

  public void setRawType(String rawType) {
    this.rawType = rawType;
  }
}
