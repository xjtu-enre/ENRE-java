package entity.dto;

import org.json.JSONPropertyName;

public class FileEntityDTO extends InternalEntityDTO {

  private String File;
  private AdditionalBinDTO additionalBin;

  public FileEntityDTO(int parentId, String file, AdditionalBinDTO additionalBin) {
    super(parentId, "File");
    this.File = file;
    this.additionalBin = additionalBin;
  }

  public FileEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String file,
      AdditionalBinDTO additionalBin) {
    super(id, name, qualifiedName, parentId, "File");
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
}
