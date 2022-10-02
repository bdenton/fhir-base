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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;

/**
 *
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 */
public class FhirOsgiServerPublisher {
	private static Logger log = LoggerFactory.getLogger(FhirOsgiServerPublisher.class);
	private static final String FIRST_SERVER = "#first";
	public static final String DEFAULT_SERVICE_NAME = "<default>";
	
	// the registered HAPI Server instances
	private Map<String,IOsgiRestfulServer> registeredServers = new ConcurrentHashMap<>();
	
	// the providers that have been registered for each Hapi Server instance 
	private Map<String,Collection<Collection<Object>>> serverProviders = new ConcurrentHashMap<>();
	
	// all the registered providers
	private List<Collection<Object>> registeredProviders = Collections.synchronizedList(new ArrayList<>());
	
	// providers that were registered before their assigned server was registered
	private Map<String,Collection<Collection<Object>>> pendingProviders = new ConcurrentHashMap<>();
	
	// at least one provider is registered without a server name..
	// in this case, there can only be one registered server
	private boolean haveDefaultProviders = false;
	
	/**
	 * Register a new FHIR Server OSGi service.
	 * We need to track these services so we can find the correct 
	 * server to use when registering/unregistering providers.
	 * <p>
	 * The OSGi service definition of a FHIR Server should look like:
	 * <code><pre>
	 * &lt;osgi:service ref="<b><i>some.bean</i></b>" interface="ca.uhn.fhir.osgi.IOsgiRestfulServer">
	 *     &lt;osgi:service-properties>
	 *         &lt;entry key="name" value="<b><i>osgi-service-name</i></b>"/>
	 *         &lt;entry key="fhir.server.name" value="<b><i>fhir-server-name</i></b>"/>
	 *     &lt;/osgi:service-properties>	
	 * &lt;/osgi:service>
	 * </pre></code>
	 * The <b><i>fhir-server-name</i></b> parameter is also specified for all
	 * of the FHIR Providers that are to be dynamically registered with the
	 * named FHIR Server.
	 * 
	 * @param server OSGi service implementing the FhirService interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws ConfigurationException
	 */
	public void registerOsgiRestfulServer (IOsgiRestfulServer server, Map<String,Object> props) throws ConfigurationException {
		if (server != null) {
			String serviceName = (String)props.get("name");
			if (null == serviceName) {
				serviceName = DEFAULT_SERVICE_NAME;
			}
			String serverName = (String)props.get(IOsgiRestfulServer.SVCPROP_SERVICE_NAME);
			if (serverName != null) {
				
				// Register the new OsgiRestfulServer
				
				if (registeredServers.containsKey(serverName)) {
					throw new ConfigurationException("FHIR Server named ["+serverName+"] is already registered. These names must be unique.");
				}
				log.trace("Registering FHIR Server ["+serverName+"]. (OSGi service named ["+serviceName+"])");
				registeredServers.put(serverName, server);
				
				// Providers don't have to specify a server-name as long
				// as there is only one registered OsgiRestfulServer
				if (haveDefaultProviders && registeredServers.size() > 1) {
					throw new ConfigurationException("FHIR Providers are registered without a server name. Only one FHIR Server is allowed.");
				}
				
				// Register any pending providers with the new OsgiRestfulServer.
				// This happens when providers are registered before the server
				
				Collection<Collection<Object>> providers = pendingProviders.get(serverName);
				if (providers != null) {
					log.trace("Registering FHIR providers waiting for this server to be registered.");
					pendingProviders.remove(serverName);
					for (Collection<Object> list : providers) {
						this.registerProviders(list, server, serverName);
					}
				}
				
				// Register any providers that didn't specify a server-name
				// with the first and only registered OsgiRestfulserver
				// and those providers were registered before the server
				
				if (registeredServers.size() == 1) {
					providers = pendingProviders.get(FIRST_SERVER);
					if (providers != null) {
						log.trace("Registering FHIR providers waiting for the first/only server to be registered.");
						pendingProviders.remove(FIRST_SERVER);
						for (Collection<Object> list : providers) {
							this.registerProviders(list, server, serverName);
						}
					}
				}
			} else {
				throw new ConfigurationException("FHIR Server registered in OSGi is missing the required ["+IOsgiRestfulServer.SVCPROP_SERVICE_NAME+"] service-property");
			}
		}
	}
	
	/**
	 * This method will be called when a FHIR Server OSGi service
	 * is being removed from the container. This normally will only
	 * occur when its bundle is stopped because it is being removed
	 * or updated. 
	 * 
	 * @param server OSGi service implementing the FhirService interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws ConfigurationException
	 */
	public void unregisterFhirServer (IOsgiRestfulServer server, Map<String,Object> props) throws ConfigurationException {
		if (server != null) {
			String serverName = (String)props.get(IOsgiRestfulServer.SVCPROP_SERVICE_NAME);
			if (serverName != null) {
				IOsgiRestfulServer service = registeredServers.get(serverName);
				if (service != null) {
					log.trace("Unregistering FHIR Server ["+serverName+"]");
					service.unregisterOsgiProviders();
					registeredServers.remove(serverName);
					log.trace("Dequeue any FHIR providers waiting for this server");
					pendingProviders.remove(serverName);
					if (registeredServers.size() == 0) {
						log.trace("Dequeue any FHIR providers waiting for the first/only server");
						pendingProviders.remove(FIRST_SERVER);
					}
					Collection<Collection<Object>> providers = serverProviders.get(serverName);
					if (providers != null) {
						serverProviders.remove(serverName);
						registeredProviders.removeAll(providers);
					}
				}
			} else {
				throw new ConfigurationException("FHIR Server registered in OSGi is missing the required ["+IOsgiRestfulServer.SVCPROP_SERVICE_NAME+"] service-property");
			}
		}
	}
	
