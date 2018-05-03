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

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.api.ModelAction;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelState;
import org.eclipse.vorto.repository.workflow.impl.DefaultModelWorkflow;
import org.junit.Test;

public class WorkflowTest extends AbstractIntegrationTest {

	@Test
	public void testGetModelByState() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		assertEquals(1,workflow.getModelsByState(ModelState.IN_DRAFT).size());
	}
	
	@Test
	public void testGetPossibleActionsForDraftState() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		final ModelInfo model = workflow.getModelsByState(ModelState.IN_DRAFT).get(0);
		assertEquals(1,workflow.getPossibleActions(model.getId()).size());
		assertEquals(ModelAction.RELEASE,workflow.getPossibleActions(model.getId()).get(0));
	}
	
	@Test
	public void testStartReviewProcessForModel() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		ModelInfo model = workflow.getModelsByState(ModelState.IN_DRAFT).get(0);
		model = workflow.transition(model.getId(), ModelAction.RELEASE);
		assertEquals(ModelState.IN_REVIEW,model.getState());
		assertEquals(0,workflow.getModelsByState(ModelState.IN_DRAFT).size());
		assertEquals(1,workflow.getModelsByState(ModelState.IN_REVIEW).size());
		assertEquals(2,workflow.getPossibleActions(model.getId()).size());
	}
	
	@Test (expected = WorkflowException.class)
	public void testTransitionWorkflowInvalidAction() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		ModelInfo model = workflow.getModelsByState(ModelState.IN_DRAFT).get(0);
		model = workflow.transition(model.getId(), ModelAction.APPROVE);
	}
	
	@Test
	public void testApproveModelInReviewState() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		ModelInfo model = workflow.getModelsByState(ModelState.IN_DRAFT).get(0);
		model = workflow.transition(model.getId(), ModelAction.RELEASE);
		assertEquals(ModelState.IN_REVIEW,model.getState());
		model = workflow.transition(model.getId(), ModelAction.APPROVE);
		assertEquals(1,workflow.getModelsByState(ModelState.RELEASED).size());
		assertEquals(0,workflow.getModelsByState(ModelState.IN_REVIEW).size());
		assertEquals(0,workflow.getModelsByState(ModelState.IN_DRAFT).size());
		assertEquals(0,workflow.getPossibleActions(model.getId()).size());
	}
	
	@Test
	public void testRejectModelInReviewState() {
		IModelWorkflow workflow = new DefaultModelWorkflow(this.modelRepository);
		checkinModel("Color.type");	
		ModelInfo model = workflow.getModelsByState(ModelState.IN_DRAFT).get(0);
		model = workflow.transition(model.getId(), ModelAction.RELEASE);
		assertEquals(ModelState.IN_REVIEW,model.getState());
		model = workflow.transition(model.getId(), ModelAction.REJECT);
		assertEquals(0,workflow.getModelsByState(ModelState.RELEASED).size());
		assertEquals(0,workflow.getModelsByState(ModelState.IN_REVIEW).size());
		assertEquals(1,workflow.getModelsByState(ModelState.IN_DRAFT).size());
		assertEquals(1,workflow.getPossibleActions(model.getId()).size());
	}
}
