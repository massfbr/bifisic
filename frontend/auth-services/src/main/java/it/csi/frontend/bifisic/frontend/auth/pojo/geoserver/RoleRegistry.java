package it.csi.frontend.bifisic.frontend.auth.pojo.geoserver;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRegistry {
	protected List<Role> roleList;
	protected List<UserRole> userList;
	protected List<GroupRole> groupList;

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<UserRole> getUserList() {
		return userList;
	}

	public void setUserList(List<UserRole> userList) {
		this.userList = userList;
	}

	public List<GroupRole> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupRole> groupList) {
		this.groupList = groupList;
	}
	
}
