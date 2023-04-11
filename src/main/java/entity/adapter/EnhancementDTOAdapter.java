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
        case "isSynchronized":
          res.setSynchronized(in.nextBoolean());
          break;
        case "isStatic":
          res.setStatic(in.nextBoolean());
          break;
        case "isGetter":
          res.setGetter(in.nextBoolean());
          break;
        case "isSetter":
          res.setSetter(in.nextBoolean());
          break;
        case "isPublic":
          res.setPublic(in.nextBoolean());
          break;
        case "isDelegator":
          res.setDelegator(in.nextBoolean());
          break;
        case "isConstructor":
          res.setConstructor(in.nextBoolean());
          break;
        case "isOverride":
          res.setOverride(in.nextBoolean());
          break;
        case "isAbstract":
          res.setAbstract(in.nextBoolean());
          break;
        case "isRecursive":
          res.setRecursive(in.nextBoolean());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
