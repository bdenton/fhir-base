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
import java.util.Collections;
import java.util.Map;

import javax.servlet.Servlet;

/**
 * A simple implementation of {@code IServletRegistration} that can be 
 * used with an IoC framework.
 *
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public class SimpleServletRegistration implements IServletRegistration {
	public static final String DEFAULT_URL_PATTERN = "/*";
	
	private String name;
	private Servlet servlet;
	private Map<String,String> initParameters;
	private Map<String,Object> contextAttributes;
	private Collection<String> urlPatterns;
	private String contextRoot;
	private boolean initOnStartup;
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Servlet getServlet() {
		return servlet;
	}
	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}
	
	@Override
	public Map<String, String> getInitParameters() {
		if (null == initParameters) {
			initParameters = Collections.emptyMap();
		}
		return initParameters;
	}
	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}
	
	@Override
	public Map<String, Object> getContextAttributes() {
		if (null == contextAttributes) {
			contextAttributes = Collections.emptyMap();
		}
		return contextAttributes;
	}
	public void setContextAttributes(Map<String, Object> contextAttributes) {
		this.contextAttributes = contextAttributes;
	}
	
	@Override
	public Collection<String> getUrlPatterns() {
		if (null == urlPatterns) {
			urlPatterns = Collections.singleton(DEFAULT_URL_PATTERN);
		}
		return urlPatterns;
	}
	public void setUrlPatterns(Collection<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}
	
	@Override
	public String getContextRoot() {
		if (null == contextRoot) {
			contextRoot = SimpleWebAppRegistration.DEFAULT_CONTEXT_ROOT; 
		}
		return contextRoot;
	}
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}
	
	@Override
	public boolean isInitOnStartup() {
		return initOnStartup;
	}
	public void setInitOnStartup(boolean initOnStartup) {
		this.initOnStartup = initOnStartup;
	}
}
