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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultWorkflowService implements IWorkflowService {

	@Autowired
	private IModelRepository modelRepository;

	private static final IWorkflowModel SIMPLE_WORKFLOW = new SimpleWorkflowModel();

	public DefaultWorkflowService(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public DefaultWorkflowService() {
	}

	@Override
	public ModelInfo doAction(ModelId model, String actionName) {
		ModelInfo modelInfo = modelRepository.getById(model);
		final Optional<IState> state = SIMPLE_WORKFLOW.getState(modelInfo.getState());
		final Optional<IAction> action = state.get().getAction(actionName);
		if (action.isPresent()) {
			final IState newState = action.get().getTo();
			modelInfo.setState(newState.getName());
			return modelRepository.updateMeta(modelInfo);
		} else {
			throw new WorkflowException("The given action is invalid.");
		}
	}

	@Override
	public List<String> getPossibleActions(ModelId model) {
		ModelInfo modelInfo = modelRepository.getById(model);
		Optional<IState> state = SIMPLE_WORKFLOW.getState(modelInfo.getState());
		// FIXME: actions should be filtered based on permissions.
		return state.get().getActions().stream().map(t -> t.getName()).collect(Collectors.toList());
	}

	@Override
	public List<ModelInfo> getModelsByState(String state) {
		return this.modelRepository.search("state:" + state.toString());
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Override
	public ModelInfo start(ModelId modelId) {
		IState nextState = SIMPLE_WORKFLOW.getInitialAction().getTo();
		ModelInfo modelInfo = modelRepository.getById(modelId);
		modelInfo.setState(nextState.getName());
		return modelRepository.updateMeta(modelInfo);
	}

}
