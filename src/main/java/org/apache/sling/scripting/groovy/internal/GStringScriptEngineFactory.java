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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import groovy.text.GStringTemplateEngine;
import groovy.text.TemplateEngine;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.codehaus.groovy.util.ReleaseInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = ScriptEngineFactory.class,
    property = {
        Constants.SERVICE_DESCRIPTION + "=Apache Sling Scripting Groovy GString ScriptEngineFactory",
        Constants.SERVICE_VENDOR + "=The Apache Software Foundation"
    }
)
@Designate(
    ocd = GStringScriptEngineFactoryConfiguration.class
)
public class GStringScriptEngineFactory extends AbstractScriptEngineFactory {

    @Reference(
        policy = ReferencePolicy.DYNAMIC,
        policyOption = ReferencePolicyOption.GREEDY
    )
    private volatile DynamicClassLoaderManager dynamicClassLoaderManager;

    private GStringScriptEngineFactoryConfiguration configuration;

    private BundleContext bundleContext;

    private TemplateEngine templateEngine;

    private ServiceRegistration<TemplateEngine> serviceRegistration;

    private final Logger logger = LoggerFactory.getLogger(GStringScriptEngineFactory.class);

    public GStringScriptEngineFactory() {
    }

    @Activate
    private void activate(final GStringScriptEngineFactoryConfiguration configuration, final BundleContext bundleContext) {
        logger.debug("activating");
        this.configuration = configuration;
        this.bundleContext = bundleContext;
        configure(configuration);
        templateEngine = new GStringTemplateEngine(dynamicClassLoaderManager.getDynamicClassLoader());
        registerTemplateEngine();
    }

    @Modified
    private void modified(final GStringScriptEngineFactoryConfiguration configuration) {
        logger.debug("modifying");
        this.configuration = configuration;
        configure(configuration);
    }

    @Deactivate
    private void deactivate() {
        logger.debug("deactivating");
        unregisterTemplateEngine();
        templateEngine = null;
        bundleContext = null;
    }

    private void configure(final GStringScriptEngineFactoryConfiguration configuration) {
        setExtensions(configuration.extensions());
        setMimeTypes(configuration.mimeTypes());
        setNames(configuration.names());
    }

    @Override
    public String getLanguageName() {
        return configuration.language_name();
    }

    @Override
    public String getLanguageVersion() {
        return ReleaseInfo.getVersion();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new GStringScriptEngine(this);
    }

    private void registerTemplateEngine() {
        if (templateEngine == null) {
            return;
        }
        final Dictionary<String, String> properties = new Hashtable<>();
        properties.put(Constants.SERVICE_DESCRIPTION, "Groovy's GStringTemplateEngine");
        properties.put(Constants.SERVICE_VENDOR, "The Apache Software Foundation");
        logger.info("registering {} as service {} with properties {}", templateEngine, TemplateEngine.class.getName(), properties);
        serviceRegistration = bundleContext.registerService(TemplateEngine.class, templateEngine, properties);
    }

    private void unregisterTemplateEngine() {
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            serviceRegistration = null;
        }
    }

    TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

}
