package entity.dto;

public class ICCVariableAttributeDTO {

  private String data;
  private String flags;
  private String extras;
  private String action;
  private String type;
  private String category;

  public ICCVariableAttributeDTO() {}

  public ICCVariableAttributeDTO(
      String data, String flags, String extras, String action, String type, String category) {
    this.data = data;
    this.flags = flags;
    this.extras = extras;
    this.action = action;
    this.type = type;
    this.category = category;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getFlags() {
    return flags;
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }

  public String getExtras() {
    return extras;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
