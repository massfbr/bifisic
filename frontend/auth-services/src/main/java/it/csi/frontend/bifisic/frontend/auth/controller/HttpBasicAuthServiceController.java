/*
 *  REST service to query for Http Basic Auth roles using OGC GeoServer Service.
 *  Copyright (C) 2018  Regione Piemonte (www.regione.piemonte.it)
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package it.csi.frontend.bifisic.frontend.auth.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;

import it.csi.frontend.bifisic.frontend.auth.pojo.UserPojo;
import it.csi.frontend.bifisic.frontend.auth.pojo.xml.RoleRef;
import it.csi.frontend.bifisic.frontend.auth.pojo.xml.RoleRegistry;
import it.csi.frontend.bifisic.frontend.auth.pojo.xml.UserRoles;
import it.csi.frontend.bifisic.frontend.auth.utils.LogFormatter;
import it.csi.frontend.bifisic.frontend.auth.vo.HttpBasicAuthUserPermissionVO;
import it.csi.frontend.bifisic.frontend.auth.vo.HttpBasicRole;
import it.csi.frontend.bifisic.frontend.auth.vo.HttpBasicService;
import java.util.Arrays;
import java.util.HashMap;

/**
 */
@Controller
@RequestMapping(method = RequestMethod.GET, value = HttpBasicAuthServiceConstants.MAPPING_HTTPBASICAUTH_SERVICE)
public final class HttpBasicAuthServiceController {
	
	private static String className = HttpBasicAuthServiceController.class.getSimpleName();

    /**
     * "No roles" empty {@link IrideRole} array.
     */
    private static final HttpBasicRole[] NO_ROLES = new HttpBasicRole[0];

    /**
     * <code>IRIDE</code> service "policies" enforcer instance.
     */
    private HttpBasicService httpBasicService;
    
    /**
     */
    private Properties httpBasicServiceProperties = null;
    private RestTemplate restTemplate = null;
    
	public Properties getHttpBasicServiceProperties() {
		return httpBasicServiceProperties;
	}
	@Autowired
	public void setHttpBasicServiceProperties(Properties httpBasicServiceProperties) {
		this.httpBasicServiceProperties = httpBasicServiceProperties;
	}

    /**
     * Get the <code>IRIDE</code> service "policies" enforcer instance.
     *
     * @return the <code>IRIDE</code> service "policies" enforcer instance
     */
    public HttpBasicService getHttpBasicService() {
        return this.httpBasicService;
    }

    /**
     * Set the <code>IRIDE</code> service "policies" enforcer instance.
     *
     * @param irideService the <code>IRIDE</code> service "policies" enforcer instance
     */
    @Autowired
    public void setHttpBasicService(HttpBasicService httpBasicService) {
        this.httpBasicService = httpBasicService;
    }

