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

/**
 * Instances of this interface can be published as OSGi services, which will result
 * in any capable HTTP server registering the resources and publishing them.  This is an
 * implementation-neutral registration interface intended to be used with any of the
 * implementations of the OSGi HTTP Service or HTTP Whiteboard service.
 *
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IResourceRegistration {

	/**
	 * The context used to retrieve the resources
	 * 
	 * @return The context that can be used to authorize and retrieve the resources
	 */
	// TODO
	public Object getResources();
	
	/**
	 * Get the root context path for the resources
	 * 
	 * @return The root context for the resources.  This is only used to associate the resources
	 * with the appropriate web application.   
	 */
	public String getContext();
	
	/**
	 * Get the prefix, if any, that will be prepended to any resource requests for the context
	 * 
	 * @return The prefix to use.  This may be null.  This corresponds to the "name" parameter 
	 * used in the <code>HttpService.registerReources()</code> method.
	 */
	public String getPrefix();
	
	/**
	 * Get the path spec used to register the resources.
	 * 
	 * @return The pathSpec used to access the resources.  This is the same as a normal
	 * servlet pathSpec.  If this is null, the resources will be registered with the web
	 * application, and will be available through the ServletContext's getResource methods
	 * but will not have an externally accessible path.
	 */
	public String getPathSpec();

}
