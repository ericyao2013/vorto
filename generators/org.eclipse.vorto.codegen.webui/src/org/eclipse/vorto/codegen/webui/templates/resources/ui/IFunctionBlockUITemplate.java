/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.webui.templates.resources.ui;

import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

public interface IFunctionBlockUITemplate {

	String renderJavascript(FunctionblockProperty fbProperty,InvocationContext ctx);
	
	String renderHtml(FunctionblockProperty fbProperty,InvocationContext ctx);
}