	/**
	 * Register a new FHIR Provider-Bundle OSGi service.
	 * 
	 * This could be a "plain" provider that is published with the 
	 * FhirProvider interface or it could be a resource provider that
	 * is published with either that same interface or the IResourceProvider
	 * interface.
	 * 
	 * (That check is not made here but is included as usage documentation)
	 *  
	 * <p>
	 * The OSGi service definition of a FHIR Provider would look like:
	 * <code><pre>
	 * &lt;osgi:service ref="<b><i>some.bean</i></b>" interface="ca.uhn.fhir.osgi.IResourceProvider">
	 *     &lt;osgi:service-properties>
	 *         &lt;entry key="name" value="<b><i>osgi-service-name</i></b>"/>
	 *         &lt;entry key="fhir.server.name" value="<b><i>fhir-server-name</i></b>"/>
	 *     &lt;/osgi:service-properties>	
	 * &lt;/osgi:service>
	 * </pre></code>
	 * The <b><i>fhir-server-name</i></b> parameter is the value assigned to the
	 * <code>fhir.server.name</code> service-property of one of the OSGi-published
	 * FHIR Servers.
	 * 
	 * @param server OSGi service implementing a FHIR provider interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws ConfigurationException
	 */
	public void registerFhirProviders (IOsgiProviderCollection bundle, Map<String,Object> props) throws ConfigurationException {
		if (bundle != null) {
			Collection<Object> providers = bundle.getProviders();
			if (providers != null && !providers.isEmpty()) {
				try {
					String serverName = (String)props.get(IOsgiRestfulServer.SVCPROP_SERVICE_NAME);
					String ourServerName = getServerName(serverName);
					String bundleName = (String)props.get("name");
					if (null == bundleName) {
						bundleName = DEFAULT_SERVICE_NAME;
					}
					log.trace("Register FHIR Provider Bundle ["+bundleName+"] on FHIR Server ["+ourServerName+"]");
					IOsgiRestfulServer server = registeredServers.get(ourServerName);
					if (server != null) {
						registerProviders(providers, server, serverName);
					} else {
						log.trace("Queue the Provider Bundle waiting for FHIR Server to be registered");
						Collection<Collection<Object>> pending;
						synchronized(pendingProviders) {
							pending = pendingProviders.get(serverName);
							if (null == pending) {
								pending = Collections.synchronizedCollection(new ArrayList<Collection<Object>>());
								pendingProviders.put(serverName, pending);
							}
						}
						pending.add(providers);
					}
				
				} catch (BadServerException e) {
					throw new ConfigurationException("Unable to register the OSGi FHIR Provider. Multiple Restful Servers exist. Specify the ["+IOsgiRestfulServer.SVCPROP_SERVICE_NAME+"] service-property");
				}
			}
		}
	}
	
	protected void registerProviders (Collection<Object> providers, IOsgiRestfulServer server, String serverName) throws ConfigurationException {
		server.registerOsgiProviders(providers);

		Collection<Collection<Object>> active;
		synchronized(serverProviders) {
			active = serverProviders.get(serverName);
			if (null == active) {
				active = Collections.synchronizedCollection(new ArrayList<Collection<Object>>());
				serverProviders.put(serverName, active);
			}
		}
		active.add(providers);
		registeredProviders.add(providers);
	}
	
	/**
	 * This method will be called when a FHIR Provider OSGi service
	 * is being removed from the container. This normally will only
	 * occur when its bundle is stopped because it is being removed
	 * or updated. 
	 * 
	 * @param server OSGi service implementing one of the provider 
	 * interfaces
	 * @param props the <service-properties> for that service
	 * 
	 * @throws ConfigurationException
	 */
	public void unregisterFhirProviders (IOsgiProviderCollection bundle, Map<String,Object> props) throws ConfigurationException {
		if (bundle != null) {
			Collection<Object> providers = bundle.getProviders();
			if (providers != null && !providers.isEmpty()) {
				try {
					registeredProviders.remove(providers);
					String serverName = (String)props.get(IOsgiRestfulServer.SVCPROP_SERVICE_NAME);
					String ourServerName = getServerName(serverName);
					IOsgiRestfulServer server = registeredServers.get(ourServerName);
					if (server != null) {
						
						server.unregisterOsgiProviders(providers);
						
						Collection<Collection<Object>> active = serverProviders.get(serverName);
						if (active != null) {
							active.remove(providers);
						}
					}
				} catch (BadServerException e) {
					throw new ConfigurationException("Unable to register the OSGi FHIR Provider. Multiple Restful Servers exist. Specify the ["+IOsgiRestfulServer.SVCPROP_SERVICE_NAME+"] service-property");
				}
			}
		}
	}

	/*
	 * Adjust the FHIR Server name allowing for null which would
	 * indicate that the Provider should be registered with the
	 * only FHIR Server defined.
	 */
	private String getServerName (String osgiName) throws BadServerException {
		String result = osgiName;
		if (null == result) {
			if (registeredServers.isEmpty()) { // wait for the first one
				haveDefaultProviders = true; // only allow one server
				result = FIRST_SERVER;
			} else
			if (registeredServers.size() == 1) { // use the only one
				haveDefaultProviders = true; // only allow one server
				result = registeredServers.keySet().iterator().next();
			} else {
				throw new BadServerException();
			}
		}
		return result;
	}
	
	class BadServerException extends Exception {
		BadServerException() {
			super();
		}
	}

}
