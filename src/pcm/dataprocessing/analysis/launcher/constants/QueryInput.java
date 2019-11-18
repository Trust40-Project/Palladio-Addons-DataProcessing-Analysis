package pcm.dataprocessing.analysis.launcher.constants;

public enum QueryInput {

	STATE_VARIABLE_QUERY("State Variable", "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationStateType(CALLEE, SVAL, ST), dataTypeAttribute(ST, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(CALLEE, R),\r\n" + "isNoRoleAuthorizedStateVal(R, S, CALLEE, SVAL)."),
	RETURN_VARIABLE_QUERY("Return Variable", "S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n"
			+ "operationReturnValueType(CALLEE, RETVAL, RT), dataTypeAttribute(RT, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n"
			+ "accessRights(OP, R),\r\n" + "isNoRoleAuthorizedReturnVal(R, S, RETVAL).");

	private String query;
	private String name;

	QueryInput(String name, String query) {
		this.name = name;
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
	public String getName() {
		return name;
	}
	
	
}
