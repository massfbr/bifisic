package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RoleRef {
	@JacksonXmlProperty(localName="roleID",isAttribute=true)
	private String roleID;

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

}
