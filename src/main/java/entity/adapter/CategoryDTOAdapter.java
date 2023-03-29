package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.CategoryDTO;

import java.io.IOException;

public class CategoryDTOAdapter extends TypeAdapter<CategoryDTO> {

  @Override
  public void write(JsonWriter out, CategoryDTO value) throws IOException {
    out.beginObject();
    out.name("name").value(value.getName());
    out.endObject();
  }

  @Override
  public CategoryDTO read(JsonReader in) throws IOException {
    CategoryDTO res = new CategoryDTO();
    while (in.hasNext()) {
      String key = in.nextName();
      assert key.equals("name");
      res.setName(in.nextString());
    }
    return res;
  }
}
