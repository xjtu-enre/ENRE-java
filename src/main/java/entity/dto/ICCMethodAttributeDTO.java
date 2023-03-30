package entity.dto;

public class ICCMethodAttributeDTO {

  private Boolean send;
  private Boolean receive;
  private String mechanismType;

  public ICCMethodAttributeDTO() {}

  public ICCMethodAttributeDTO(Boolean send, Boolean receive, String mechanismType) {
    this.send = send;
    this.receive = receive;
    this.mechanismType = mechanismType;
  }

  public Boolean getSend() {
    return send;
  }

  public void setSend(Boolean send) {
    this.send = send;
  }

  public Boolean getReceive() {
    return receive;
  }

  public void setReceive(Boolean receive) {
    this.receive = receive;
  }

  public String getMechanismType() {
    return mechanismType;
  }

  public void setMechanismType(String mechanismType) {
    this.mechanismType = mechanismType;
  }
}
