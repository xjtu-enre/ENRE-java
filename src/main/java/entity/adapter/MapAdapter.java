package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapAdapter extends TypeAdapter<Map<String, Integer>> {

  @Override
  public void write(JsonWriter out, Map<String, Integer> value) throws IOException {
    out.beginObject();
    for (Map.Entry<String, Integer> entry : value.entrySet()) {
      out.name(entry.getKey()).value(entry.getValue());
    }
    out.endObject();
  }

  @Override
  public Map<String, Integer> read(JsonReader in) throws IOException {
    Map<String, Integer> res = new HashMap<>();
    in.beginObject();
    while (in.hasNext()) {
      String key = in.nextName();
      res.put(key, in.nextInt());
    }
    in.endObject();
    return res;
  }
}
