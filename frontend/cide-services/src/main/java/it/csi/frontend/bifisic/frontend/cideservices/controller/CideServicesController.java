/*
 *  REST service to CRUD services for cide data base.
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
package it.csi.frontend.bifisic.frontend.cideservices.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(method = RequestMethod.GET, value = CideServicesConstants.MAPPING_CIDE_SERVICE)
public final class CideServicesController {
	
	private static String className = CideServicesController.class.getSimpleName();

    @RequestMapping(value = "/isAlive", method = RequestMethod.GET)
    public @ResponseBody boolean isAlive() {
    	
    	final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
    	Logger.getLogger(CideServicesConstants.LOGGER).info(it.csi.frontend.bifisic.frontend.cideservices.utils.LogFormatter.format(className, methodName, "BEGIN"));       
    	return true;
    }
    
}
