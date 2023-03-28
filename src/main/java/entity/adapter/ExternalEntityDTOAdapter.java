package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ExternalEntityDTO;

import java.io.IOException;

public class ExternalEntityDTOAdapter extends TypeAdapter<ExternalEntityDTO> {

  @Override
  public void write(JsonWriter out, ExternalEntityDTO value) throws IOException {
    out.beginObject();
    out.name("external").value(value.isExternal());
    out.name("id").value(value.getId());
    out.name("name").value(value.getName());
    out.name("qualifiedName").value(value.getQualifiedName());
    out.endObject();
  }

  @Override
  public ExternalEntityDTO read(JsonReader in) throws IOException {
    ExternalEntityDTO res = new ExternalEntityDTO();
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
      }
    }
    in.endObject();
    return res;
  }
}
