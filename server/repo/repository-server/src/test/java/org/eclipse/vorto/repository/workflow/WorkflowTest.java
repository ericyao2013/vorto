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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.Test;

public class WorkflowTest extends AbstractIntegrationTest {

	@Test
	public void testGetModelByState() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		assertEquals(1,workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName()).size());
	}
	
	@Test
	public void testGetPossibleActionsForDraftState() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		assertEquals(1,workflow.getPossibleActions(model.getId(),UserContext.user(getCallerId())).size());
		assertEquals(SimpleWorkflowModel.ACTION_RELEASE.getName(),workflow.getPossibleActions(model.getId(),UserContext.user(getCallerId())).get(0));
	}
	
	@Test
	public void testStartReviewProcessForModel() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_RELEASE.getName());
		assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(),model.getState());
		assertEquals(0,workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName()).size());
		assertEquals(1,workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName()).size());
		
		when(userRepository.findByUsername(UserContext.user(getCallerId()).getHashedUsername())).thenReturn(User.create(getCallerId(),Role.USER));

		assertEquals(1,workflow.getPossibleActions(model.getId(),UserContext.user(getCallerId())).size());
	}
	
	@Test (expected = WorkflowException.class)
	public void testTransitionWorkflowInvalidAction() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_APPROVE.getName());
	}
	
	@Test
	public void testApproveModelByAdminInReviewState() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		
		when(userRepository.findByUsername(UserContext.user(getCallerId()).getHashedUsername())).thenReturn(User.create(getCallerId(),Role.USER));
		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_RELEASE.getName());
		assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(),model.getState());
		
		when(userRepository.findByUsername(UserContext.user("admin").getHashedUsername())).thenReturn(User.create("admin",Role.ADMIN));
		assertEquals(1,workflow.getPossibleActions(model.getId(), UserContext.user(getCallerId())).size());
		assertEquals(2,workflow.getPossibleActions(model.getId(), UserContext.user("admin")).size());
		model = workflow.doAction(model.getId(),UserContext.user("admin"), SimpleWorkflowModel.ACTION_APPROVE.getName());
		assertEquals(1,workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName()).size());
		assertEquals(0,workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName()).size());
		assertEquals(0,workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName()).size());
		assertEquals(0,workflow.getPossibleActions(model.getId(),UserContext.user(getCallerId())).size());
	}
	
	@Test (expected = WorkflowException.class)
	public void testApproveModelByUserInReviewState() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		
		when(userRepository.findByUsername(UserContext.user(getCallerId()).getHashedUsername())).thenReturn(User.create(getCallerId(),Role.USER));
		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_RELEASE.getName());
		assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(),model.getState());
		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_APPROVE.getName());
	}

	@Test
	public void testRejectModelInReviewState() {
		IWorkflowService workflow = new DefaultWorkflowService(this.modelRepository,userRepository);
		ModelInfo model = checkinModel("Color.type");	
		workflow.start(model.getId());
		
		when(userRepository.findByUsername(UserContext.user(getCallerId()).getHashedUsername())).thenReturn(User.create(getCallerId(),Role.USER));

		model = workflow.doAction(model.getId(),UserContext.user(getCallerId()), SimpleWorkflowModel.ACTION_RELEASE.getName());
		assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(),model.getState());
		
		when(userRepository.findByUsername(UserContext.user("admin").getHashedUsername())).thenReturn(User.create("admin",Role.ADMIN));

		model = workflow.doAction(model.getId(),UserContext.user("admin"), SimpleWorkflowModel.ACTION_REJECT.getName());
		assertEquals(0,workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName()).size());
		assertEquals(0,workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName()).size());
		assertEquals(1,workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName()).size());
		assertEquals(1,workflow.getPossibleActions(model.getId(),UserContext.user(getCallerId())).size());
	}
}
