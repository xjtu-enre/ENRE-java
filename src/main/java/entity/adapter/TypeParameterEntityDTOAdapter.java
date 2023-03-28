package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.TypeParameterEntityDTO;

import java.io.IOException;

public class TypeParameterEntityDTOAdapter extends TypeAdapter<TypeParameterEntityDTO> {

  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;
  private final LocationDTOAdapter locationDTOAdapter;

  public TypeParameterEntityDTOAdapter() {
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
    this.locationDTOAdapter = new LocationDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, TypeParameterEntityDTO value) throws IOException {
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
    out.name("location");
    this.locationDTOAdapter.write(out, value.getLocation());
    out.name("rawType").value(value.getRawType());
    out.endObject();
  }

  @Override
  public TypeParameterEntityDTO read(JsonReader in) throws IOException {
    TypeParameterEntityDTO res = new TypeParameterEntityDTO();
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
      }
    }
    in.endObject();
    return res;
  }
}
