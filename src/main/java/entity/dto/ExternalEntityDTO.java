package entity.dto;

public class ExternalEntityDTO extends EntityDTO {
  public ExternalEntityDTO() {}

  public ExternalEntityDTO(int id, String name, String qualifiedName) {
    super(id, name, qualifiedName, true);
  }
}
