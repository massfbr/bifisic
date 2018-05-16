package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class GroupList {
	@JacksonXmlElementWrapper(localName="groupRoles",useWrapping=false)
	private GroupRoles[] groupRoles;

	public GroupRoles[] getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(GroupRoles[] groupRoles) {
		this.groupRoles = groupRoles;
	}




	
}
