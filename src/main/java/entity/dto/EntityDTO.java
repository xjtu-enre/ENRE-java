package entity.dto;

public class EntityDTO {

  private int id;
  private String name;
  private String qualifiedName;
  private boolean external;

  public EntityDTO() {}

  public EntityDTO(int id, String name, String qualifiedName, boolean external) {
    this.id = id;
    this.name = name;
    this.qualifiedName = qualifiedName;
    this.external = external;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public void setQualifiedName(String qualifiedName) {
    this.qualifiedName = qualifiedName;
  }

  public boolean isExternal() {
    return external;
  }

  public void setExternal(boolean external) {
    this.external = external;
  }
}
