package entity.dto;

import java.util.List;

public class ClassEntityDTO extends BaseClassEntityDTO {

  private List<Integer> innerType;

  public ClassEntityDTO() {
  }

  public ClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType,
      String hidden) {
    super(parentId, rawType, location, modifiers, file, additionalBin, hidden);
    this.innerType = innerType;
  }

  public ClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType,
      String hidden) {
    super(
        id,
        name,
        qualifiedName,
        parentId,
        rawType,
        location,
        modifiers,
        file,
        additionalBin,
        hidden);
    this.innerType = innerType;
  }

  public ClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType) {
    super(parentId, rawType, location, modifiers, file, additionalBin);
    this.innerType = innerType;
  }

  public ClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType) {
    super(id, name, qualifiedName, parentId, rawType, location, modifiers, file, additionalBin);
    this.innerType = innerType;
  }

  public List<Integer> getInnerType() {
    return innerType;
  }

  public void setInnerType(List<Integer> innerType) {
    this.innerType = innerType;
  }
}
