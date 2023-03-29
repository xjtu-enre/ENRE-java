package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ICCMethodAttributeDTO;
import entity.dto.ICCVariableAttributeDTO;

import java.io.IOException;

public class ICCVariableAttributeDTOAdapter extends TypeAdapter<ICCVariableAttributeDTO> {

  @Override
  public void write(JsonWriter out, ICCVariableAttributeDTO value) throws IOException {
    out.beginObject();
    out.name("data").value(value.getData());
    out.name("flags").value(value.getFlags());
    out.name("extras").value(value.getExtras());
    out.name("action").value(value.getAction());
    out.name("type").value(value.getType());
    out.name("category").value(value.getCategory());
    out.endObject();
  }

  @Override
  public ICCVariableAttributeDTO read(JsonReader in) throws IOException {
    ICCVariableAttributeDTO res = new ICCVariableAttributeDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "data":
          res.setData(in.nextString());
          break;
        case "flags":
          res.setFlags(in.nextString());
          break;
        case "extras":
          res.setExtras(in.nextString());
          break;
        case "action":
          res.setAction(in.nextString());
          break;
        case "type":
          res.setType(in.nextString());
          break;
        case "category":
          res.setCategory(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
