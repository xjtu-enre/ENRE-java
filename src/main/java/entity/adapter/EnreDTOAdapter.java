package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EnreDTOAdapter extends TypeAdapter<EnreDTO> {

  @Override
  public void write(JsonWriter out, EnreDTO value) throws IOException {
    out.beginObject();
    out.name("schemaVersion").value(value.getSchemaVersion());
    out.name("variables");
    out.beginArray();
    for (EntityDTO variable : value.getVariables()) {
      writeEntityDTO(out, variable);
    }
    out.endArray();
    out.name("cells");
    out.beginArray();
    for (CellDTO cell : value.getCells()) {
      writeCellDTO(out, cell);
    }
    out.endArray();
    writeMap(out, value.getEntityNum(), "entityNum");
    writeMap(out, value.getRelationNum(), "relationNum");
    writeCategories(out, value.getCategories());
    out.endObject();
  }

  private void writeCategories(JsonWriter out, List<CategoryDTO> categories) throws IOException {
    out.name("categories");
    out.beginArray();
    for (CategoryDTO category : categories) {
      out.beginObject();
      out.name("name").value(category.getName());
      out.endObject();
    }
    out.endArray();
  }

  private void writeMap(JsonWriter out, Map<String, Integer> map, String key) throws IOException {
    out.name(key);
    out.beginObject();
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      out.name(entry.getKey()).value(entry.getValue());
    }
    out.endObject();
  }

  private void writeValuesDTO(JsonWriter out, ValuesDTO obj) throws IOException {
    out.name("values");
    out.beginObject();
    writeLocationDTO(out, obj.getLoc(), "loc");
    for (Map.Entry<String, Integer> entry : obj.getRelations().entrySet()) {
      out.name(entry.getKey()).value(entry.getValue());
    }
    if (obj.getInvoke() != null) {
      out.name("invoke").value(obj.getInvoke());
    }
    if (obj.getBindVar() != null) {
      out.name("bindVar").value(obj.getBindVar());
    }
    if (obj.getModifyAccessible() != null) {
      out.name("modifyAccessible").value(obj.getModifyAccessible());
    }
    if (obj.getArguments() != null) {
      out.name("arguments").value(obj.getArguments());
    }
    out.endObject();
  }

  private void writeCellDTO(JsonWriter out, CellDTO cell) throws IOException {
    out.beginObject();
    out.name("src").value(cell.getSrc());
    out.name("dest").value(cell.getDest());
    writeValuesDTO(out, cell.getValues());
    out.endObject();
  }

  private void writeFile(JsonWriter out, String file) throws IOException {
    if (file == null) {
      return;
    }
    out.name("File").value(file);
  }

  private void writeModifiers(JsonWriter out, String modifiers) throws IOException {
    if (modifiers == null) {
      return;
    }
    out.name("modifiers").value(modifiers);
  }

  private void writeRawType(JsonWriter out, String rawType) throws IOException {
    if (rawType == null) {
      return;
    }
    out.name("rawType").value(rawType);
  }

  private void writeAdditionalBinDTO(JsonWriter out, AdditionalBinDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("additionalBin");
    out.beginObject();
    out.name("binNum").value(obj.getBinNum());
    out.name("binPath").value(obj.getBinPath());
    out.endObject();
  }

  private void writeLocationDTO(JsonWriter out, LocationDTO obj, String key) throws IOException {
    if (obj == null) {
      return;
    }
    out.name(key);
    out.beginObject();
    out.name("startLine").value(obj.getStartLine());
    out.name("endLine").value(obj.getEndLine());
    out.name("startColumn").value(obj.getStartColumn());
    out.name("endColumn").value(obj.getEndColumn());
    out.endObject();
  }

  private void writeEnhancementDTO(JsonWriter out, MethodEntityDTO.EnhancementDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("enhancement");
    out.beginObject();
    out.name("synchronized").value(obj.getSynchronized());
    out.name("static").value(obj.getStatic());
    out.name("getter").value(obj.getGetter());
    out.name("setter").value(obj.getSetter());
    out.name("public").value(obj.getPublic());
    out.name("delegator").value(obj.getDelegator());
    out.name("constructor").value(obj.getConstructor());
    out.name("override").value(obj.getOverride());
    out.name("abstract").value(obj.getAbstract());
    out.name("recursive").value(obj.getRecursive());
    out.endObject();
  }

  private void writeParameterDTO(JsonWriter out, MethodEntityDTO.ParameterDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("parameter");
    out.beginObject();
    out.name("types").value(obj.getTypes());
    out.name("names").value(obj.getNames());
    out.endObject();
  }

  private void writeInnerType(JsonWriter out, List<Integer> innerType) throws IOException {
    if (innerType == null) {
      return;
    }
    out.name("innerType");
    out.beginArray();
    for (Integer integer : innerType) {
      out.value(integer);
    }
    out.endArray();
  }

  private void writeEntityDTO(JsonWriter out, EntityDTO variable) throws IOException {
    out.beginObject();
    out.name("external").value(variable.isExternal());
    out.name("id").value(variable.getId());
    out.name("qualifiedName").value(variable.getQualifiedName());
    out.name("name").value(variable.getName());
    if (!variable.isExternal()) {
      InternalEntityDTO inter = (InternalEntityDTO) variable;
      out.name("category").value(inter.getCategory());
      out.name("parentId").value(inter.getParentId());
      if (inter instanceof PackageEntityDTO) {
      } else if (inter instanceof FileEntityDTO) {
        FileEntityDTO entity = (FileEntityDTO) inter;
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
      } else if (inter instanceof ClassEntityDTO) {
        ClassEntityDTO entity = (ClassEntityDTO) inter;
        writeRawType(out, entity.getRawType());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeInnerType(out, entity.getInnerType());
      } else if (inter instanceof AnonymousClassEntityDTO) {
        AnonymousClassEntityDTO entity = (AnonymousClassEntityDTO) inter;
        writeRawType(out, entity.getRawType());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        out.name("anonymousBindVar").value(entity.getAnonymousBindVar());
        out.name("anonymousRank").value(entity.getAnonymousRank());
      } else if (inter instanceof EnumEntityDTO) {
        EnumEntityDTO entity = (EnumEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof EnumConstantEntityDTO) {
        EnumConstantEntityDTO entity = (EnumConstantEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
      } else if (inter instanceof AnnotationEntityDTO) {
        AnnotationEntityDTO entity = (AnnotationEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof AnnotationMemberEntityDTO) {
        AnnotationMemberEntityDTO entity = (AnnotationMemberEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof InterfaceEntityDTO) {
        InterfaceEntityDTO entity = (InterfaceEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof MethodEntityDTO) {
        MethodEntityDTO entity = (MethodEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeEnhancementDTO(out, entity.getEnhancement());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeParameterDTO(out, entity.getParameter());
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof TypeParameterEntityDTO) {
        TypeParameterEntityDTO entity = (TypeParameterEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeRawType(out, entity.getRawType());
      } else if (inter instanceof VariableEntityDTO) {
        VariableEntityDTO entity = (VariableEntityDTO) inter;
        writeFile(out, entity.getFile());
        writeAdditionalBinDTO(out, entity.getAdditionalBin());
        out.name("global").value(entity.getGlobal());
        writeLocationDTO(out, entity.getLocation(), "location");
        writeModifiers(out, entity.getModifiers());
        writeRawType(out, entity.getRawType());
      }
    }
    out.endObject();
  }

  @Override
  public EnreDTO read(JsonReader in) throws IOException {
    return null;
  }
}
