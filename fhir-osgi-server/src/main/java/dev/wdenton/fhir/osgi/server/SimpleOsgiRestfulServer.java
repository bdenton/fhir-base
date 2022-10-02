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
package dev.wdenton.fhir.osgi.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 *
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 */
public class SimpleOsgiRestfulServer extends RestfulServer implements IOsgiRestfulServer {
	private static final long serialVersionUID = -6649896883999039096L;

	private static Logger log = LoggerFactory.getLogger(SimpleOsgiRestfulServer.class);
	
	private Collection<Object> serverProviders = Collections.synchronizedCollection(new ArrayList<Object>());

	public SimpleOsgiRestfulServer () {
		super();
	}

	public SimpleOsgiRestfulServer (FhirContext theCtx) {
		super(theCtx);
	}

	/**
	 * Dynamically registers a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be registered
	 * @throws ConfigurationException
	 */
	@Override
	public void registerOsgiProvider (Object provider) throws ConfigurationException {
		if (null == provider) {
			throw new NullPointerException("FHIR Provider cannot be null");
		}
		try {
			super.registerProvider(provider);
			log.trace("registered provider. class ["+provider.getClass().getName()+"]");
			this.serverProviders.add(provider);
		} catch (Exception e) {
			log.error("Error registering FHIR Provider", e);
			throw new ConfigurationException("Error registering FHIR Provider", e);
		}
	}

	/**
	 * Dynamically unregisters a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be unregistered
	 * @throws ConfigurationException
	 */
	@Override
	public void unregisterOsgiProvider (Object provider) throws ConfigurationException {
		if (null == provider) {
			throw new NullPointerException("FHIR Provider cannot be null");
		}
		try {
			this.serverProviders.remove(provider);
			log.trace("unregistered provider. class ["+provider.getClass().getName()+"]");
			super.unregisterProvider(provider);
		} catch (Exception e) {
			log.error("Error unregistering FHIR Provider", e);
			throw new ConfigurationException("Error unregistering FHIR Provider", e);
		}
	}

	/**
	 * Dynamically registers a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be registered
	 * @throws ConfigurationException
	 */
	@Override
	public void registerOsgiProviders (Collection<Object> providers) throws ConfigurationException {
		if (null == providers) {
			throw new NullPointerException("FHIR Provider list cannot be null");
		}
		try {
			super.registerProviders(providers);
			for (Object provider : providers) {
				log.trace("registered provider. class ["+provider.getClass().getName()+"]");
				this.serverProviders.add(provider);
			}
		} catch (Exception e) {
			log.error("Error registering FHIR Providers", e);
			throw new ConfigurationException("Error registering FHIR Providers", e);
		}
	}

	/**
	 * Dynamically unregisters a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be unregistered
	 * @throws ConfigurationException
	 */
	@Override
	public void unregisterOsgiProviders (Collection<Object> providers) throws ConfigurationException {
		if (null == providers) {
			throw new NullPointerException("FHIR Provider list cannot be null");
		}
		try {
			for (Object provider : providers) {
				log.trace("unregistered provider. class ["+provider.getClass().getName()+"]");
				this.serverProviders.remove(provider);
			}
			super.unregisterProviders(providers);
		} catch (Exception e) {
			log.error("Error unregistering FHIR Providers", e);
			throw new ConfigurationException("Error unregistering FHIR Providers", e);
		}
	}

	/**
	 * Dynamically unregisters all of providers currently registered
	 * 
	 * @throws ConfigurationException
	 */
	@Override
	public void unregisterOsgiProviders () throws ConfigurationException {
		// need to make a copy to be able to remove items
		Collection<Object> providers = new ArrayList<Object>();
		providers.addAll(this.serverProviders);
		this.unregisterOsgiProviders(providers);
	}

}
