package entity.dto;

import java.util.Objects;

public class LocationDTO {

  private int startLine;
  private int endLine;
  private int startColumn;
  private int endColumn;

  public LocationDTO() {}

  public LocationDTO(int startLine, int endLine, int startColumn, int endColumn) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.startColumn = startColumn;
    this.endColumn = endColumn;
  }

  public int getStartLine() {
    return startLine;
  }

  public void setStartLine(int startLine) {
    this.startLine = startLine;
  }

  public int getEndLine() {
    return endLine;
  }

  public void setEndLine(int endLine) {
    this.endLine = endLine;
  }

  public int getStartColumn() {
    return startColumn;
  }

  public void setStartColumn(int startColumn) {
    this.startColumn = startColumn;
  }

  public int getEndColumn() {
    return endColumn;
  }

  public void setEndColumn(int endColumn) {
    this.endColumn = endColumn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocationDTO that = (LocationDTO) o;
    return startLine == that.startLine
        && endLine == that.endLine
        && startColumn == that.startColumn
        && endColumn == that.endColumn;
  }

  @Override
  public int hashCode() {
    return Objects.hash(startLine, endLine, startColumn, endColumn);
  }

  @Override
  public String toString() {
    return "LocationDTO{"
        + "startLine="
        + startLine
        + ", endLine="
        + endLine
        + ", startColumn="
        + startColumn
        + ", endColumn="
        + endColumn
        + '}';
  }
}
