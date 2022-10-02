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

import java.util.Map;

import javax.servlet.Filter;

/**
 * Instances of this interface can be published as OSGi services, which will result
 * in any capable HTTP server registering the filter and activating it.  This is an
 * implementation-neutral registration interface intended to be used with any of the
 * implementations of the OSGi HTTP Service or HTTP Whiteboard service.
 * <p>
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IFilterRegistration {
	
    public static final int DISPATCH_DEFAULT = 0;
    public static final int DISPATCH_REQUEST = 1;
    public static final int DISPATCH_FORWARD = 2;
    public static final int DISPATCH_INCLUDE = 4;
    public static final int DISPATCH_ERROR = 8;
    public static final int DISPATCH_ALL = DISPATCH_REQUEST + DISPATCH_FORWARD + DISPATCH_INCLUDE + DISPATCH_ERROR;
    
    /**
     * Get the dispatch flags that indicate at what point the filter should be inviked
     * 
     * @return An or'ed combination of the enumerated dispatch flags, DISPATCH_REQUEST, 
     * DISPATCH_FORWARD, DISPATCH_INCLUDE, DISPATCH_ERROR or the convenience definitions 
     * DISPATCH_DEFAULT or DISPATCH_ALL.
     */
    public int getDispatch();

	/**
	 * Get the name of this Filter.  
	 * 
	 * @return The filter name that will be used by the J2EE container  
	 */
	public String getName();
	
	/**
	 * Get the filter
	 * 
	 * @return The actual Filter instance that will be deployed.  The Filter's
	 * <code>init(FilterConfig)</code> method will be invoked when the filter is
	 * initialized within the J2EE container. 
	 */
	public Filter getFilter();
	
    /**
     * Get the filter's init params
     * 
     * @return The init params for the filter.  These will be passed to the Filter's
     * init method when the filter is initialized in the container.
     */
    public Map<String,String> getInitParameters();
    
    /**
     * Get the root context that this filter belongs to.  This allows the framework to associate
     * the filter with a web application in scenarios where there may be multiple web applications
     * deployed.
     * 
     * @return The string root context path for the web application that this filter belongs
     * to.  This may be null, in which case it is assumed to be "/".  
     */
    public String getContext();
    
    /**
     * Get the path name mapping for the filter
     * 
     * @return A path name spec for this filter.  This may be null, in which case the 
     * assigned servlet mapping will be used.  If both are null, this filter will be applied
     * to all requests for the context. 
     */
    public String getPathSpec();
    
    /**
     * Get the servlet mapping for the filter
     * 
     * @return A servlet name for this filter.  The filter will be applied to all requests
     * directed to the named servlet, if it exists.  If this is null and the path spec is null,
     * the filter will be applied to all requests for the context.
     */
    public String getServlet();

}
