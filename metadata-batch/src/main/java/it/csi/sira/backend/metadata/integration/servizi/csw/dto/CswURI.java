package it.csi.sira.backend.metadata.integration.servizi.csw.dto;

public class CswURI {
  private int tipo;
  private String protocol;
  private String url;
  private int statusCode;
  private String statusMessage;

  public String getProtocol() {
	return protocol;
  }

  public void setProtocol(String protocol) {
	this.protocol = protocol;
  }

  public String getUrl() {
	return url;
  }

  public void setUrl(String url) {
	this.url = url;
  }

  public int getTipo() {
    return tipo;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }

public int getStatusCode() {
	return statusCode;
}

public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}

public String getStatusMessage() {
	return statusMessage;
}

public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}
  
}
