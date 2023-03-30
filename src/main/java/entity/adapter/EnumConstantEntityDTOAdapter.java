package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ClassEntityDTO;
import entity.dto.EnumConstantEntityDTO;

import java.io.IOException;

public class EnumConstantEntityDTOAdapter extends TypeAdapter<EnumConstantEntityDTO> {

  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;

  public EnumConstantEntityDTOAdapter() {
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, EnumConstantEntityDTO value) throws IOException {
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
    out.endObject();
  }

  @Override
  public EnumConstantEntityDTO read(JsonReader in) throws IOException {
    EnumConstantEntityDTO res = new EnumConstantEntityDTO();
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
      }
    }
    in.endObject();
    return res;
  }
}
