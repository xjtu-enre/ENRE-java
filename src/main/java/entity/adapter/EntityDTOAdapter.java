package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class EntityDTOAdapter extends TypeAdapter<EntityDTO> {

  private final LocationDTOAdapter locationDTOAdapter;
  private final EnhancementDTOAdapter enhancementDTOAdapter;
  private final ParameterDTOAdapter parameterDTOAdapter;
  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;
  private final ComponentDTOAdapter componentDTOAdapter;
  private final ICCMethodAttributeDTOAdapter iccMethodAttributeDTOAdapter;
  private final ICCVariableAttributeDTOAdapter iccVariableAttributeDTOAdapter;

  public EntityDTOAdapter() {
    this.locationDTOAdapter = new LocationDTOAdapter();
    this.enhancementDTOAdapter = new EnhancementDTOAdapter();
    this.parameterDTOAdapter = new ParameterDTOAdapter();
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
    this.componentDTOAdapter = new ComponentDTOAdapter();
    this.iccMethodAttributeDTOAdapter = new ICCMethodAttributeDTOAdapter();
    this.iccVariableAttributeDTOAdapter = new ICCVariableAttributeDTOAdapter();
  }

  protected void writeLocationDTO(JsonWriter out, LocationDTO obj, String key) throws IOException {
    if (obj == null) {
      return;
    }
    out.name(key);
    this.locationDTOAdapter.write(out, obj);
  }

  protected void writeHidden(JsonWriter out, String hidden) throws IOException {
    if (hidden == null) {
      return;
    }
    out.name("hidden").value(hidden);
  }

  protected void writeFile(JsonWriter out, String file) throws IOException {
    if (file == null) {
      return;
    }
    out.name("File").value(file);
  }

  protected void writeModifiers(JsonWriter out, String modifiers) throws IOException {
    if (modifiers == null) {
      return;
    }
    out.name("modifiers").value(modifiers);
  }

  protected void writeRawType(JsonWriter out, String rawType) throws IOException {
    if (rawType == null) {
      return;
    }
    out.name("rawType").value(rawType);
  }

  protected void writeAdditionalBinDTO(JsonWriter out, AdditionalBinDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("additionalBin");
    this.additionalBinDTOAdapter.write(out, obj);
  }

  protected void writeEnhancementDTO(JsonWriter out, MethodEntityDTO.EnhancementDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("enhancement");
    this.enhancementDTOAdapter.write(out, obj);
  }

  protected void writeParameterDTO(JsonWriter out, MethodEntityDTO.ParameterDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("parameter");
    this.parameterDTOAdapter.write(out, obj);
  }

  protected void writeInnerType(JsonWriter out, List<Integer> innerType) throws IOException {
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

  protected void writeICCMethodAttributeDTO(JsonWriter out, ICCMethodAttributeDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("iccMethodAttribute");
    this.iccMethodAttributeDTOAdapter.write(out, obj);
  }

  protected void writeICCVariableAttributeDTO(JsonWriter out, ICCVariableAttributeDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("iccVariableAttribute");
    this.iccVariableAttributeDTOAdapter.write(out, obj);
  }

  protected void writeComponentDTO(JsonWriter out, ComponentDTO obj) throws IOException {
    if (obj == null) {
      return;
    }
    out.name("component");
    this.componentDTOAdapter.write(out, obj);
  }

  protected void write(JsonWriter out, PackageEntityDTO value) {
    return;
  }

  protected void write(JsonWriter out, FileEntityDTO value) throws IOException {
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeFile(out, value.getFile());
  }

  protected void write(JsonWriter out, ClassEntityDTO value) throws IOException {
    writeRawType(out, value.getRawType());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeInnerType(out, value.getInnerType());
    writeHidden(out, value.getHidden());
    writeComponentDTO(out, value.getComponent());
  }

  protected void write(JsonWriter out, AnonymousClassEntityDTO value) throws IOException {
    writeRawType(out, value.getRawType());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeComponentDTO(out, value.getComponent());
    writeHidden(out, value.getHidden());
    out.name("anonymousBindVar").value(value.getAnonymousBindVar());
    out.name("anonymousRank").value(value.getAnonymousRank());
  }

  protected void write(JsonWriter out, EnumEntityDTO value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeRawType(out, value.getRawType());
    writeHidden(out, value.getHidden());
  }

  protected void write(JsonWriter out, EnumConstantEntityDTO value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeHidden(out, value.getHidden());
  }

  protected void write(JsonWriter out, AnnotationEntityDTO value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeRawType(out, value.getRawType());
    writeHidden(out, value.getHidden());
  }

  protected void write(JsonWriter out, AnnotationMemberEntityDTO  value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeLocationDTO(out, value.getLocation(), "location");
    writeRawType(out, value.getRawType());
  }

  protected void write(JsonWriter out, InterfaceEntityDTO value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeHidden(out, value.getHidden());
    writeRawType(out, value.getRawType());
  }

  protected void write(JsonWriter out, MethodEntityDTO value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeEnhancementDTO(out, value.getEnhancement());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeParameterDTO(out, value.getParameter());
    writeRawType(out, value.getRawType());
    writeHidden(out, value.getHidden());
    writeICCMethodAttributeDTO(out, value.getIccMethodAttribute());
  }

  protected void write(JsonWriter out, TypeParameterEntityDTO  value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    writeLocationDTO(out, value.getLocation(), "location");
    writeRawType(out, value.getRawType());
  }

  protected void write(JsonWriter out, VariableEntityDTO  value) throws IOException {
    writeFile(out, value.getFile());
    writeAdditionalBinDTO(out, value.getAdditionalBin());
    out.name("global").value(value.getGlobal());
    writeLocationDTO(out, value.getLocation(), "location");
    writeModifiers(out, value.getModifiers());
    writeRawType(out, value.getRawType());
    writeHidden(out, value.getHidden());
    writeICCVariableAttributeDTO(out, value.getIccVariableAttribute());
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
        write(out, (PackageEntityDTO) inter);
      } else if (inter instanceof FileEntityDTO) {
        write(out, (FileEntityDTO) inter);
      } else if (inter instanceof ClassEntityDTO) {
        write(out, (ClassEntityDTO) inter);
      } else if (inter instanceof AnonymousClassEntityDTO) {
        write(out, (AnonymousClassEntityDTO) inter);
      } else if (inter instanceof EnumEntityDTO) {
        write(out, (EnumEntityDTO) inter);
      } else if (inter instanceof EnumConstantEntityDTO) {
        write(out, (EnumConstantEntityDTO) inter);
      } else if (inter instanceof AnnotationEntityDTO) {
        write(out, (AnnotationEntityDTO) inter);
      } else if (inter instanceof AnnotationMemberEntityDTO) {
        write(out, (AnnotationMemberEntityDTO) inter);
      } else if (inter instanceof InterfaceEntityDTO) {
        write(out, (InterfaceEntityDTO) inter);
      } else if (inter instanceof MethodEntityDTO) {
         write(out, (MethodEntityDTO) inter);
      } else if (inter instanceof TypeParameterEntityDTO) {
        write(out, (TypeParameterEntityDTO) inter);
      } else if (inter instanceof VariableEntityDTO) {
        write(out, (VariableEntityDTO) inter);
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
