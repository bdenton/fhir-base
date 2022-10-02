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

import java.util.Collections;
import java.util.Collection;
import java.util.Map;

/**
 * A simple implementation of {@code IWebAppRegistration} that can be 
 * used with an IoC framework.
 *
 * Copyright (C) 2019 - 2022 WWilliam E. Denton
 * @author williamEdenton@gmail.com
 */
public class SimpleWebAppRegistration implements IWebAppRegistration {
	public static final String DEFAULT_CONTEXT_ROOT = "/";
	
	private String contextRoot;
	private Map<String,String> contextInitParameters;
	private Map<String,Object> contextAttributes;
	private Collection<IServletRegistration> servlets;
	private Collection<IFilterRegistration> filters;
	private Collection<IResourceRegistration> resources;
	
	@Override
	public String getContextRoot () {
		if (null == contextRoot) {
			contextRoot = DEFAULT_CONTEXT_ROOT; 
		}
		return contextRoot;
	}
	public void setContextRoot (String contextRoot) {
		this.contextRoot = contextRoot;
	}
	
	@Override
	public Map<String,String> getContextInitParameters () {
		if (null == contextInitParameters) {
			contextInitParameters = Collections.emptyMap();
		}
		return contextInitParameters;
	}
	public void setContextInitParameters (Map<String,String> contextInitParameters) {
		this.contextInitParameters = contextInitParameters;
	}
	
	@Override
	public Map<String,Object> getContextAttributes () {
		if (null == contextAttributes) {
			contextAttributes = Collections.emptyMap();
		}
		return contextAttributes;
	}
	public void setContextAttributes (Map<String,Object> contextAttributes) {
		this.contextAttributes = contextAttributes;
	}
	
	@Override
	public Collection<IServletRegistration> getServlets () {
		if (null == servlets) {
			servlets = Collections.emptySet();
		}
		return servlets;
	}
	public void setServlets (Collection<IServletRegistration> servlets) {
		this.servlets = servlets;
	}
	
	@Override
	public Collection<IFilterRegistration> getFilters () {
		if (null == filters) {
			filters = Collections.emptySet();
		}
		return filters;
	}
	public void setFilters (Collection<IFilterRegistration> filters) {
		this.filters = filters;
	}
	
	@Override
	public Collection<IResourceRegistration> getResources () {
		if (null == resources) {
			resources = Collections.emptySet();
		}
		return resources;
	}
	public void setResources (Collection<IResourceRegistration> resources) {
		this.resources = resources;
	}

}
