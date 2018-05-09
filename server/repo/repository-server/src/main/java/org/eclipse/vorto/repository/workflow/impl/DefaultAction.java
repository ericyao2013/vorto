/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.workflow.impl;

import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;

public class DefaultAction implements IAction {

	private String name;
	private String description;
	private IState to;
	
	public DefaultAction(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public DefaultAction(String name) {
		this(name,"");
	}
	
	public void setTo(IState to) {
		this.to = to;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public IState getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "DefaultAction [name=" + name + ", description=" + description + ", to=" + to + "]";
	}
	
	

}
