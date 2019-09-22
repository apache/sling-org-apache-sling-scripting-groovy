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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import groovy.lang.Writable;
import groovy.text.Template;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;

public class GStringScriptEngine extends AbstractSlingScriptEngine {

    private final GStringScriptEngineFactory scriptEngineFactory;

    GStringScriptEngine(final GStringScriptEngineFactory gStringScriptEngineFactory) {
        super(gStringScriptEngineFactory);
        this.scriptEngineFactory = gStringScriptEngineFactory;
    }

    public Object eval(final Reader reader, final ScriptContext scriptContext) throws ScriptException {
        final Template template;
        try {
            template = scriptEngineFactory.getTemplateEngine().createTemplate(reader);
        } catch (IOException | ClassNotFoundException e) {
            throw new ScriptException("Unable to compile GString template: " + e.getMessage());
        }

        final Bindings bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
        final Writer writer = scriptContext.getWriter();
        final Writable result = template.make(bindings);

        try {
            result.writeTo(writer);
        } catch (IOException e) {
            throw new ScriptException("Unable to write result of script execution: " + e.getMessage());
        }

        return null;
    }

}
