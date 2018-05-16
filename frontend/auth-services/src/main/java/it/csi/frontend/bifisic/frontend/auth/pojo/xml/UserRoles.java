package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class UserRoles {
	@JacksonXmlProperty(localName="username",isAttribute=true)
	private String username;
	@JacksonXmlElementWrapper(localName="userRoles",useWrapping=false)
	private RoleRef[] roleRef;

	public RoleRef[] getRoleRef() {
		return roleRef;
	}

	public void setRoleRef(RoleRef[] roleRef) {
		this.roleRef = roleRef;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}
