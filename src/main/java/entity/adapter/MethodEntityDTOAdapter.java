package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.MethodEntityDTO;

import java.io.IOException;

public class MethodEntityDTOAdapter extends TypeAdapter<MethodEntityDTO> {

  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;
  private final LocationDTOAdapter locationDTOAdapter;
  private final EnhancementDTOAdapter enhancementDTOAdapter;
  private final ParameterDTOAdapter parameterDTOAdapter;

  public MethodEntityDTOAdapter() {
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
    this.locationDTOAdapter = new LocationDTOAdapter();
    this.enhancementDTOAdapter = new EnhancementDTOAdapter();
    this.parameterDTOAdapter = new ParameterDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, MethodEntityDTO value) throws IOException {
    out.beginObject();
    out.name("external").value(value.isExternal());
    out.name("id").value(value.getId());
    out.name("name").value(value.getName());
    out.name("qualifiedName").value(value.getQualifiedName());
    out.name("category").value(value.getCategory());
    out.name("parentId").value(value.getParentId());
    out.name("File").value(value.getFile());
    out.name("additionalBin");
    this.additionalBinDTOAdapter.write(out, value.getAdditionalBin());
    out.name("enhancement");
    this.enhancementDTOAdapter.write(out, value.getEnhancement());
    out.name("location");
    this.locationDTOAdapter.write(out, value.getLocation());
    out.name("modifiers").value(value.getModifiers());
    out.name("parameter");
    this.parameterDTOAdapter.write(out, value.getParameter());
    out.name("rawType").value(value.getRawType());
    out.endObject();
  }

  @Override
  public MethodEntityDTO read(JsonReader in) throws IOException {
    MethodEntityDTO res = new MethodEntityDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "external":
          res.setExternal(in.nextBoolean());
          break;
        case "id":
          res.setId(in.nextInt());
          break;
        case "name":
          res.setName(in.nextString());
          break;
        case "qualifiedName":
          res.setQualifiedName(in.nextString());
          break;
        case "category":
          res.setCategory(in.nextString());
          break;
        case "parentId":
          res.setParentId(in.nextInt());
          break;
        case "File":
          res.setFile(in.nextString());
          break;
        case "additionalBin":
          res.setAdditionalBin(this.additionalBinDTOAdapter.read(in));
          break;
        case "enhancement":
          res.setEnhancement(this.enhancementDTOAdapter.read(in));
          break;
        case "location":
          res.setLocation(this.locationDTOAdapter.read(in));
          break;
        case "modifiers":
          res.setModifiers(in.nextString());
          break;
        case "parameter":
          res.setParameter(this.parameterDTOAdapter.read(in));
          break;
        case "rawType":
          res.setRawType(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
