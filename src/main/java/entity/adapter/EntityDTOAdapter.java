package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.*;

import java.io.IOException;
import java.util.List;

public class EntityDTOAdapter extends TypeAdapter<EntityDTO> {

  private final LocationDTOAdapter locationDTOAdapter;
  private final EnhancementDTOAdapter enhancementDTOAdapter;
  private final ParameterDTOAdapter parameterDTOAdapter;
  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;

  public EntityDTOAdapter() {
    this.locationDTOAdapter = new LocationDTOAdapter();
    this.enhancementDTOAdapter = new EnhancementDTOAdapter();
    this.parameterDTOAdapter = new ParameterDTOAdapter();
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
  }

  private void writeLocationDTO(JsonWriter out, LocationDTO obj, String key) throws IOException {
    if (obj == null) {
      return;
    }
    out.name(key);
    this.locationDTOAdapter.write(out, obj);
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
    this.additionalBinDTOAdapter.write(out, obj);
  }

  private void writeEnhancementDTO(JsonWriter out, MethodEntityDTO.EnhancementDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("enhancement");
    this.enhancementDTOAdapter.write(out, obj);
  }

  private void writeParameterDTO(JsonWriter out, MethodEntityDTO.ParameterDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("parameter");
    this.parameterDTOAdapter.write(out, obj);
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

  @Override
  public void write(JsonWriter out, EntityDTO value) throws IOException {
    out.beginObject();
    out.name("external").value(value.isExternal());
    out.name("id").value(value.getId());
    out.name("qualifiedName").value(value.getQualifiedName());
    out.name("name").value(value.getName());
    if (!value.isExternal()) {
      InternalEntityDTO inter = (InternalEntityDTO) value;
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
  public EntityDTO read(JsonReader in) throws IOException {
    // TODO not implemented yet
    return null;
  }
}
