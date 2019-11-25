package pcm.dataprocessing.analysis.launcher.query;

import org.osgi.service.component.annotations.Component;
import org.prolog4j.Query;
import org.prolog4j.Solution;

/**
 * 
 * @author Mirko Sowa
 *
 */
@Component(immediate = true, property = { "id=pcm.dataprocessing.analysis.launcher.query", "name=StateVariableQuery" })
public class StateVariableQuery implements IQueryInput {

	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationStateType(CALLEE, SVAL, ST), dataTypeAttribute(ST, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(CALLEE, R),\r\n" + "isNoRoleAuthorizedStateVal(R, S, CALLEE, SVAL).";

	@Override
	public String getQuery() {
		return myGoal;
	}

}
