package entity.dto;

public class CellDTO {

  private int src;
  private int dest;
  private ValuesDTO values;

  public CellDTO() {}

  public CellDTO(int src, int dest, ValuesDTO values) {
    this.src = src;
    this.dest = dest;
    this.values = values;
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

  public ValuesDTO getValues() {
    return values;
  }

  public void setValues(ValuesDTO values) {
    this.values = values;
  }
}
