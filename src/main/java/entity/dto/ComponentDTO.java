package entity.dto;

public class ComponentDTO {

  private String componentCategory;

  public ComponentDTO() {}

  public ComponentDTO(String componentCategory) {
    this.componentCategory = componentCategory;
  }

  public String getComponentCategory() {
    return componentCategory;
  }

  public void setComponentCategory(String componentCategory) {
    this.componentCategory = componentCategory;
  }
}
