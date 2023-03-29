package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.MethodEntityDTO;

import java.io.IOException;

public class ParameterDTOAdapter extends TypeAdapter<MethodEntityDTO.ParameterDTO> {
  @Override
  public void write(JsonWriter out, MethodEntityDTO.ParameterDTO value) throws IOException {
    out.beginObject();
    out.name("types").value(value.getTypes());
    out.name("names").value(value.getNames());
    out.endObject();
  }

  @Override
  public MethodEntityDTO.ParameterDTO read(JsonReader in) throws IOException {
    MethodEntityDTO.ParameterDTO res = new MethodEntityDTO.ParameterDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "types":
          res.setTypes(in.nextString());
          break;
        case "names":
          res.setNames(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
