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
 *
 * Copyright (c) 2019-2022 William E. Denton
 * @author williamEdenton@gmail.com
 */
public class SimpleOsgiProviderWapper implements IOsgiProviderWrapper {

	private Object provider;

	public void setProvider (Object provider) {
		this.provider = provider;
	}
	
	@Override
	public Object getProvider () {
		return this.provider;
	}

}
