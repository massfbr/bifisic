package it.csi.frontend.bifisic.frontend.auth.pojo.geoserver;


public class UserRole {
	protected String username;
	protected RoleRef[] roleRef;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public RoleRef[] getRoleRef() {
		return roleRef;
	}
	public void setRoleRef(RoleRef[] roleRef) {
		this.roleRef = roleRef;
	}
	
}
