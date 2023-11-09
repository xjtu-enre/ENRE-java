package entity.dto;

import org.json.JSONPropertyName;

public class EnumConstantEntityDTO extends InternalEntityDTO {

  private String File;
  private String hidden;
  private AdditionalBinDTO additionalBin;
  private LocationDTO location;

  public EnumConstantEntityDTO() {
  }

  public EnumConstantEntityDTO(int parentId, String file, AdditionalBinDTO additionalBin) {
    super(parentId, "Enum Constant");
    this.File = file;
    this.additionalBin = additionalBin;
  }

  public EnumConstantEntityDTO(
          int id,
          String name,
          String qualifiedName,
          int parentId,
          String file,
          String hidden,
          AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Enum Constant");
    this.File = file;
    this.hidden = hidden;
    this.additionalBin = additionalBin;
  }

  public EnumConstantEntityDTO(int id,
                               String name,
                               String qualifiedName,
                               int parentId,
                               String file,
                               String hidden,
                               AdditionalBinDTO additionalBin,
                               LocationDTO location) {
    super(id, name, qualifiedName, parentId, "Enum Constant");
    this.File = file;
    this.hidden = hidden;
    this.additionalBin = additionalBin;
    this.location = location;
  }

  public EnumConstantEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "Enum Constant");
    this.File = file;
    this.additionalBin = additionalBin;
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

  public LocationDTO getLocation() {
    return location;
  }

  public void setLocation(LocationDTO location) {
    this.location = location;
  }
}
