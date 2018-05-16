package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Role {
	@JacksonXmlProperty(localName="id",isAttribute=true)
	private String id;
	@JacksonXmlProperty(localName="parentID",isAttribute=true)
	private String parentID
	;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
}
