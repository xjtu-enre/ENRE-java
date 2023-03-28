package entity.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// EntityCallDTO
public class McgDTO {

  private List<EntityDTO> variables;
  private List<MethodCallDTO> cells;
  private Integer methodCount;
  private Map<String, Integer> typeCount;

  public McgDTO() {
    this.variables = new ArrayList<>();
    this.cells = new ArrayList<>();
    this.typeCount = new HashMap<>();
    this.methodCount = 0;
  }

  public McgDTO(Integer methodCount) {
    this.methodCount = methodCount;
  }

  public List<EntityDTO> getVariables() {
    return variables;
  }

  public List<MethodCallDTO> getCells() {
    return cells;
  }

  public Integer getMethodCount() {
    return methodCount;
  }

  public void setMethodCount(Integer methodCount) {
    this.methodCount = methodCount;
  }

  public Map<String, Integer> getTypeCount() {
    return typeCount;
  }
}
