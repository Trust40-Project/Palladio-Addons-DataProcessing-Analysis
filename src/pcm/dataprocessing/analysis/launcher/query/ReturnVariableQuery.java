package pcm.dataprocessing.analysis.launcher.query;

import org.prolog4j.Query;
import org.prolog4j.Solution;

//@Component(immediate = true, property = { "id=package pcm.dataprocessing.analysis.launcher.query", "name=ReturnVariableQuery" })
public class ReturnVariableQuery extends Query{
	
	private final static String myGoal = "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\\r\\n\"\r\n"
			+ "operationReturnValueType(CALLEE, RETVAL, RT), dataTypeAttribute(RT, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\\r\\n\"\r\n"
			+ "accessRights(OP, R),\\r\\n\" + \"isNoRoleAuthorizedReturnVal(R, S, RETVAL).";
	
	protected ReturnVariableQuery(String arg0) {
		super(myGoal);
	}
		
	
	/* Was soll das tun */

	
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
