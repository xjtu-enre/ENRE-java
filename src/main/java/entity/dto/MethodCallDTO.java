package entity.dto;

public class MethodCallDTO {

  private int src;
  private int dest;
  private String type;
  private LocationDTO location;

  public MethodCallDTO() {}

  public MethodCallDTO(int src, int dest, String type, LocationDTO location) {
    this.src = src;
    this.dest = dest;
    this.type = type;
    this.location = location;
  }

  public int getSrc() {
    return src;
  }

  public void setSrc(int src) {
    this.src = src;
  }

  public int getDest() {
    return dest;
  }

  public void setDest(int dest) {
    this.dest = dest;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LocationDTO getLocation() {
    return location;
  }

  public void setLocation(LocationDTO location) {
    this.location = location;
  }
}
