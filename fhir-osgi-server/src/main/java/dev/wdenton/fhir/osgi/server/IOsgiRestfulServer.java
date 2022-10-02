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

import java.util.Collection;
import ca.uhn.fhir.context.ConfigurationException;

/**
 * Instances of the FHIR OSGi Server must implement this interface
 * in order to be registered as OSGi services capable of dynamic
 * provider registration. It expected that implementations of this
 * interface will also extend {@code RestfulService}.
 *
 * The OSGi service definition for instances of the FHIR SERver
 * should have the following <service-property> entry:
 * 
 * <entry key="fhir.server.name" value="a-name"/>
 * 
 * where the value matches the same <service-property> specified
 * on the published "provider" OSGi services that are to be
 * dynamically registered in the FHIR Server instance.
 * <p>
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 *
 */
public interface IOsgiRestfulServer {
	public static final String SVCPROP_SERVICE_NAME = "fhir.server.name";

	/**
	 * Dynamically registers a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be registered
	 * @throws ConfigurationException
	 */
	public void registerOsgiProvider (Object provider) throws ConfigurationException;

	/**
	 * Dynamically unregisters a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be unregistered
	 * @throws ConfigurationException
	 */
	public void unregisterOsgiProvider (Object provider) throws ConfigurationException;

	/**
	 * Dynamically registers a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be registered
	 * @throws ConfigurationException
	 */
	public void registerOsgiProviders (Collection<Object> provider) throws ConfigurationException;

	/**
	 * Dynamically unregisters a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be unregistered
	 * @throws ConfigurationException
	 */
	public void unregisterOsgiProviders (Collection<Object> provider) throws ConfigurationException;

	/**
	 * Dynamically unregisters all of providers currently registered
	 * 
	 * @throws ConfigurationException
	 */
	public void unregisterOsgiProviders () throws ConfigurationException;

}
