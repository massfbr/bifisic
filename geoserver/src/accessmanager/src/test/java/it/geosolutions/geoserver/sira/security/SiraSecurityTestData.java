/*
 *  CSI SIRA - Access Manager Security Module ("Rules Engine"), a GeoServer Secure Catalog Resource Access Manager plugin with which specify advanced rules evaluated to decide what the specified user can access.
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
package it.geosolutions.geoserver.sira.security;

import org.geoserver.test.AbstractAppSchemaMockData;

/**
 *
 * @author Stefano Costa, GeoSolutions
 * @author "Simone Cornacchia - seancrow76@gmail.com, simone.cornacchia@consulenti.csi.it (CSI:71740)"
 */
public class SiraSecurityTestData extends AbstractAppSchemaMockData {

    static final String AUA_FEATURE_TYPE = "AutorizzazioneUnicaAmbientale";
    static final String ISTANZA_FEATURE_TYPE = "IstanzaAutorizzativa";

    /**
     * Prefix for sira namespace.
     */
    protected static final String SIRA_PREFIX = "sira";

    /**
     * URI for sira namespace.
     */
    protected static final String SIRA_URI = "http://www.regione.piemonte.it/ambiente/sira/1.0";

    /**
     * Constructor.
     */
    public SiraSecurityTestData() {
        this.setSchemaCatalog("sira-catalog.xml");
    }

    /*
     *
     * (non-Javadoc)
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    protected void addContent() {
        this.putNamespace(SIRA_PREFIX, SIRA_URI);
        this.addFeatureType(SIRA_PREFIX, AUA_FEATURE_TYPE, "AUA.xml", "aua.properties", "sira-catalog.xml");
        this.addFeatureType(SIRA_PREFIX, ISTANZA_FEATURE_TYPE, "Istanza.xml", "istanza.properties", "sira-catalog.xml");
    }

}
