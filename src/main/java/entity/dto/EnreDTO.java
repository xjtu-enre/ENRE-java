package entity.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnreDTO {

  private String schemaVersion;
  private List<EntityDTO> variables;
  private List<CellDTO> cells;
  private Map<String, Integer> entityNum;
  private Map<String, Integer> relationNum;
  private List<CategoryDTO> categories;

  public EnreDTO() {
    this.variables = new ArrayList<>();
    this.cells = new ArrayList<>();
    this.entityNum = new HashMap<>();
    this.relationNum = new HashMap<>();
    this.categories = new ArrayList<>();
  }

  public EnreDTO(String schemaVersion) {
    this.schemaVersion = schemaVersion;
    this.variables = new ArrayList<>();
    this.cells = new ArrayList<>();
    this.entityNum = new HashMap<>();
    this.relationNum = new HashMap<>();
    this.categories = new ArrayList<>();
  }

  public String getSchemaVersion() {
    return schemaVersion;
  }

  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public List<EntityDTO> getVariables() {
    return variables;
  }

  public void setVariables(List<EntityDTO> variables) {
    this.variables = variables;
  }

  public List<CellDTO> getCells() {
    return cells;
  }

  public void setCells(List<CellDTO> cells) {
    this.cells = cells;
  }

  public Map<String, Integer> getEntityNum() {
    return entityNum;
  }

  public void setEntityNum(Map<String, Integer> entityNum) {
    this.entityNum = entityNum;
  }

  public Map<String, Integer> getRelationNum() {
    return relationNum;
  }

  public void setRelationNum(Map<String, Integer> relationNum) {
    this.relationNum = relationNum;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoryDTO> categories) {
    this.categories = categories;
  }
}
