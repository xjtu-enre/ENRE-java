package entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import entity.dto.AnonymousClassEntityDTO;

import java.io.IOException;

public class AnonymousClassEntityDTOAdapter extends TypeAdapter<AnonymousClassEntityDTO> {

  private final LocationDTOAdapter locationDTOAdapter;
  private final AdditionalBinDTOAdapter additionalBinDTOAdapter;

  public AnonymousClassEntityDTOAdapter() {
    this.locationDTOAdapter = new LocationDTOAdapter();
    this.additionalBinDTOAdapter = new AdditionalBinDTOAdapter();
  }

  @Override
  public void write(JsonWriter out, AnonymousClassEntityDTO value) throws IOException {
    out.beginObject();
    out.name("external").value(value.isExternal());
    out.name("id").value(value.getId());
    out.name("name").value(value.getName());
    out.name("qualifiedName").value(value.getQualifiedName());
    out.name("category").value(value.getCategory());
    out.name("parentId").value(value.getParentId());
    out.name("rawType").value(value.getRawType());
    out.name("location");
    this.locationDTOAdapter.write(out, value.getLocation());
    out.name("modifiers").value(value.getModifiers());
    out.name("File").value(value.getFile());
    out.name("additionalBin");
    this.additionalBinDTOAdapter.write(out, value.getAdditionalBin());
    out.name("anonymousBindVar").value(value.getAnonymousBindVar());
    out.name("anonymousRank").value(value.getAnonymousRank());
    out.endObject();
  }

  @Override
  public AnonymousClassEntityDTO read(JsonReader in) throws IOException {
    AnonymousClassEntityDTO res = new AnonymousClassEntityDTO();
    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case "external":
          res.setExternal(in.nextBoolean());
          break;
        case "id":
          res.setId(in.nextInt());
          break;
        case "name":
          res.setName(in.nextString());
          break;
        case "qualifiedName":
          res.setQualifiedName(in.nextString());
          break;
        case "category":
          res.setCategory(in.nextString());
          break;
        case "parentId":
          res.setParentId(in.nextInt());
          break;
        case "location":
          res.setLocation(this.locationDTOAdapter.read(in));
          break;
        case "rawType":
          res.setRawType(in.nextString());
          break;
        case "modifiers":
          res.setModifiers(in.nextString());
          break;
        case "File":
          res.setFile(in.nextString());
          break;
        case "additionalBin":
          res.setAdditionalBin(this.additionalBinDTOAdapter.read(in));
          break;
        case "anonymousBindVar":
          res.setAnonymousBindVar(in.nextInt());
          break;
        case "anonymousRank":
          res.setAnonymousRank(in.nextInt());
          break;
      }
    }
    in.endObject();
    return res;
  }
}
