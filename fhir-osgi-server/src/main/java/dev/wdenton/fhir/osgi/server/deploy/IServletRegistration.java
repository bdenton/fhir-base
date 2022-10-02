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

import javax.servlet.Servlet;

/**
 * Instances of this interface can be published as OSGi services, which will result
 * in any capable HTTP server registering the servlet and publishing it.  This is an
 * implementation-neutral registration interface intended to be used with any of the
 * implementations of the OSGi HTTP Service or HTTP Whiteboard service.
 * <p>
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IServletRegistration {

    /**
     * The servlet name.  This required, may not be null, and must be unique within
     * the web application (containing {@code IWebAppRegistration} bean or via defined
     * "context root" for standalone OSGi services published with this interface..
     * 
     * @return The servlet name. This must be a non-null, not-empty string that is a 
     * unique identifier for this servlet in the web application it belongs to.
     */
    public String getName ();

    /**
     * Get the actual servlet instance to register with the server
     *
     * @return The servlet instance. The servlet will be initialized through its
     * {@code init(ServletContext)} method when it is first retrieved.
     */
    public Servlet getServlet ();

    /**
     * Get the init parameters for the servlet.  In many case, the init parameters will be 
     * redundant because the servlet instance may alredy have been initialized in Spring or 
     * through some other mechanism.  In some case though, legacy servlets require the normal
     * servlet lifecycle with a callback to {@code init()}.
     *  
     * @return The (possibly empty) {@code Map} with the init params for the servlet.
     */
    public Map<String,String> getInitParameters ();
    
    /**
     * Get any context attributes that should be added to the servlet context
     * 
     * @return A (possibly empty) {@code Map} with the  of servlet context attributes
     */
    public Map<String,Object> getContextAttributes ();

    /**
     * This is the URL patterns defined by the servlet specification. This is equivalent
     * to the &lt;servlet-mapping&gt; elements in JEE {@code web.xml} deployment descriptors.  
     * There are three possible forms for this value:
     * <ul>
     * <li>*.&lt;suffix&gt;  This is used to map a file/resource suffix to this servlet.</li>
     * <li>&lt;literal&gt;/*  Maps a literal string and all sub-paths to this servlet</li>
     * <li>&lt;literal&gt; Map a literal path exactly to the servlet</li>
     * </ul>
     * Note that this value is relative to the context-root returned from <code>getContextRoot()</code>, it
     * is not an absolute path.
     * 
     * @return Returns a {@code Collection} of URL patterns to be mapped to this servlet..
     */
    public Collection<String> getUrlPatterns ();
    
    /**
     * Get the root context that this servlet belongs to. This allows the framework to associate
     * the servlet with a web application in scenarios where there may be multiple web applications
     * deployed.  If this instance is used as part of a {@code IWebAppRegistration} service, 
     * this value is ignored.
     * 
     * @return The string root context path for the web application that this servlet belongs
     * to.  If not specified, then a value of "/" will be returned.  
     */
    public String getContextRoot ();
    
    /**
     * Should the servlet be initialized at deployment time?  
     * 
     * @return Returns true if the servlet should be initialized when deployed 
     */
    public boolean isInitOnStartup ();

}
