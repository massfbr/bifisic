/*
 *  REST service to query for IRIDE roles using CSI-Piemonte IRIDE Service.
 *  Copyright (C) 2016  Regione Piemonte (www.regione.piemonte.it)
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


/**
 */
public final class HttpBasicAuthServiceConstants {

	/**
     * Application name requesting <code>HTTPBASICAUTH</code> service.
     */
    public static final String LOGGER = "httpbasic";
    
    /**
     * Application name requesting <code>HTTPBASICAUTH</code> service.
     */
    public static final String APPLICATION_NAME = "BIFISIC";

    /**
     */
    public static final String MAPPING_HTTPBASICAUTH_SERVICE = "/httpbasicauth";

    /**
     */
    public static final String MAPPING_ROLES_FOR_IDENTITY = "/getRoles";

    /**
     */
    public static final String MAPPING_ROLES_FOR_DIGITAL_IDENTITY = "/getRolesForDigitalIdentity";

    /**
     */
    public static final String HEADER_SHIBBOLETH_HTTPBASICAUTH = "Shib-Iride-IdentitaDigitale";

    /**
     * Constructor.
     */
    private HttpBasicAuthServiceConstants() {
        /* NOP */
    }

}
