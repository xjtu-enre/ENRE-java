package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ComponentDTO;

import java.io.IOException;

public class ComponentDTOAdapter extends TypeAdapter<ComponentDTO> {

  @Override
  public void write(JsonWriter out, ComponentDTO value) throws IOException {
    out.beginObject();
    out.name("componentCategory").value(value.getComponentCategory());
    out.endObject();
  }

  @Override
  public ComponentDTO read(JsonReader in) throws IOException {
    ComponentDTO res = new ComponentDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "componentCategory":
          res.setComponentCategory(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