     /**
     * Executed after dependencies have been injected.
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
    	
    	final String methodName = new Object() {
    	}.getClass().getEnclosingMethod().getName();
    	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN"));
        
    	restTemplate = new RestTemplate();
       	this.getHttpBasicService().initializeFromConfig(httpBasicServiceProperties.getProperty("serverURL"));
    }
    
    @RequestMapping(value = "/testResources", method = RequestMethod.GET)
    public @ResponseBody boolean testResources() {
    	
    	final String methodName = new Object() {
    	}.getClass().getEnclosingMethod().getName();
    	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN"));
       
    	return true;
    }

    /**
    *
    * @param user
    * @param pass
    * @return
    */
   @RequestMapping(
       value = HttpBasicAuthServiceConstants.MAPPING_ROLES_FOR_IDENTITY,
       produces = "application/json"
   )
   @ResponseBody
   public ResponseEntity<HttpBasicAuthUserPermissionVO> getRolesForIdentity(
       String user, String pass) {
   	final String methodName = new Object() {
   	}.getClass().getEnclosingMethod().getName();
   	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN"));
   	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "user: "+user+" pass: "+pass));
      
       
       try {
       	HttpBasicAuthUserPermissionVO userP = this.getUserPermission(user);
           
           Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "Got {} role(s) for HTTPBASICAUTH Digital Identity {} "+userP.getRoles().length));
                         
           return new ResponseEntity<>(userP, HttpStatus.OK);
       } catch (Exception e) {
           Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).error(LogFormatter.format(className, methodName, "IRIDE roles retrieval for Digital Identity "),e);
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   /**
     *
     * @param user
     * @return
     */
    @RequestMapping(
        headers = { HttpBasicAuthServiceConstants.HEADER_SHIBBOLETH_HTTPBASICAUTH },
        value = HttpBasicAuthServiceConstants.MAPPING_ROLES_FOR_DIGITAL_IDENTITY,
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<HttpBasicAuthUserPermissionVO> getRolesForDigitalIdentity(
        @RequestHeader(
            value = HttpBasicAuthServiceConstants.HEADER_SHIBBOLETH_HTTPBASICAUTH,
            defaultValue = ""
        ) String user) {
    	final String methodName = new Object() {
    	}.getClass().getEnclosingMethod().getName();
    	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN"));
    	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "user: "+user));
       
        
        try {
        	HttpBasicAuthUserPermissionVO userP = this.getUserPermission(user);
            
            Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "Got {} role(s) for HTTPBASICAUTH Digital Identity {} "+userP.getRoles().length));
                          
            return new ResponseEntity<>(userP, HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).error(LogFormatter.format(className, methodName, "IRIDE roles retrieval for Digital Identity "),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
   @RequestMapping(
		   value = HttpBasicAuthServiceConstants.MAPPING_ROLES_FOR_DIGITAL_IDENTITY, 
		   method = RequestMethod.GET,
		   produces = "application/json"
		   )
   public @ResponseBody ResponseEntity<HttpBasicAuthUserPermissionVO> getRolesForDigitalIdentity() {
   	final String methodName = new Object() {
   	}.getClass().getEnclosingMethod().getName();
   	Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN NOT AUTHENTICATED... fake"));
   	   
       try {
    	   HttpBasicAuthUserPermissionVO userP = new HttpBasicAuthUserPermissionVO();
    	                       
           return new ResponseEntity<>(userP, HttpStatus.OK);
       } catch (Exception e) {
           Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).error(LogFormatter.format(className, methodName, "IRIDE roles retrieval for Digital Identity "),e);
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
   
   
   /**
   *
   * @param user
   * @return
   */
  private HttpBasicAuthUserPermissionVO getUserPermission(String user) {
	  HttpBasicRole[] roles = NO_ROLES;
      HttpBasicAuthUserPermissionVO userP = new HttpBasicAuthUserPermissionVO();
      final String methodName = new Object() {
	  	}.getClass().getEnclosingMethod().getName();
	  Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "BEGIN"));
  	
/*
      final IrideIdentity irideIdentity = IrideIdentity.parseIrideIdentity(user);
      Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "irideIdentity: "+irideIdentity));
      if(irideIdentity != null)
        Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).info(LogFormatter.format(className, methodName, "getCodFiscale: "+irideIdentity.getCodFiscale()));
      
      if (irideIdentity != null) {
    	  userP.setUserIdentity(CastUtils.getIrideIdentityVOFromIrideIdentity(irideIdentity));
          
    	  roles = this.getIrideService().findRuoliForPersonaInApplication(
              irideIdentity,
              new IrideApplication(HttpBasicAuthServiceConstants.APPLICATION_NAME)
          );
    	  
    	  userP.setRoles(CastUtils.getRolesVOFromRole(roles));
      }
*/
      return userP;
  }

    /**
     *
     * @param user
     * @return
     */
    private HttpBasicRole[] getRolesForUser(String user) {
    	HttpBasicRole[] roles = NO_ROLES;
/*
        final IrideIdentity irideIdentity = IrideIdentity.parseIrideIdentity(user);
        if (irideIdentity != null) {
            roles = this.getIrideService().findRuoliForPersonaInApplication(
                irideIdentity,
                new IrideApplication(HttpBasicAuthServiceConstants.APPLICATION_NAME)
            );
        }
*/
        return roles;
    }

    HttpHeaders createHeaders(final String username, final String password){
    	   return new HttpHeaders() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
    	         String auth = username + ":" + password;
    	         byte[] encodedAuth = Base64.encodeBase64( 
    	            auth.getBytes(Charset.forName("US-ASCII")) );
    	         String authHeader = "Basic " + new String( encodedAuth );
    	         set( "Authorization", authHeader );
    	      }};
    	}

	@RequestMapping(value="/auth", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> authenticateRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user, String password) throws URISyntaxException
	{
		RoleRegistry roleRegistry = null;
		UserPojo userPojo = new UserPojo();
				
		String rolesUrl = getHttpBasicServiceProperties().getProperty("rolesURL");
		Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).debug(LogFormatter.format(className, "authenticateRest", "calling "+rolesUrl+" user:admin password: ---"));
	    URI rolesUri = new URI(rolesUrl);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity =
				restTemplate.exchange(rolesUri, HttpMethod.GET, new HttpEntity<Object>(createHeaders(geoserverAdminUser, geoserverAdminPassword)), String.class);
			if (responseEntity.getStatusCode()==HttpStatus.OK) {
				try {
					XmlMapper xmlMapper = new XmlMapper();
					roleRegistry = xmlMapper.readValue(responseEntity.getBody().getBytes(), RoleRegistry.class);
					
				} catch (JsonProcessingException e) {
			           Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).error(LogFormatter.format(className, "authenticateRest", "JsonProcessingException " + responseEntity.getBody()),e);
				} catch (IOException e) {
			           Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).error(LogFormatter.format(className, "authenticateRest", "IOException " + responseEntity.getBody()),e);
				}
			}
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	    String layerUrl = getHttpBasicServiceProperties().getProperty("layerURL");
	    Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).debug(LogFormatter.format(className, "authenticateRest", "calling "+layerUrl+" user:"+user+" password:"+password));
	    
	    URI layerUri = new URI(layerUrl);
        ResponseEntity<String> responseEntityLayer=null;
		try {
			responseEntityLayer = restTemplate.exchange(layerUri, HttpMethod.GET, new HttpEntity<Object>(createHeaders(user, password)), String.class);
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg,responseEntityLayer!=null?responseEntityLayer.getStatusCode():HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (roleRegistry!=null) {
			for (UserRoles userRole : roleRegistry.getUserList().getUserRoles()) {
				System.out.println(LogFormatter.format(className, "authenticateRest", "comparing "+userRole.getUsername()+" to:"+user));
			    Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).debug(LogFormatter.format(className, "authenticateRest", "comparing "+userRole.getUsername()+" to:"+user));
				if (userRole.getUsername().equalsIgnoreCase(user)) {
					System.out.println(LogFormatter.format(className, "authenticateRest", "found "+userRole.getUsername()+" with "+(userRole.getRoleRef()!=null?userRole.getRoleRef().length:"null")+" roles"));
				    Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).debug(LogFormatter.format(className, "authenticateRest", "found "+userRole.getUsername()));
					userPojo.setName(userRole.getUsername());
					for (RoleRef roleRef : userRole.getRoleRef()) {
					    System.out.println(LogFormatter.format(className, "authenticateRest", "adding "+roleRef.getRoleID()));
					    Logger.getLogger(HttpBasicAuthServiceConstants.LOGGER).debug(LogFormatter.format(className, "authenticateRest", "adding "+roleRef.getRoleID()));
						userPojo.getRoles().add(roleRef.getRoleID());
					}
				}
			}
		}
		
        return new ResponseEntity<Object>(userPojo,responseEntityLayer.getStatusCode());

	}

	@RequestMapping(value="/adduser", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> adduserRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user, String password) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("adduserURL");
		System.out.println(LogFormatter.format(className, "adduserRest", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<String> responseEntity = null;
		HttpHeaders headers = null;
		try {
			headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);
			//headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN }));
			headers.setContentType(MediaType.APPLICATION_XML);

	        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><org.geoserver.rest.security.xml.JaxbUser><userName>" + user +
	        		"</userName><password>" + password +
	        		"</password><enabled>true</enabled></org.geoserver.rest.security.xml.JaxbUser>";
	        HttpEntity<String> requestEntity = new HttpEntity<String>(xmlString, headers);

	        //responseEntity = restTemplate.postForEntity(uri, requestEntity, String.class);
	        //responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);	
	        responseEntity = restTemplate.postForEntity(uri, requestEntity, null);	
		System.out.println(LogFormatter.format(className, "adduserRest", "status code: "+responseEntity.getStatusCode()));
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (ResourceAccessException e) {
			e.printStackTrace();
			try {
				ResponseEntity<Object> responseEntityRoles = listrolesRest(method, request, response, user);
				if (responseEntityRoles.getStatusCode()==HttpStatus.OK) {
			        return new ResponseEntity<Object>("user yet present",HttpStatus.FOUND);
				}
		        return new ResponseEntity<Object>("message: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (RestClientException ex) {
				ex.printStackTrace();
		        return new ResponseEntity<Object>("message: "+ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (RestClientException e) {
			e.printStackTrace();
	        return new ResponseEntity<Object>("message: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}

	@RequestMapping(value="/edituser", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> edituserRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user, String password) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("edituserURL") + "/" + user + "/";
		System.out.println(LogFormatter.format(className, "edituserRest", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<String> responseEntity = null;
		HttpHeaders headers = null;
		try {
			headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN }));
			headers.setContentType(MediaType.APPLICATION_XML);
	        String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><org.geoserver.rest.security.xml.JaxbUser><userName>" + user +
	        		"</userName><password>" + password +
	        		"</password><enabled>true</enabled></org.geoserver.rest.security.xml.JaxbUser>";
	        HttpEntity<String> requestEntity = new HttpEntity<String>(xmlString, headers);

	        //responseEntity = restTemplate.postForEntity(uri, requestEntity, String.class);
	        //responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Void.class);	
	        responseEntity = restTemplate.postForEntity(uri, requestEntity, null);	
		System.out.println(LogFormatter.format(className, "adduserRest", "status code: "+responseEntity.getStatusCode()));
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (ResourceAccessException e) {
			e.printStackTrace();
	        return new ResponseEntity<Object>("user not found: "+user,HttpStatus.NOT_FOUND);
		} catch (RestClientException e) {
			e.printStackTrace();
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}

	@RequestMapping(value="/deleteuser", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> deleteuserRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("deleteuserURL") + "/" + user + "/";
		System.out.println(LogFormatter.format(className, "deleteuserRest", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<Void> responseEntity = null;
		try {
			HttpHeaders headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);			
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN }));
			restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Object>(headers), Void.class);
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			e.printStackTrace();
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}

	@RequestMapping(value="/addrole", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> addroleRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user, String role) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("addroleURL") + "/" + role + "/user/" + user + "/";
		System.out.println(LogFormatter.format(className, "addroleRest", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<String> responseEntity = null;
		try {
			HttpHeaders headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);			
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN }));
			//responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<Object>(headers), String.class);
		    responseEntity = restTemplate.postForEntity(uri, new HttpEntity<Object>(headers), null);	
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			e.printStackTrace();
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}
	
	@RequestMapping(value="/deleterole", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> deleteroleRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user, String role) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("deleteroleURL") + "/" + role + "/user/" + user + "/";
		System.out.println(LogFormatter.format(className, "deleteroleURL", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<Void> responseEntity = null;
		try {
			HttpHeaders headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);			
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN }));
			responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<Object>(headers), Void.class);
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			e.printStackTrace();
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}
	
	@RequestMapping(value="/listroles", produces="application/json")
	@ResponseBody
	public ResponseEntity<Object> listrolesRest(HttpMethod method, HttpServletRequest request,
	    HttpServletResponse response, String user) throws URISyntaxException
	{
		String url = getHttpBasicServiceProperties().getProperty("listrolesURL") + "/" + user + "/";
		System.out.println(LogFormatter.format(className, "listrolesRest", "calling "+url+" user:admin password: ---"));
	    URI uri = new URI(url);

		String geoserverAdminUser = getHttpBasicServiceProperties().getProperty("geoserverAdminUser");
		String geoserverAdminPassword = getHttpBasicServiceProperties().getProperty("geoserverAdminPassword");
        
		ResponseEntity<String> responseEntity = null;
		try {
			HttpHeaders headers = createHeaders(geoserverAdminUser, geoserverAdminPassword);			
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_XML }));
			responseEntity =
					restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
		} catch (HttpStatusCodeException e) {
			String msg = e.getMessage();
		    int statusCode = e.getStatusCode().value();
	        return new ResponseEntity<Object>(msg,HttpStatus.valueOf(statusCode));
		} catch (RestClientException e) {
			e.printStackTrace();
			String msg = e.getMessage();
	        return new ResponseEntity<Object>(msg + "\nmessage: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

        return new ResponseEntity<Object>(responseEntity, responseEntity.getStatusCode());

	}
}
