/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.scripting.groovy.it;

import javax.inject.Inject;
import javax.script.ScriptEngineFactory;

import aQute.bnd.osgi.Constants;
import groovy.text.TemplateEngine;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.servlets.ServletResolver;
import org.apache.sling.auth.core.AuthenticationSupport;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.testing.paxexam.TestSupport;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.options.ModifiableCompositeOption;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.service.http.HttpService;

import static org.apache.sling.testing.paxexam.SlingOptions.slingModels;
import static org.apache.sling.testing.paxexam.SlingOptions.slingQuickstartOakTar;
import static org.apache.sling.testing.paxexam.SlingOptions.slingResourcePresence;
import static org.apache.sling.testing.paxexam.SlingOptions.slingScripting;
import static org.apache.sling.testing.paxexam.SlingOptions.spifly;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.cm.ConfigurationAdminOptions.factoryConfiguration;

public class GroovyTestSupport extends TestSupport {

    @Inject
    protected ServletResolver servletResolver;

    // SlingScriptAdapterFactory
    @Inject
    @Filter(value = "(adapters=org.apache.sling.api.scripting.SlingScript)")
    protected AdapterFactory adapterFactory;

    @Inject
    protected SlingRequestProcessor slingRequestProcessor;

    @Inject
    protected AuthenticationSupport authenticationSupport;

    @Inject
    protected HttpService httpService;

    @Inject
    @Filter(value = "(names=GString)")
    protected ScriptEngineFactory scriptEngineFactory;

    @Inject
    protected TemplateEngine templateEngine;

    public ModifiableCompositeOption baseConfiguration() {
        return composite(
            super.baseConfiguration(),
            quickstart(),
            // Sling Scripting Groovy
            testBundle("bundle.filename"),
            mavenBundle().groupId("org.apache.groovy").artifactId("groovy").versionAsInProject(),
            mavenBundle().groupId("org.apache.groovy").artifactId("groovy-templates").versionAsInProject(),
            // testing
            testGroovyJson(),
            slingResourcePresence(),
            factoryConfiguration("org.apache.sling.jcr.repoinit.RepositoryInitializer")
                .put("scripts", new String[]{"create path (sling:OrderedFolder) /content/groovy\nset ACL for everyone\nallow jcr:read on /content/groovy\nend"})
                .asOption(),
            mavenBundle().groupId("org.jsoup").artifactId("jsoup").versionAsInProject()
        );
    }

    @ProbeBuilder
    public TestProbeBuilder probeConfiguration(final TestProbeBuilder testProbeBuilder) {
        testProbeBuilder.setHeader(Constants.EXPORT_PACKAGE, "org.apache.sling.scripting.groovy.it.app");
        testProbeBuilder.setHeader("Sling-Model-Packages", "org.apache.sling.scripting.groovy.it.app");
        testProbeBuilder.setHeader("Sling-Initial-Content", "initial-content");
        return testProbeBuilder;
    }

    protected Option testGroovyJson() {
        return composite(
            mavenBundle().groupId("org.apache.groovy").artifactId("groovy-json").versionAsInProject(),
            spifly()
        );
    }

    protected Option quickstart() {
        final int httpPort = findFreePort();
        final String workingDirectory = workingDirectory();
        return composite(
            slingQuickstartOakTar(workingDirectory, httpPort),
            slingModels(),
            slingScripting()
        );
    }

}
