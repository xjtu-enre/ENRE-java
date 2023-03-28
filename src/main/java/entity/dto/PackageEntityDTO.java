package entity.dto;

public class PackageEntityDTO extends InternalEntityDTO {

  public PackageEntityDTO(int parentId, String category) {
    super(parentId, category);
  }

  public PackageEntityDTO(int id, String name, String qualifiedName, int parentId) {
    super(id, name, qualifiedName, parentId, "Package");
  }
}
