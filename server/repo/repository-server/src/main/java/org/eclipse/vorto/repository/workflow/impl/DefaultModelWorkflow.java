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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelAction;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelState;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.workflow.IModelWorkflow;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelWorkflow implements IModelWorkflow {

	@Autowired
	private IModelRepository modelRepository;
	
	public DefaultModelWorkflow(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	public DefaultModelWorkflow() {
	}
	
	@Override
	public ModelInfo transition(ModelId model, ModelAction action) {
		ModelInfo modelInfo = modelRepository.getById(model);
		if (modelInfo.getState().canTransition(action)) {
			if (action == ModelAction.RELEASE) {
				modelInfo.setState(ModelState.IN_REVIEW);
			} else if (action == ModelAction.APPROVE) {
				modelInfo.setState(ModelState.RELEASED);
			} else if (action == ModelAction.REJECT) {
				modelInfo.setState(ModelState.IN_DRAFT);
			}
			
			return this.modelRepository.updateMeta(modelInfo);
		} else {
			throw new WorkflowException("Action is not allowed in state '"+modelInfo.getState()+"'");
		}
	}

	@Override
	public List<ModelAction> getPossibleActions(ModelId model) {
		ModelInfo modelInfo = modelRepository.getById(model);
		return modelInfo.getState().getPossibleActions();
	}

	@Override
	public List<ModelInfo> getModelsByState(ModelState state) {
		return this.modelRepository.search("state:"+state.toString());
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	

}
