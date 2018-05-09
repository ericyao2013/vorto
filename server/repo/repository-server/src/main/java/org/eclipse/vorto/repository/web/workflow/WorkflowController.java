package org.eclipse.vorto.repository.web.workflow;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="Workflow Controller", description="REST API to manage the state of a model")
@RestController
@RequestMapping(value="/rest/workflows")
public class WorkflowController {

	@Autowired
    private IWorkflowService workflowService;
    
    @ApiOperation(value = "Returns the list of possible actions for a the specific model state")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Not found"), 
            				@ApiResponse(code = 200, message = "OK")})
    @RequestMapping(method = RequestMethod.GET,
    				value = "/actions/{namespace}/{name}/{version:.+}",
    				produces = "application/json")
    public List<String> getPossibleActions(	@ApiParam(value = "namespace", required = true) @PathVariable String namespace,
									    		@ApiParam(value = "name", required = true) @PathVariable String name,
									    		@ApiParam(value = "version", required = true) @PathVariable String version) {
    	
    	final ModelId modelId = new ModelId(name, namespace, version);
    	return workflowService.getPossibleActions(modelId);
    }
    
    @ApiOperation(value = "Transitions the model state to the next for the provided action.")
    @RequestMapping(method = RequestMethod.PUT,
    				value = "/actions/{namespace}/{name}/{version:.+}/{actionName}",
    				produces = "application/json")
    public ModelInfo executeAction(	@ApiParam(value = "namespace", required = true) @PathVariable String namespace,
									    		@ApiParam(value = "name", required = true) @PathVariable String name,
									    		@ApiParam(value = "version", required = true) @PathVariable String version,
									    		@ApiParam(value = "actionName", required = true) @PathVariable String actionName) {
    	
    	final ModelId modelId = new ModelId(name, namespace, version);
    	return workflowService.doAction(modelId, actionName);
	
    }
}
