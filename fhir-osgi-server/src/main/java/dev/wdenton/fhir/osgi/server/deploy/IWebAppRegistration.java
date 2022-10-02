/*
 * #%L
 * FHIR-Base - OSGi Server Framework Bundle
 * %%
 * Copyright (C) 2019 - 2022 William E. Denton
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package dev.wdenton.fhir.osgi.server.deploy;

import java.util.Collection;
import java.util.Map;

/**
 * This interface is used to publish a set of Web resources as a single web application
 * including all servlets, filters, and static resources (possibly event listeners 
 * in the future).  This is a convenience that can help minimize the number of OSGi services, 
 * and the deployment time, for large web applications.  This interface also accommodates 
 * a single root context path that applies to all of the deployed artifacts.
 * <p>
 * Currently this is the only mechanism available to publish the context attributes for
 * a web application context.  The may be used in conjunction with other {@code IServletRegistration},
 * {@code IFilterRegistration}, etc. instances that will be consolidated by the HTTP service that
 * consumes these registrations based on the context root.
 * <p>
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IWebAppRegistration {
	
	/**
	 * Get the root context for the application.  This provides the root path for
	 * all artifacts associated with the application.
	 * 
	 * @return The root context path for the application. If not specified in the
	 * Spring/Blueprint wiring, then a value of "/" will be returned.
	 */
	public String getContextRoot ();
	
	/**
	 * Get the Servlet Context init params.
	 * 
	 * @return A possibly empty {@code Map} containing the context init params
	 */
	public Map<String,String> getContextInitParameters ();
	
	/**
	 * Get the initial set of Servlet Context attributes.
	 * The actual attributes available via {@code ServletContext}
	 * will consist of these attributes along with any defined
	 * in {@code IServletRegistration} members of this application.
	 * 
	 * @return A possibly empty {@code Map} containing the context attributes
	 */
	public Map<String,Object> getContextAttributes ();
	
	/**
	 * Get the servlets for this web application
	 * 
	 * @return A possibly empty {@code Collection} of {@code IServletRegistration} 
	 * instances associated with this application
	 * 
	 * @see IServletRegistration
	 */
	public Collection<IServletRegistration> getServlets ();
	
	/**
	 * Get the servlet filters for this web application
	 * 
	 * @return A possibly empty {@code Collection} of {@code IFilterRegistration} 
	 * instances associated with this application
	 * 
	 * @see IFilterRegistration
	 */
	public Collection<IFilterRegistration> getFilters ();
	
	/**
	 * Get the resources for this web applicatrion
	 * 
	 * @return A possibly empty {@code Collection} of {@code IResourceRegistration} 
	 * instances associated with this application
	 * 
	 * @see IResourceRegistration
	 */
	public Collection<IResourceRegistration> getResources ();

}
