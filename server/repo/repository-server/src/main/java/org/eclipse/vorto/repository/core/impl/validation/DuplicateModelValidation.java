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
package org.eclipse.vorto.repository.core.impl.validation;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.InvocationContext;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class DuplicateModelValidation implements IModelValidator {

	private IModelRepository modelRepository;

	public DuplicateModelValidation(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	@Override
	public void validate(ModelInfo modelResource, InvocationContext context)
			throws ValidationException {
		ModelInfo existingModel = modelRepository.getById(modelResource.getId());
		if (existingModel != null && (!isAdmin(context) && !isAuthor(existingModel, context))) {
			throw new ValidationException("Model already exists", modelResource);
		}
	}

	private boolean isAdmin(InvocationContext context) {
		return context.getUsername().equalsIgnoreCase("admin");
	}
	
	private boolean isAuthor(ModelInfo model, InvocationContext context) {
		return model.getAuthor().equalsIgnoreCase(context.getUsername());
	}
}
