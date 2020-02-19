package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;

/**
 * 
 * 
 * 
 * @author Mirko Sowa
 *
 */
@Component(immediate = true, property = { "id=pcm.dataprocessing.analysis.launcher.returnquery", "name=ReturnVariableQuery" })
public class ReturnVariableQuery implements IQuery {

	private static final String ADDITIONAL_THEORY = "accessRights(OP, R) :-\n" + 
			"	findall(X, operationProperty(OP, 'EnumCharacteristicType Roles (_vP5JoFqnEeiY18w7ObeSrg)', X), R).\n" + 
			"	\n" + 
			"isNoRoleAuthorizedReturnVal([], _, _).	\n" + 
			"isNoRoleAuthorizedReturnVal([Role | R], S, RETVAL) :-\n" + 
			"	\\+ returnValue(S, RETVAL, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)', Role),\n" + 
			"	isNoRoleAuthorizedReturnVal(R, S, RETVAL).\n";
	
	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationReturnValueType(CALLEE, RETVAL, RT), dataTypeAttribute(RT, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(OP, R),\r\n" + "isNoRoleAuthorizedReturnVal(R, S, RETVAL).";

	@Override
	public String getQueryString() {
		return myGoal;
	}

	@Override
	public Map<String, String> getResultVars() {
		return Map.of("Callee", "CALLEE", "Call", "CALL", "Operation", "OP", "Return Value", "RETVAL", "Return Type", "RT", "Roles", "R");
	}

	@Override
	public String getAdditionalTheory() {
		return ADDITIONAL_THEORY;
	}

}
