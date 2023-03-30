package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.EnumEntityDTO;

import java.io.IOException;

public class EnumEntityDTOAdapter extends TypeAdapter<EnumEntityDTO> {

  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;
  private final LocationDTOAdapter locationDTOAdapter;

  public EnumEntityDTOAdapter() {
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
    this.locationDTOAdapter = new LocationDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, EnumEntityDTO value) throws IOException {
    out.beginObject();
    out.name("external").value(value.isExternal());
    out.name("id").value(value.getId());
    out.name("name").value(value.getName());
    out.name("qualifiedName").value(value.getQualifiedName());
    out.name("category").value(value.getCategory());
    out.name("parentId").value(value.getParentId());
    out.name("File").value(value.getFile());
    this.additionalBinDTOAdapter.write(out, value.getAdditionalBin());
    this.locationDTOAdapter.write(out, value.getLocation());
    out.name("modifiers").value(value.getModifiers());
    out.name("rawType").value(value.getRawType());
    out.endObject();
  }

  @Override
  public EnumEntityDTO read(JsonReader in) throws IOException {
    EnumEntityDTO res = new EnumEntityDTO();
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
        case "location":
          res.setLocation(this.locationDTOAdapter.read(in));
          break;
        case "rawType":
          res.setRawType(in.nextString());
          break;
        case "modifiers":
          res.setModifiers(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
