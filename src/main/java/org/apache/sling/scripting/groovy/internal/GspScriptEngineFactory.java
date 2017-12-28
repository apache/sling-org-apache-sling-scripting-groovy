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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.codehaus.groovy.util.ReleaseInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Script engine for Groovy Server Pages.
 */
@Component(
    service = ScriptEngineFactory.class,
    property = {
        "service.description=GSP Script Engine",
        "service.vendor=The Apache Software Foundation"
    }
)
@Designate(
    ocd = GspScriptEngineFactoryConfiguration.class
)
public class GspScriptEngineFactory extends AbstractScriptEngineFactory {

    @Reference
    private DynamicClassLoaderManager dynamicClassLoaderManager;

    private GspScriptEngineFactoryConfiguration configuration;

    private final Logger logger = LoggerFactory.getLogger(GspScriptEngineFactory.class);

    public GspScriptEngineFactory() {
    }

    @Activate
    private void activate(final GspScriptEngineFactoryConfiguration configuration) {
        logger.debug("activating");
        this.configuration = configuration;
        configure(configuration);
    }

    @Modified
    private void modified(final GspScriptEngineFactoryConfiguration configuration) {
        logger.debug("modifying");
        this.configuration = configuration;
        configure(configuration);
    }

    @Deactivate
    private void deactivate() {
        logger.debug("deactivating");
    }

    private void configure(final GspScriptEngineFactoryConfiguration configuration) {
        setExtensions(configuration.extensions());
        setMimeTypes(configuration.mimeTypes());
        setNames(configuration.names());
    }

    public String getLanguageName() {
        return "Groovy Server Pages";
    }

    public String getLanguageVersion() {
        return ReleaseInfo.getVersion();
    }

    public ScriptEngine getScriptEngine() {
        return new GspScriptEngine(this, dynamicClassLoaderManager.getDynamicClassLoader());
    }

}
