package it.csi.frontend.bifisic.frontend.auth.pojo.geoserver;


public class GroupRole {
	protected String groupname;
	protected RoleRef[] roleRef;
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

