package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.CategoryDTO;
import entity.dto.CellDTO;
import entity.dto.EnreDTO;
import entity.dto.EntityDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnreDTOAdapter extends TypeAdapter<EnreDTO> {

  private final CellDTOAdapter cellDTOAdapter;
  private final MapAdapter mapAdapter;
  private final CategoryDTOAdapter categoryDTOAdapter;
  private final EntityDTOAdapter entityDTOAdapter;

  public EnreDTOAdapter(CellDTOAdapter cellDTOAdapter, MapAdapter mapAdapter, CategoryDTOAdapter categoryDTOAdapter, EntityDTOAdapter entityDTOAdapter) {
    this.cellDTOAdapter = cellDTOAdapter;
    this.mapAdapter = mapAdapter;
    this.categoryDTOAdapter = categoryDTOAdapter;
    this.entityDTOAdapter = entityDTOAdapter;
  }

  @Override
  public void write(JsonWriter out, EnreDTO value) throws IOException {
    out.beginObject();
    out.name("schemaVersion").value(value.getSchemaVersion());
    out.name("variables");
    out.beginArray();
    for (EntityDTO variable : value.getVariables()) {
      this.entityDTOAdapter.write(out, variable);
    }
    out.endArray();
    out.name("cells");
    out.beginArray();
    for (CellDTO cell : value.getCells()) {
      this.cellDTOAdapter.write(out, cell);
    }
    out.endArray();
    out.name("entityNum");
    this.mapAdapter.write(out, value.getEntityNum());
    out.name("relationNum");
    this.mapAdapter.write(out, value.getRelationNum());
    out.name("categories");
    out.beginArray();
    for (CategoryDTO category : value.getCategories()) {
      this.categoryDTOAdapter.write(out, category);
    }
    out.endArray();
    out.endObject();
  }

  @Override
  public EnreDTO read(JsonReader in) throws IOException {
    EnreDTO res = new EnreDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "schemaVersion":
          res.setSchemaVersion(in.nextString());
          break;
        case "variables":
          List<EntityDTO> variables = new ArrayList<>();
          in.beginArray();
          while (in.hasNext()) {
            variables.add(this.entityDTOAdapter.read(in));
          }
          in.endArray();
          res.setVariables(variables);
          break;
        case "cells":
          List<CellDTO> cells = new ArrayList<>();
          in.beginArray();
          while (in.hasNext()) {
            cells.add(this.cellDTOAdapter.read(in));
          }
          in.endObject();
          res.setCells(cells);
          break;
        case "entityNum":
          res.setEntityNum(this.mapAdapter.read(in));
          break;
        case "relationNum":
          res.setRelationNum(this.mapAdapter.read(in));
          break;
        case "categories":
          List<CategoryDTO> categories = new ArrayList<>();
          in.beginArray();
          categories.add(this.categoryDTOAdapter.read(in));
          in.endArray();
          res.setCategories(categories);
      }
    }
    in.endObject();
    return res;
  }
}
