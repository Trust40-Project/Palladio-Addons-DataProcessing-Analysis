package pcm.dataprocessing.analysis.wfe.query.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.prolog4j.Query;
import org.prolog4j.Solution;

import pcm.dataprocessing.analysis.wfe.query.IQuery;

/**
 * 
 * @author Mirko Sowa
 *
 */
@Component(immediate = true, property = { "id=pcm.dataprocessing.analysis.launcher.returnquery", "name=ReturnVariableQuery" })
public class ReturnVariableQuery implements IQuery {

	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationReturnValueType(CALLEE, RETVAL, RT), dataTypeAttribute(RT, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(OP, R),\r\n" + "isNoRoleAuthorizedReturnVal(R, S, RETVAL).";

	@Override
	public String getQuery() {
		return myGoal;
	}

	@Override
	public Map<String, String> getResultVars() {
		return Map.of("Svar", "S");
	}

}
