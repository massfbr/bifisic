package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class RoleList {
	@JacksonXmlElementWrapper(localName="role",useWrapping=false)
	private Role[] role;

	public Role[] getRole() {
		return role;
	}

	public void setRole(Role[] role) {
		this.role = role;
	}
	
}
