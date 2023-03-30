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
    out.name("isSynchronized").value(value.getSynchronized());
    out.name("isStatic").value(value.getStatic());
    out.name("isGetter").value(value.getGetter());
    out.name("isSetter").value(value.getSetter());
    out.name("isPublic").value(value.getPublic());
    out.name("isDelegator").value(value.getDelegator());
    out.name("isConstructor").value(value.getConstructor());
    out.name("isOverride").value(value.getOverride());
    out.name("isAbstract").value(value.getAbstract());
    out.name("isRecursive").value(value.getRecursive());
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
