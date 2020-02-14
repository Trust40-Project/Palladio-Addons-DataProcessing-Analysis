package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;

/**
 * 
 * @author Mirko Sowa
 *
 */
@Component(immediate = true, property = { "id=pcm.dataprocessing.analysis.launcher.statequery",
		"name=StateVariableQuery" })
public class StateVariableQuery implements IQuery {

	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationStateType(CALLEE, SVAL, ST), dataTypeAttribute(ST, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(CALLEE, R),\r\n" + "isNoRoleAuthorizedStateVal(R, S, CALLEE, SVAL).";

	@Override
	public String getQueryString() {
		return myGoal;
	}

	@Override
	public Map<String, String> getResultVars() {
		return Map.of("Svar", "S");
	}

}
