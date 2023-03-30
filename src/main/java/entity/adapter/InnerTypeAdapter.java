package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InnerTypeAdapter extends TypeAdapter<List<Integer>> {

  @Override
  public void write(JsonWriter out, List<Integer> value) throws IOException {
    out.beginArray();
    for (Integer s : value) {
      out.value(s);
    }
    out.endArray();
  }

  @Override
  public List<Integer> read(JsonReader in) throws IOException {
    List<Integer> innerType = new ArrayList<>();
    String key = in.nextName();
    assert key.equals("innerType");
    in.beginArray();
    while (in.hasNext()) {
      innerType.add(in.nextInt());
    }
    in.endArray();
    return innerType;
  }
}
