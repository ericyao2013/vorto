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
package org.eclipse.vorto.repository.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ModelState {

	IN_DRAFT(ModelAction.RELEASE),
	IN_REVIEW(ModelAction.APPROVE,ModelAction.REJECT),
	RELEASED;
	
	private final List<ModelAction> possibleActions;
	
	private ModelState(ModelAction... actions) {
		possibleActions= new ArrayList<>(Arrays.asList(actions));
	}

	public boolean canTransition(ModelAction action) { 
		return possibleActions.contains(action);
	}
	
	public List<ModelAction> getPossibleActions() {
		return this.possibleActions;
	}
}
