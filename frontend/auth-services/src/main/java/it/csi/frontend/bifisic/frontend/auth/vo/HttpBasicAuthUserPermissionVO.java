package it.csi.frontend.bifisic.frontend.auth.vo;

import java.io.Serializable;

public class HttpBasicAuthUserPermissionVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * <code>IRIDE</code> Roles code.
     */
    private HttpBasicRoleVO[] roles;
    
    /**
     * <code>IRIDE</code> userIdentity.
     */
    private HttpBasicIdentityVO userIdentity;

	public HttpBasicRoleVO[] getRoles() {
		return roles;
	}

	public void setRoles(HttpBasicRoleVO[] roles) {
		this.roles = roles;
	}

	public void setUserIdentity(HttpBasicIdentityVO userIdentity) {
		this.userIdentity = userIdentity;
	}

	public HttpBasicIdentityVO getUserIdentity() {
		return userIdentity;
	}
    
}
