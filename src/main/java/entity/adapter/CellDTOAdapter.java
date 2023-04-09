package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.CellDTO;

import java.io.IOException;

public class CellDTOAdapter extends TypeAdapter<CellDTO> {

  protected final ValuesDTOAdapter valuesDTOAdapter;

  public CellDTOAdapter(ValuesDTOAdapter valuesDTOAdapter) {
    this.valuesDTOAdapter = valuesDTOAdapter;
  }

  @Override
  public void write(JsonWriter out, CellDTO value) throws IOException {
    out.beginObject();
    out.name("src").value(value.getSrc());
    out.name("dest").value(value.getDest());
    out.name("values");
    this.valuesDTOAdapter.write(out, value.getValues());
    out.endObject();
  }

  @Override
  public CellDTO read(JsonReader in) throws IOException {
    CellDTO res = new CellDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "src":
          res.setSrc(in.nextInt());
          break;
        case "dest":
          res.setDest(in.nextInt());
          break;
        case "values":
          res.setValues(this.valuesDTOAdapter.read(in));
          break;
      }
    }
    in.endObject();
    return res;
  }
}
