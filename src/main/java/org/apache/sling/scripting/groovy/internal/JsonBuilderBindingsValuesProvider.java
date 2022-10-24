/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.scripting.groovy.internal;

import javax.script.Bindings;

import groovy.json.JsonBuilder;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.osgi.service.component.annotations.Component;

/**
 * BindingsValuesProvider which binds an instance of JsonBuilder.
 */
@Component(
    immediate = true,
    service = BindingsValuesProvider.class,
    property = {
        "javax.script.name=gstring",
        "service.description=Groovy JsonBuilder BindingsValuesProvider",
        "service.vendor=The Apache Software Foundation"

    }
)
public final class JsonBuilderBindingsValuesProvider implements BindingsValuesProvider {

    @Override
    public void addBindings(final Bindings bindings) {
        final JsonBuilder jsonBuilder = new JsonBuilder();
        bindings.put("jsonBuilder", jsonBuilder);
    }

}
