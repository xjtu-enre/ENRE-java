package entity.dto;

public class AnonymousClassEntityDTO extends BaseClassEntityDTO {

  private int anonymousBindVar;
  private int anonymousRank;

  public AnonymousClassEntityDTO() {
  }

  public AnonymousClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      int anonymousBindVar,
      int anonymousRank,
      String hidden) {
    super(parentId, rawType, location, modifiers, file, additionalBin, hidden);
    this.anonymousBindVar = anonymousBindVar;
    this.anonymousRank = anonymousRank;
  }

  public AnonymousClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      int anonymousBindVar,
      int anonymousRank,
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
    this.anonymousBindVar = anonymousBindVar;
    this.anonymousRank = anonymousRank;
  }

  public AnonymousClassEntityDTO(
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      int anonymousBindVar,
      int anonymousRank) {
    super(parentId, rawType, location, modifiers, file, additionalBin);
    this.anonymousBindVar = anonymousBindVar;
    this.anonymousRank = anonymousRank;
  }

  public AnonymousClassEntityDTO(
      int id,
      String name,
      String qualifiedName,
      int parentId,
      String rawType,
      LocationDTO location,
      String modifiers,
      String file,
      AdditionalBinDTO additionalBin,
      int anonymousBindVar,
      int anonymousRank) {
    super(id, name, qualifiedName, parentId, rawType, location, modifiers, file, additionalBin);
    this.anonymousBindVar = anonymousBindVar;
    this.anonymousRank = anonymousRank;
  }

  public int getAnonymousBindVar() {
    return anonymousBindVar;
  }

  public void setAnonymousBindVar(int anonymousBindVar) {
    this.anonymousBindVar = anonymousBindVar;
  }

  public int getAnonymousRank() {
    return anonymousRank;
  }

  public void setAnonymousRank(int anonymousRank) {
    this.anonymousRank = anonymousRank;
  }
}
