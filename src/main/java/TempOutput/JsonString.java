package TempOutput;

import entity.*;
import entity.dto.*;
import entity.properties.Index;
import entity.properties.Relation;
import org.json.JSONObject;
import util.SingleCollect;
import util.Tuple;
import visitor.relationInf.RelationInf;

import java.util.*;

public class JsonString {

  private static final SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

  public static HashMap<String, Integer> turnStrMap(String status) {
    HashMap<String, Integer> nameTonum = new HashMap<>();
    String[] cells = status.split("\n");
    for (String cell : cells) {
      String[] nameAnum = cell.split(": ");
      nameTonum.put(nameAnum[0], Integer.valueOf(nameAnum[1]));
    }
    return nameTonum;
  }

  /**
   * get current entity's file id
   *
   * @param id
   * @return
   */
  public static int getCurrentFileId(int id) {
    if (singleCollect.getEntityById(id) instanceof FileEntity) {
      return id;
    } else if (singleCollect.getEntityById(singleCollect.getEntityById(id).getParentId()) instanceof FileEntity) {
      return singleCollect.getEntityById(id).getParentId();
    } else {
      return getCurrentFileId(singleCollect.getEntityById(id).getParentId());
    }
  }

  private static AdditionalBinDTO getAdditionalBinDTO(BaseEntity entity) {
    Tuple<String, Integer> binPath = entity.getBinPath();
    AdditionalBinDTO res = new AdditionalBinDTO();
    if (binPath != null) {
      res.setBinNum(binPath.getR());
      res.setBinPath(binPath.getL());
    }
    return res;
  }

  private static String getFile(BaseEntity entity) {
    if (entity instanceof PackageEntity) {
      return null;
    }
    return ((FileEntity) singleCollect.getEntityById(getCurrentFileId(entity.getId()))).getFullPath();
  }

  private static MethodEntityDTO.EnhancementDTO getEnhancementDTO(BaseEntity entity) {
    if (!(entity instanceof MethodEntity)) {
      return null;
    }
    MethodEntity methodEntity = (MethodEntity) entity;
    Index indices = methodEntity.getIndices();
    if (indices == null) {
      return null;
    }
    return new MethodEntityDTO.EnhancementDTO(
        indices.getIsGetter(),
        indices.getIsRecursive(),
        indices.getIsStatic(),
        indices.getIsConstructor(),
        indices.getIsOverride(),
        indices.getIsSetter(),
        indices.getIsPublic(),
        indices.getIsDelegator(),
        indices.getIsSynchronized(),
        indices.getMethodIsAbstract()
    );
  }

  private static LocationDTO getLocationDTO(Relation relation) {
    return new LocationDTO(
        relation.getLocation().getStartLine(),
        relation.getLocation().getEndLine(),
        relation.getLocation().getStartColumn(),
        relation.getLocation().getEndColumn()
    );
  }

  private static LocationDTO getLocationDTO(BaseEntity entity) {
    if (entity instanceof PackageEntity || entity instanceof FileEntity) {
      return null;
    }
    return new LocationDTO(
        entity.getLocation().getStartLine(),
        entity.getLocation().getEndLine(),
        entity.getLocation().getStartColumn(),
        entity.getLocation().getEndColumn()
    );
  }

  private static Boolean getGlobal(BaseEntity entity) {
    if (!(entity instanceof VariableEntity)) {
      return null;
    }
    VariableEntity variableEntity = (VariableEntity) entity;
    return variableEntity.getGlobal();
  }

  private static String getHidden(ProcessHidden processHidden, BaseEntity entity, String parType) {
    if (entity instanceof VariableEntity) {
      if (!processHidden.getResult().isEmpty() && ((VariableEntity) entity).getGlobal() && processHidden.checkHidden((VariableEntity) entity) != null) {
        return processHidden.checkHidden((VariableEntity) entity);
      }
    } else if (entity instanceof TypeEntity) {
      if (!processHidden.getResult().isEmpty() && processHidden.checkHidden((TypeEntity) entity) != null) {
        return processHidden.checkHidden((TypeEntity) entity);
      }
    } else if (entity instanceof MethodEntity && parType != null) {
      if (!processHidden.getResult().isEmpty() && processHidden.checkHidden((MethodEntity) entity, parType) != null) {
        return processHidden.checkHidden((MethodEntity) entity, parType);
      }
    }
    return null;
  }

