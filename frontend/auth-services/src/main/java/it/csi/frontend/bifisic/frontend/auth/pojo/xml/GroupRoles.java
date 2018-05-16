package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GroupRoles {
	@JacksonXmlProperty(localName="groupname",isAttribute=true)
	private String groupname;
	@JacksonXmlElementWrapper(localName="groupRoles",useWrapping=false)
	private RoleRef[] roleRef;
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public RoleRef[] getRoleRef() {
		return roleRef;
	}
	public void setRoleRef(RoleRef[] roleRef) {
		this.roleRef = roleRef;
	}

	
}
