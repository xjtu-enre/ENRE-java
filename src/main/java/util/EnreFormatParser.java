package util;

import entity.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class EnreFormatParser {

  private static MethodEntityDTO.ParameterDTO parseParameterDTO(JSONObject parent) {
    if (!parent.has("parameter")) {
      return null;
    }
    JSONObject paramObj = (JSONObject) parent.get("parameter");
    if (!verify(paramObj, "types", "names")) {
      return null;
    }
    return new MethodEntityDTO.ParameterDTO(
        (String) paramObj.get("types"), (String) paramObj.get("names"));
  }

  private static Boolean verify(JSONObject obj, String... keys) {
    for (String key : keys) {
      if (!obj.has(key)) {
        return false;
      }
    }
    return true;
  }

  private static MethodEntityDTO.EnhancementDTO parseEnhancementDTO(JSONObject parent) {
    if (!parent.has("enhancement")) {
      return null;
    }
    JSONObject obj = (JSONObject) parent.get("enhancement");
    if (!verify(
        obj,
        "isGetter",
        "isRecursive",
        "isStatic",
        "isConstructor",
        "isOverride",
        "isSetter",
        "isPublic",
        "isDelegator",
        "isSynchronized",
        "isAbstract")) {
      return null;
    }
    return new MethodEntityDTO.EnhancementDTO(
        (Boolean) obj.get("isGetter"),
        (Boolean) obj.get("isRecursive"),
        (Boolean) obj.get("isStatic"),
        (Boolean) obj.get("isConstructor"),
        (Boolean) obj.get("isOverride"),
        (Boolean) obj.get("isSetter"),
        (Boolean) obj.get("isPublic"),
        (Boolean) obj.get("isDelegator"),
        (Boolean) obj.get("isSynchronized"),
        (Boolean) obj.get("isAbstract"));
  }

  private static List<Integer> parseInnerType(JSONObject parent) {
    if (!parent.has("innerType")) {
      return null;
    }
    List<Integer> res = new ArrayList<>();
    for (Object innerType : (JSONArray) parent.get("innerType")) {
      res.add((Integer) innerType);
    }
    return res;
  }

  private static AdditionalBinDTO parseAdditionalBinDTO(JSONObject parent) {
    if (!parent.has("additionalBin")) {
      return null;
    }
    JSONObject obj = (JSONObject) parent.get("additionalBin");
    if (!verify(obj, "binNum", "binPath")) {
      return null;
    }
    return new AdditionalBinDTO((Integer) obj.get("binNum"), (String) obj.get("binPath"));
  }

  private static ICCMethodAttributeDTO parseICCMethodAttributeDTO(JSONObject parent) {
    if (!parent.has("iccMethodAttribute")) {
      return null;
    }
    JSONObject obj = (JSONObject) parent.get("iccMethodAttribute");
    if (!verify(obj, "receive", "send", "machanismType")) {
      return null;
    }
    return new ICCMethodAttributeDTO(
        (Boolean) obj.get("send"), (Boolean) obj.get("receive"), (String) obj.get("machanismType"));
  }

  private static ICCVariableAttributeDTO parseICCVariableAttributeDTO(JSONObject parent) {
    if (!parent.has("iccVariableAttribute")) {
      return null;
    }
    JSONObject obj = (JSONObject) parent.get("iccVariableAttribute");
    if (!verify(obj, "data", "flags", "extras", "action", "type", "category")) {
      return null;
    }
    return new ICCVariableAttributeDTO(
        (String) obj.get("data"),
        (String) obj.get("flags"),
        (String) obj.get("extras"),
        (String) obj.get("action"),
        (String) obj.get("type"),
        (String) obj.get("category"));
  }

  private static ComponentDTO parseComponentDTO(JSONObject parent) {
    if (!parent.has("component")) {
      return null;
    }
    JSONObject obj = (JSONObject) parent.get("component");
    if (!verify(obj, "componentCategory")) {
      return null;
    }
    return new ComponentDTO((String) obj.get("componentCategory"));
  }

  private static LocationDTO parseLocationDTO(JSONObject parent, String key) {
    if (!parent.has(key)) {
      return null;
    }
    JSONObject locObj = (JSONObject) parent.get(key);
    if (!verify(locObj, "startLine", "endLine", "startColumn", "endColumn")) {
      return null;
    }
    return new LocationDTO(
        (Integer) locObj.get("startLine"),
        (Integer) locObj.get("endLine"),
        (Integer) locObj.get("startColumn"),
        (Integer) locObj.get("endColumn"));
  }

  private static ValuesDTO parseValuesDTO(JSONObject parent) {
    if (!parent.has("values")) {
      return null;
    }
    JSONObject valuesObj = (JSONObject) parent.get("values");
    LocationDTO locDTO = parseLocationDTO(valuesObj, "loc");
    ValuesDTO valuesDTO = new ValuesDTO(locDTO);
    String key;
    Iterator<String> it = valuesObj.keys();
    while (it.hasNext()) {
      key = it.next();
      switch (key) {
        case "loc":
          break;
        case "iccMechanism":
          valuesDTO.setIccMechanism((String) valuesObj.get("iccMechanism"));
          break;
        case "iccCategory":
          valuesDTO.setIccCategory((String) valuesObj.get("iccCategory"));
          break;
        case "arguments":
          valuesDTO.setArguments((String) valuesObj.get("arguments"));
          break;
        case "modifyAccessible":
          valuesDTO.setModifyAccessible((Boolean) valuesObj.get("modifyAccessible"));
          break;
        case "bindVar":
          valuesDTO.setBindVar((Integer) valuesObj.get("bindVar"));
          break;
        case "invoke":
          valuesDTO.setInvoke((Boolean) valuesObj.get("invoke"));
          break;
        default:
          valuesDTO.getRelations().put(key, (Integer) valuesObj.get(key));
      }
    }
    return valuesDTO;
  }

  private static CellDTO parseCellDTO(JSONObject cellObj) {
    if (!verify(cellObj, "src", "dest")) {
      return null;
    }
    Integer src = (Integer) cellObj.get("src");
    Integer dest = (Integer) cellObj.get("dest");
    ValuesDTO valuesDTO = parseValuesDTO(cellObj);
    return new CellDTO(src, dest, valuesDTO);
  }

  private static Object get(JSONObject obj, String key) {
    if (obj.has(key)) {
      return obj.get(key);
    }
    return null;
  }

  private static EntityDTO parseEntityDTO(JSONObject variableObj) {
    EntityDTO res;
    Integer id = (Integer) variableObj.get("id");
    String name = (String) variableObj.get("name");
    String qualifiedName = (String) variableObj.get("qualifiedName");
    Boolean external = (Boolean) variableObj.get("external");
    if (external) {
      res = new ExternalEntityDTO(id, name, qualifiedName);
    } else {
      String category = (String) variableObj.get("category");
      Integer parentId = (Integer) variableObj.get("parentId");
      String file = (String) get(variableObj, "File");
      String rawType = (String) get(variableObj, "rawType");
      Integer anonymousBindVar = (Integer) get(variableObj, "anonymousBindVar");
      Integer anonymousRank = (Integer) get(variableObj, "anonymousRank");
      String modifiers = (String) get(variableObj, "modifiers");
      Boolean global = (Boolean) get(variableObj, "global");
      String hidden = (String) get(variableObj, "hidden");
      AdditionalBinDTO additionalBinDTO = parseAdditionalBinDTO(variableObj);
      LocationDTO locationDTO = parseLocationDTO(variableObj, "location");
      ICCMethodAttributeDTO iccMethodAttributeDTO = parseICCMethodAttributeDTO(variableObj);
      ICCVariableAttributeDTO iccVariableAttributeDTO = parseICCVariableAttributeDTO(variableObj);
      ComponentDTO componentDTO = parseComponentDTO(variableObj);
      List<Integer> innerType = parseInnerType(variableObj);
      MethodEntityDTO.EnhancementDTO enhancementDTO = parseEnhancementDTO(variableObj);
      MethodEntityDTO.ParameterDTO parameterDTO = parseParameterDTO(variableObj);
      switch (category) {
        case "Package":
          res = new PackageEntityDTO(id, name, qualifiedName, parentId);
          break;
        case "File":
          res = new FileEntityDTO(id, name, qualifiedName, parentId, file, additionalBinDTO);
          break;
        case "Class":
          if (anonymousRank == null || anonymousBindVar == null) {
            res =
                new ClassEntityDTO(
                    id,
                    name,
                    qualifiedName,
                    parentId,
                    rawType,
                    locationDTO,
                    modifiers,
                    file,
                    hidden,
                    componentDTO,
                    additionalBinDTO,
                    innerType);
          } else {
            res =
                new AnonymousClassEntityDTO(
                    id,
                    name,
                    qualifiedName,
                    parentId,
                    rawType,
                    locationDTO,
                    modifiers,
                    file,
                    hidden,
                    componentDTO,
                    additionalBinDTO,
                    anonymousBindVar,
                    anonymousRank);
          }
          break;
        case "Enum":
          res =
              new EnumEntityDTO(
                  id,
                  name,
                  qualifiedName,
                  parentId,
                  file,
                  additionalBinDTO,
                  locationDTO,
                  modifiers,
                  hidden,
                  rawType);
          break;
        case "Enum Constant":
          res =
              new EnumConstantEntityDTO(id, name, qualifiedName, parentId, file, hidden, additionalBinDTO);
          break;
        case "Annotation":
          res =
              new AnnotationEntityDTO(
                  id,
                  name,
                  qualifiedName,
                  parentId,
                  file,
                  additionalBinDTO,
                  locationDTO,
                  modifiers,
                  hidden,
                  rawType);
          break;
        case "Annotation Member":
          res =
              new AnnotationMemberEntityDTO(
                  id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO, rawType);
          break;
        case "Interface":
          res =
              new InterfaceEntityDTO(
                  id,
                  name,
                  qualifiedName,
                  parentId,
                  file,
                  additionalBinDTO,
                  locationDTO,
                  modifiers,
                  hidden,
                  rawType);
          break;
        case "Method":
          res =
              new MethodEntityDTO(
                  id,
                  name,
                  qualifiedName,
                  parentId,
                  file,
                  additionalBinDTO,
                  enhancementDTO,
                  locationDTO,
                  modifiers,
                  parameterDTO,
                  rawType,
                  hidden,
                  iccMethodAttributeDTO);
          break;
        case "Module":
          res = null;
          break;
        case "Record":
          res = null;
          break;
        case "Type Parameter":
          res =
              new TypeParameterEntityDTO(
                  id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO, rawType);
          break;
        case "Variable":
          res =
              new VariableEntityDTO(
                  id,
                  name,
                  qualifiedName,
                  parentId,
                  file,
                  additionalBinDTO,
                  global,
                  locationDTO,
                  modifiers,
                  rawType,
                  hidden,
                  iccVariableAttributeDTO);
          break;
        default:
          throw new RuntimeException("No such entity type: " + category);
      }
      return res;
    }
    return res;
  }

  private static Map<String, Integer> parseMap(JSONObject parent, String key) {
    if (!verify(parent, key)) {
      return null;
    }
    Map<String, Integer> res = new HashMap<>();
    JSONObject obj = (JSONObject) parent.get(key);
    Iterator<String> it = obj.keys();
    while (it.hasNext()) {
      String curKey = it.next();
      Integer curVal = (Integer) obj.get(curKey);
      res.put(curKey, curVal);
    }
    return res;
  }

  private static List<CategoryDTO> parseCategories(JSONObject parent) {
    if (!verify(parent, "categories")) {
      return null;
    }
    List<CategoryDTO> res = new ArrayList<>();
    for (Object arr : (JSONArray) parent.get("categories")) {
      JSONObject obj = (JSONObject) arr;
      String val = (String) obj.get("name");
      res.add(new CategoryDTO(val));
    }
    return res;
  }

  public static EnreDTO parse(JSONObject obj) {
    EnreDTO enre = new EnreDTO();
    enre.setSchemaVersion((String) obj.get("schemaVersion"));
    if (obj.has("cells")) {
      Object cellsObj = obj.get("cells");
      if (cellsObj instanceof JSONObject) {
        enre.getCells().add(parseCellDTO((JSONObject) cellsObj));
      } else if (cellsObj instanceof JSONArray) {
        for (Object cell : (JSONArray) obj.get("cells")) {
          enre.getCells().add(parseCellDTO((JSONObject) cell));
        }
      } else {
        throw new RuntimeException("unknown object type for cells: " + cellsObj.getClass().getName());
      }
    }
    int maxIndex = 0;
    if (obj.has("variables")) {
      Object variableObj = obj.get("variables");
      if (variableObj instanceof JSONObject) {
        EntityDTO entity = parseEntityDTO((JSONObject) variableObj);
        if (entity.getId() > maxIndex) {
          maxIndex = entity.getId();
        }
        enre.getVariables().add(entity);
      } else if (variableObj instanceof JSONArray) {
        for (Object variable : (JSONArray) obj.get("variables")) {
          EntityDTO entity = parseEntityDTO((JSONObject) variable);
          if (entity.getId() > maxIndex) {
            maxIndex = entity.getId();
          }
          enre.getVariables().add(entity);
        }
      } else {
        throw new RuntimeException("unknown object type for variables: " + variableObj.getClass().getName());
      }
    }
    if (obj.has("entityNum")) {
      enre.setEntityNum(parseMap(obj, "entityNum"));
    }
    if (obj.has("relationNum")) {
      enre.setRelationNum(parseMap(obj, "relationNum"));
    }
    enre.setCategories(parseCategories(obj));
    return enre;
  }
}
