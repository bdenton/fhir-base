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

/**
 * This interface allows a HAPI FHIR Providers (Resource 
 * Providers, System Provider, other "plain" providers) 
 * to be published as an OSGi Service. This approach is 
 * needed versus the direct publication of providers as OSGi 
 * services because references to OSGi services are
 * really proxies that only implement the methods of the
 * service's published interfaces. This means that the introspection
 * and annotation processing needed for HAPI FHIR provider
 * processing is not possible on those proxy references.
 * To get around this restriction, instances of this interface
 * will be published as OSGi services and the real providers
 * will typically be wired into the underlying bean either
 * via Spring-DM, Gemini Blueprint, or OSGi DS..
 * <p>
 * Beans that implement this interface are published as 
 * OSGi services and will be registered in the specified 
 * FHIR Server. The following example shows
 * the Gemini Blueprint definition for an OSGi service
 * that implements this interface:
 * 
 * <code><pre>
 * &lt;service interface="dev.wdenton.fhir.osgi.server.IOsgiProviderWrapper">
 * 	&lt;service-properties>
 * 		&lt;entry key="<b><i>fhir.server.name</i></b>" value="servername"/>
 * 	&lt;/service-properties>
 * 	&lt;bean class="ca.uhn.fhir.osgi.provider.SimpleProviderWrapper">
 * 		&lt;property name="provider" ref="<i>aResourceProvider</i>" />
 * 	&lt;/bean>
 * &lt;/service>
 * . . .
 * &lt;bean id="<i>aResourceProvider</i>" class="...MyPatientResourceProvider">
 * 	&lt;property name="..." />
 * &lt;/bean>
 * </pre></code>
 * 
 * where the value of the <b><i>fhir.server.name</i></b> service-property
 * matches the same <service-property> assigned to a {@code IOsgiRestfulServer} OSGi service.
 * @see IOsgiRestfulServer
 * <p>
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IOsgiProviderWrapper {
	public Object getProvider ();
}
