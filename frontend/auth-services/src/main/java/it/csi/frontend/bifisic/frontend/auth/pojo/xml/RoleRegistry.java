package it.csi.frontend.bifisic.frontend.auth.pojo.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="roleRegistry")
public class RoleRegistry {
	@JacksonXmlProperty(localName="xmlns",isAttribute=true)
	private String xmlns;
	@JacksonXmlProperty(localName="version",isAttribute=true)
	private String version;

	@JacksonXmlElementWrapper(localName="roleList",useWrapping=false)
	private RoleList roleList;
	@JacksonXmlElementWrapper(localName="userList",useWrapping=false)
	private UserList userList;
	@JacksonXmlElementWrapper(localName="groupList",useWrapping=false)
	private GroupList groupList;
	public RoleList getRoleList() {
		return roleList;
	}
	public void setRoleList(RoleList roleList) {
		this.roleList = roleList;
	}
	public UserList getUserList() {
		return userList;
	}
	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	public GroupList getGroupList() {
		return groupList;
	}
	public void setGroupList(GroupList groupList) {
		this.groupList = groupList;
	}
	
	
}
