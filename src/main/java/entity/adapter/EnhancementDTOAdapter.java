package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.MethodEntityDTO;

import java.io.IOException;

public class EnhancementDTOAdapter extends TypeAdapter<MethodEntityDTO.EnhancementDTO> {

  @Override
  public void write(JsonWriter out, MethodEntityDTO.EnhancementDTO value) throws IOException {
    out.beginObject();
    out.name("synchronized").value(value.getSynchronized());
    out.name("static").value(value.getStatic());
    out.name("getter").value(value.getGetter());
    out.name("setter").value(value.getSetter());
    out.name("public").value(value.getPublic());
    out.name("delegator").value(value.getDelegator());
    out.name("constructor").value(value.getConstructor());
    out.name("override").value(value.getOverride());
    out.name("abstract").value(value.getAbstract());
    out.name("recursive").value(value.getRecursive());
    out.endObject();
  }

  @Override
  public MethodEntityDTO.EnhancementDTO read(JsonReader in) throws IOException {
    MethodEntityDTO.EnhancementDTO res = new MethodEntityDTO.EnhancementDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "synchronized":
          res.setSynchronized(in.nextBoolean());
          break;
        case "static":
          res.setStatic(in.nextBoolean());
          break;
        case "getter":
          res.setGetter(in.nextBoolean());
          break;
        case "setter":
          res.setSetter(in.nextBoolean());
          break;
        case "public":
          res.setPublic(in.nextBoolean());
          break;
        case "delegator":
          res.setDelegator(in.nextBoolean());
          break;
        case "constructor":
          res.setConstructor(in.nextBoolean());
          break;
        case "override":
          res.setOverride(in.nextBoolean());
          break;
        case "abstract":
          res.setAbstract(in.nextBoolean());
          break;
        case "recursive":
          res.setRecursive(in.nextBoolean());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
