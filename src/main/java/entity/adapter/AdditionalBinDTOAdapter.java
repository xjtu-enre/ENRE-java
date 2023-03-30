package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.AdditionalBinDTO;

import java.io.IOException;

public class AdditionalBinDTOAdapter extends TypeAdapter<AdditionalBinDTO> {
  @Override
  public void write(JsonWriter out, AdditionalBinDTO value) throws IOException {
    out.beginObject();
    out.name("binNum").value(value.getBinNum());
    out.name("binPath").value(value.getBinPath());
    out.endObject();
  }

  @Override
  public AdditionalBinDTO read(JsonReader in) throws IOException {
    AdditionalBinDTO res = new AdditionalBinDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "binNum":
          res.setBinNum(in.nextInt());
          break;
        case "binPath":
          res.setBinPath(in.nextString());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