  private static MethodEntityDTO.ParameterDTO getParameterDTO(BaseEntity entity) {
    if (!(entity instanceof MethodEntity)) {
      return null;
    }
    MethodEntity methodEntity = (MethodEntity) entity;
    StringBuilder types = new StringBuilder();
    StringBuilder names = new StringBuilder();
    ArrayList<Integer> params = methodEntity.getParameters();
    for (int i = 0; i < params.size(); i++) {
      int parId = params.get(i);
      types.append(processRawType(singleCollect.getEntityById(parId).getRawType()));
      names.append(singleCollect.getEntityById(parId).getName());
      if (i != params.size() - 1) {
        types.append(" ");
        names.append(" ");
      }
    }
    return new MethodEntityDTO.ParameterDTO(types.toString(), names.toString());
  }

  private static String getModifiers(BaseEntity entity) {
    StringBuilder res = new StringBuilder();
    ArrayList<String> modifiers = entity.getModifiers();
    for (int i = 0; i < modifiers.size(); i++) {
      String mod = modifiers.get(i);
      if (!mod.contains("@")) {
        res.append(mod);
      }
      if (i != modifiers.size() - 1) {
        res.append(" ");
      }
    }
    return res.toString();
  }

  private static String getRawType(BaseEntity entity) {
    if (entity.getRawType() == null) {
      return null;
    }
    return processRawType(entity.getRawType());
  }

  private static List<Integer> getInnerType(BaseEntity entity) {
    if (!(entity instanceof TypeEntity)) {
      return null;
    }
    TypeEntity typeEntity = (TypeEntity) entity;
    return typeEntity.getInnerType();
  }

  private static ExternalEntityDTO parseExternalEntityDTO(ExternalEntity entity) {
    return new ExternalEntityDTO(
        entity.getId(),
        entity.getName(),
        entity.getQualifiedName()
    );
  }

  private static CellDTO parseCellDTO(int fromEntity, int toEntity, Relation relation, boolean slim) {
    CellDTO cellDTO = new CellDTO();
    if (relation.getKind().contains("by")) {
      return null;
    }
//                    JSONObject srcObj = new JSONObject();
//                    srcObj.put("id", fromEntity);
//                    srcObj.put("type", singleCollect.getEntityType(fromEntity));
//                    srcObj.put("name", singleCollect.getEntityById(fromEntity).getName());
//                    srcObj.put("qualified name", singleCollect.getEntityById(fromEntity).getQualifiedName());
//                    srcObj.put("parentId", singleCollect.getEntityById(fromEntity).getParentId());
//                    srcObj.put("childrenIds", singleCollect.getEntityById(fromEntity).getChildrenIds());
    cellDTO.setSrc(fromEntity);
    cellDTO.setDest(toEntity);
//                    subObj.accumulate("src", srcObj);
//   l                 subObj.accumulate("dest", destObj);
    ValuesDTO valuesDTO = new ValuesDTO();
    valuesDTO.getRelations().put(relation.getKind(), 1);
    if (relation.getBindVar() != -1) {
      valuesDTO.setBindVar(relation.getBindVar());
    }
    if (relation.getModifyAccessible()) {
      valuesDTO.setModifyAccessible(true);
    }
    if (!relation.getArguemnts().isEmpty()) {
      ArrayList<String> arguments = relation.getArguemnts();
      StringBuilder args = new StringBuilder();
      for (int i = 0; i < arguments .size(); i++) {
        args.append(arguments.get(i).replace("\"", "").replace(",", " "));
        if (i != arguments.size() - 1) {
          args.append(" ");
        }
      }
      valuesDTO.setArguments(args.toString());
    }
    if (relation.getInvoke()) {
//                        try{
//                            JSONObject locObj = new JSONObject();
//                            locObj.put("startLine", type.getLocation().getStartLine());
//                            locObj.put("endLine", type.getLocation().getEndLine());
//                            locObj.put("startColumn", type.getLocation().getStartColumn());
//                            locObj.put("endColumn", type.getLocation().getEndColumn());
//                            reObj.accumulate("invoke", locObj);
//                        } catch (NullPointerException e){
      valuesDTO.setInvoke(true);
//                        }
    }
//                    else {
    if (!slim) {
      valuesDTO.setLoc(getLocationDTO(relation));
    }
//                    }
    cellDTO.setValues(valuesDTO);

//                }
    return cellDTO;
  }

