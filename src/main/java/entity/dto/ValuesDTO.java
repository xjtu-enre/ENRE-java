package entity.dto;

import java.util.HashMap;
import java.util.Map;

public class ValuesDTO {

  private LocationDTO loc;
  private final Map<String, Integer> relations = new HashMap<>();
  private String iccMechanism;
  private String iccCategory;
  private String arguments = null;
  private Integer bindVar = null;
  private Boolean modifyAccessible = null;
  private Boolean invoke = null;

  public ValuesDTO() {}

  public ValuesDTO(LocationDTO loc) {
    this.loc = loc;
  }

  public ValuesDTO(LocationDTO loc, String iccMechanism, String iccCategory) {
    this.loc = loc;
    this.iccMechanism = iccMechanism;
    this.iccCategory = iccCategory;
  }

  public ValuesDTO(LocationDTO loc, String iccMechanism, String iccCategory, String arguments) {
    this.loc = loc;
    this.iccMechanism = iccMechanism;
    this.iccCategory = iccCategory;
    this.arguments = arguments;
  }

  public Boolean getInvoke() {
    return invoke;
  }

  public void setInvoke(Boolean invoke) {
    this.invoke = invoke;
  }

  public Integer getBindVar() {
    return bindVar;
  }

  public void setBindVar(Integer bindVar) {
    this.bindVar = bindVar;
  }

  public Boolean getModifyAccessible() {
    return modifyAccessible;
  }

  public void setModifyAccessible(Boolean modifyAccessible) {
    this.modifyAccessible = modifyAccessible;
  }

  public String getArguments() {
    return arguments;
  }

  public void setArguments(String arguments) {
    this.arguments = arguments;
  }

  public void addRelation(String relation, Integer count) {
    int old = relations.getOrDefault(relation, 0);
    relations.put(relation, old + count);
  }

  public Map<String, Integer> getRelations() {
    return relations;
  }

  public LocationDTO getLoc() {
    return loc;
  }

  public void setLoc(LocationDTO loc) {
    this.loc = loc;
  }

  public String getIccMechanism() {
    return iccMechanism;
  }

  public void setIccMechanism(String iccMechanism) {
    this.iccMechanism = iccMechanism;
  }

  public String getIccCategory() {
    return iccCategory;
  }

  public void setIccCategory(String iccCategory) {
    this.iccCategory = iccCategory;
  }
}
