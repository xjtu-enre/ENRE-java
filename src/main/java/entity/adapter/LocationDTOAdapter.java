package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.LocationDTO;

import java.io.IOException;

public class LocationDTOAdapter extends TypeAdapter<LocationDTO> {

  @Override
  public void write(JsonWriter out, LocationDTO value) throws IOException {
    out.beginObject();
    out.name("startLine").value(value.getStartLine());
    out.name("endLine").value(value.getEndLine());
    out.name("startColumn").value(value.getStartColumn());
    out.name("endColumn").value(value.getEndColumn());
    out.endObject();
  }

  @Override
  public LocationDTO read(JsonReader in) throws IOException {
    LocationDTO res = new LocationDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "startLine":
          res.setStartLine(in.nextInt());
          break;
        case "endLine":
          res.setEndLine(in.nextInt());
          break;
        case "startColumn":
          res.setStartColumn(in.nextInt());
          break;
        case "endColumn":
          res.setEndColumn(in.nextInt());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
