package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.ValuesDTO;

import java.io.IOException;
import java.util.Map;

public class ValuesDTOAdapter extends TypeAdapter<ValuesDTO> {

  private final LocationDTOAdapter locationDTOAdapter;

  public ValuesDTOAdapter() {
    this.locationDTOAdapter = new LocationDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, ValuesDTO value) throws IOException {
    out.beginObject();
    if (value.getLoc() != null) {
      out.name("loc");
      this.locationDTOAdapter.write(out, value.getLoc());
    }
    for (Map.Entry<String, Integer> entry : value.getRelations().entrySet()) {
      out.name(entry.getKey()).value(entry.getValue());
    }
    if (value.getIccMechanism() != null) {
      out.name("iccMechanism").value(value.getIccMechanism());
    }
    if (value.getIccCategory() != null) {
      out.name("iccCategory").value(value.getIccCategory());
    }
    if (value.getInvoke() != null) {
      out.name("invoke").value(value.getInvoke());
    }
    if (value.getBindVar() != null) {
      out.name("bindVar").value(value.getBindVar());
    }
    if (value.getModifyAccessible() != null) {
      out.name("modifyAccessible").value(value.getModifyAccessible());
    }
    if (value.getArguments() != null) {
      out.name("arguments").value(value.getArguments());
    }
    out.endObject();
  }

  @Override
  public ValuesDTO read(JsonReader in) throws IOException {
    ValuesDTO res = new ValuesDTO();
    in.beginObject();
    while (in.hasNext()) {
      String key = in.nextName();
      switch (key) {
        case "loc":
          res.setLoc(this.locationDTOAdapter.read(in));
          break;
        case "invoke":
          res.setInvoke(in.nextBoolean());
          break;
        case "bindVar":
          res.setBindVar(in.nextInt());
          break;
        case "modifyAccessible":
          res.setModifyAccessible(in.nextBoolean());
          break;
        case "arguments":
          res.setArguments(in.nextString());
          break;
        case "iccMechanism":
          res.setIccMechanism(in.nextString());
          break;
        case "iccCategory":
          res.setIccCategory(in.nextString());
          break;
        default:
          res.addRelation(key, in.nextInt());
      }
    }
    in.endObject();
    return res;
  }
}