  private static EntityDTO parseEntityDTO(ProcessHidden processHidden, BaseEntity entity, boolean slim) {
    int id = entity.getId();
    String name = entity.getName();
    String qualifiedName = entity.getQualifiedName();
    int parentId = entity.getParentId();
    String file = getFile(entity);
    AdditionalBinDTO additionalBinDTO = getAdditionalBinDTO(entity);
    MethodEntityDTO.EnhancementDTO enhancementDTO = getEnhancementDTO(entity);
    MethodEntityDTO.ParameterDTO parameterDTO = getParameterDTO(entity);
    LocationDTO locationDTO = getLocationDTO(entity);
    String modifiers = getModifiers(entity);
    String rawType = getRawType(entity);
    Boolean isGlobal = getGlobal(entity);
    String hidden = null;
    if (parameterDTO != null) {
      hidden = getHidden(processHidden, entity, parameterDTO.getTypes());
    }
    List<Integer> innerType = getInnerType(entity);
    if (slim) {
      locationDTO = null;
      enhancementDTO = null;
    }
    if (entity instanceof PackageEntity) {
      return new PackageEntityDTO(
          id, name, qualifiedName, parentId
      );
    } else if (entity instanceof FileEntity) {
      return new FileEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO
      );
    } else if (entity instanceof MethodEntity) {
      return new MethodEntityDTO(
          id, name, qualifiedName, parentId, file,
          additionalBinDTO, enhancementDTO, locationDTO, modifiers, parameterDTO, rawType, hidden
      );
    } else if (entity instanceof ClassEntity) {
      ClassEntity classEntity = (ClassEntity) entity;
      if (classEntity.getAnonymousRank() == 0) {
        return new ClassEntityDTO(
            id, name, qualifiedName, parentId, rawType, locationDTO, modifiers, file,
            additionalBinDTO, innerType, hidden
        );
      } else {
        return new AnonymousClassEntityDTO(
            id, name, qualifiedName, parentId, rawType,
            locationDTO, modifiers, file, additionalBinDTO, classEntity.getAnonymousBindVar(),
            classEntity.getAnonymousRank(), hidden);
      }
    } else if (entity instanceof EnumEntity) {
      return new EnumEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO,
          modifiers, rawType, hidden
      );
    } else if (entity instanceof EnumConstantEntity) {
      return new EnumConstantEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO
      );
    } else if (entity instanceof AnnotationEntity) {
      return new AnnotationEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO,
          locationDTO, modifiers, rawType
      );
    } else if (entity instanceof AnnotationTypeMember) {
      return new AnnotationMemberEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO, rawType
      );
    } else if (entity instanceof InterfaceEntity) {
      return new InterfaceEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO,
          modifiers, rawType, hidden
      );
    } else if (entity instanceof TypeParameterEntity) {
      return new TypeParameterEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO, locationDTO, rawType
      );
    } else if (entity instanceof VariableEntity) {
      return new VariableEntityDTO(
          id, name, qualifiedName, parentId, file, additionalBinDTO, isGlobal, locationDTO,
          modifiers, rawType, hidden
          );
    } else {
      throw new RuntimeException("unimplemented entity type: " + entity.getClass().getName());
    }
  }

  public static EnreDTO JSONWriteRelation(Map<Integer, ArrayList<Tuple<Integer, Relation>>> relationMap, String hiddenPath, boolean slim) throws Exception {

    EnreDTO res = new EnreDTO();

    ProcessHidden processHidden = ProcessHidden.getProcessHiddeninstance();
    if (hiddenPath != null) {
      processHidden.convertCSV2DB(hiddenPath);
//            processHidden.outputConvertInfo("base-enre-out/hidden_convert.csv");
    }

    res.setSchemaVersion("1");
    Iterator<BaseEntity> iterator = singleCollect.getEntities().iterator();

    List<CategoryDTO> categories = new ArrayList<>();
    for (String i : List.of("Package", "File", "Class", "Interface", "Annotation", "Enum", "Method", "Variable", "EnumConstant", "AnnotationTypeMember")) {
      categories.add(new CategoryDTO(i));
    }
    res.setCategories(categories);

    RelationInf relationInf = new RelationInf();
    Map<String, Integer> entityNum = new HashMap<>();
    for (String entity : turnStrMap(relationInf.entityStatis()).keySet()) {
      entityNum.put(entity, turnStrMap(relationInf.entityStatis()).get(entity));
    }
    res.setEntityNum(entityNum);

    Map<String, Integer> relationNum = new HashMap<>();
    for (String relation : turnStrMap(relationInf.dependencyStatis()).keySet()) {
      relationNum.put(relation, turnStrMap(relationInf.dependencyStatis()).get(relation));
    }
    res.setRelationNum(relationNum);

//        JSONObject subCKIndices = new JSONObject();
//        for (String index : singleCollect.getCkIndices().keySet()){
//            subCKIndices.put(index, singleCollect.getCk(index));
//        }
//        obj.put("CKIndices", subCKIndices);

    List<EntityDTO> variables = new ArrayList<>();

    while (iterator.hasNext()) {
      BaseEntity entity = iterator.next();
      variables.add(parseEntityDTO(processHidden, entity, slim));
    }

    if (!slim) {
      for (ExternalEntity externalEntity : singleCollect.getExternalEntities()) {
        variables.add(parseExternalEntityDTO(externalEntity));
      }
    }

    res.setVariables(variables);

    List<CellDTO> cells = new ArrayList<>();
    for (int fromEntity : relationMap.keySet()) {
      for (Tuple<Integer, Relation> toEntityObj : relationMap.get(fromEntity)) {
        int toEntity = toEntityObj.getL();
//                for(Relation type : relationMap.get(fromEntity).get(toEntity)) {
        Relation type = toEntityObj.getR();
        CellDTO celLDTO = parseCellDTO(fromEntity, toEntity, type, slim);
        if (celLDTO != null) {
          cells.add(celLDTO);
        }
      }
    }
    res.setCells(cells);
    /**
     * Output not match hidden
     */
//        if (hiddenPath != null) {
//            processHidden.outputResult();
//        }
    return res;
  }

  public static String JSONWriteEntity(List<BaseEntity> entityList) throws Exception {


    JSONObject obj = new JSONObject();// 创建JSONObject对象

    obj.put("schemaVersion", "1");

    List<String> subObjVariable = new ArrayList<String>();// 创建对象数组里的子对象
    for (BaseEntity en : entityList) {
      subObjVariable.add(en.toString());
    }
    obj.put("variables", subObjVariable);

    return obj.toString();
  }

  public static String processRawType(String rawType) {
    if (rawType == null) {
      return null;
    }
//        else {
//            if (rawType.contains("<")){
////                String[] temp = rawType.split("<");
////                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
//                rawType = rawType.replaceAll("<", "&gt;");
//            }
//            if (rawType.contains(">")){
//                rawType = rawType.replaceAll(">", "\\>");
//            }
//            if (rawType.contains("[")){
////                String[] temp = rawType.split("\\[");
////                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
//                rawType = rawType.replaceAll("\\[", "\\[");
//            }
//            if (rawType.contains("]")){
//                rawType = rawType.replaceAll("]", "\\]");
//            }
//            if (rawType.contains(",")){
////                String[] temp = rawType.split(",");
////                rawType = processRawType(temp[0]).concat("-"+processRawType(temp[1]));
//                rawType = rawType.replaceAll(",", "\\,");
//            }
//            if (rawType.contains("java")){
//                String[] temp = rawType.split("\\.");
//                rawType = temp[temp.length - 1];
//            }
//            rawType = StringEscapeUtils.unescapeJava(rawType);
//        }
    return rawType;
  }

}
