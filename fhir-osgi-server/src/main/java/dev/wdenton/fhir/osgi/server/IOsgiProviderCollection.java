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

/**
 * This interface is used to publish a list of HAPI Providers
 * (typically, Resource Providers but could also be System 
 * Provider or other "plain" providers) as an OSGi Service 
 * to be published to the {@code IOsgiRestfulServer} server. This approach is 
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
 * 
 * Beans that implement this interface can be published as 
 * OSGi services and the {@code FhirOsgiServerManager} will 
 * handle registering the Provider members of the collection
 * with the speciied instance of the {@code IOsgiRestfulServer}. 
 * The following example shows
 * the Gemini Blueprint definition for the OSGi service:
 * 
 * <code><pre>
 * &lt;service interface="dev.wdenton.fhir.osgi.server.IOsgiProviderCollection">
 * 	&lt;service-properties>
 * 		&lt;entry key="<b><i>fhir.server.name</i></b>" value="servername"/>
 * 	&lt;/service-properties>
 * 	&lt;bean class="ca.uhn.fhir.osgi.provider.SimpleProviderCollection">
 * 		&lt;property name="providers" >
 * 			&lt;list>
 * 				&lt;idref component-id="<i>samplePatientProvider</i>" />
 * 				. . .
 * 			&lt;/list>
 * 		&lt;/property>
 * 	&lt;/bean>
 * &lt;/service>
 * . . .
 * &lt;bean id="<i>samplePatientProvider</i>" class="...MyPatientResourceProvider">
 * 	&lt;property name="..." />
 * &lt;/bean>
 * </pre></code>
 * 
 * where the value of the <b><i>fhir.server.name</i></b> service-property
 * matches the same {@code <service-property>} assigned to a {@code IOsgiRestfulServer} OSGi service.
 * <p>
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 */
public interface IOsgiProviderCollection {
	public Collection<Object> getProviders ();
}
