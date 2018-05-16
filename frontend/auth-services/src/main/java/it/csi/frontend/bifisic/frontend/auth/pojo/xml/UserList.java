package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class UserList {
	@JacksonXmlElementWrapper(localName="userRoles",useWrapping=false)
	private UserRoles[] userRoles;

	public UserRoles[] getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(UserRoles[] userRoles) {
		this.userRoles = userRoles;
	}


	
}
