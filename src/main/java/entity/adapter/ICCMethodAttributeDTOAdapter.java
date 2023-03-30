package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ICCMethodAttributeDTO;

import java.io.IOException;

public class ICCMethodAttributeDTOAdapter extends TypeAdapter<ICCMethodAttributeDTO> {
  @Override
  public void write(JsonWriter out, ICCMethodAttributeDTO value) throws IOException {
    out.beginObject();
    out.name("send").value(value.getSend());
    out.name("receive").value(value.getReceive());
    out.name("mechanismType").value(value.getMechanismType());
    out.endObject();
  }

  @Override
  public ICCMethodAttributeDTO read(JsonReader in) throws IOException {
    ICCMethodAttributeDTO res = new ICCMethodAttributeDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "send":
          res.setSend(in.nextBoolean());
          break;
        case "receive":
          res.setReceive(in.nextBoolean());
          break;
        case "mechanismType":
          res.setMechanismType(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
