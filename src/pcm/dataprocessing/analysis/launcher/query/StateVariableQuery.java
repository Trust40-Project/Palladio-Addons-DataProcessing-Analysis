package pcm.dataprocessing.analysis.launcher.query;

import org.prolog4j.Query;
import org.prolog4j.Solution;

//@Component(immediate = true, property = { "id=package pcm.dataprocessing.analysis.launcher.query", "name=ReturnVariableQuery" })
public class StateVariableQuery extends Query {

	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationStateType(CALLEE, SVAL, ST), dataTypeAttribute(ST, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(CALLEE, R),\r\n" + "isNoRoleAuthorizedStateVal(R, S, CALLEE, SVAL).";

	protected StateVariableQuery(String arg0) {
		super(myGoal);
	}

	/* ?? */
	@Override
	public Query bind(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query bind(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Solution<A> solve(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
