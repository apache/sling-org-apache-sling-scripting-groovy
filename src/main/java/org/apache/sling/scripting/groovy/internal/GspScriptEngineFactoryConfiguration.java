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

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
    name = "Apache Sling Scripting Groovy GSP ScriptEngineFactory",
    description = "Script engine factory for Groovy's GString template engine."
)
@interface GspScriptEngineFactoryConfiguration {

    @AttributeDefinition(
        name = "service ranking",
        description = "Service property for identifying the service's ranking number."
    )
    int service_ranking() default 0;

    @AttributeDefinition(
        name = "extensions",
        description = "The extensions this script engine is registered for."
    )
    String[] extensions() default {
        "gsp"
    };

    @AttributeDefinition(
        name = "mime types",
        description = "The MIME (content) types this script engine is registered for."
    )
    String[] mimeTypes() default {
        "text/x-gsp"
    };

    @AttributeDefinition(
        name = "names",
        description = "The names under which this script engine is registered."
    )
    String[] names() default {
        "GSP",
        "gsp"
    };

}
