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
      ComponentDTO component,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType) {
    super(parentId, rawType, location, modifiers, file, component, additionalBin);
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
          ComponentDTO component,
          AdditionalBinDTO additionalBin,
          String hidden,
          List<Integer> innerType) {
    super(
            id,
            name,
            qualifiedName,
            parentId,
            rawType,
            location,
            modifiers,
            file,
            hidden,
            component,
            additionalBin);
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
          String hidden,
          ComponentDTO component,
          AdditionalBinDTO additionalBin,
          List<Integer> innerType) {
    super(
            id,
            name,
            qualifiedName,
            parentId,
            rawType,
            location,
            modifiers,
            file,
            hidden,
            component,
            additionalBin);
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
      ComponentDTO component,
      AdditionalBinDTO additionalBin,
      List<Integer> innerType) {
    super(
        id,
        name,
        qualifiedName,
        parentId,
        rawType,
        location,
        modifiers,
        file,
        component,
        additionalBin);
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
