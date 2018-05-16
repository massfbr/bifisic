package it.csi.frontend.bifisic.frontend.auth.pojo;

import java.util.ArrayList;
import java.util.List;

public class UserPojo {
	protected String name;
	protected List<String> roles = new ArrayList<String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
