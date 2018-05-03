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
package org.eclipse.vorto.repository.workflow;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelAction;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelState;

public interface IModelWorkflow {

	/**
	 * Transitions the given models to the next possible state
	 * @param models
	 */
	ModelInfo transition(ModelId model, ModelAction action);
	
	List<ModelAction> getPossibleActions(ModelId model);
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	List<ModelInfo> getModelsByState(ModelState state);
	
}
