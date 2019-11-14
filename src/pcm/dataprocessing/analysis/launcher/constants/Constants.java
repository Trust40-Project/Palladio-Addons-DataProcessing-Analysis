package pcm.dataprocessing.analysis.launcher.constants;
/**
 * Constants of the PCM Dataprocessing Launcher
 * @author Mirko Sowa
 *
 */
public enum Constants {

	NAME("Trust 4.0 Modelling Launcher"),
	BUTTON_BROWSE_TEXT("Browse..."),
	BUTTON_DIR_BROWSE_TEXT("Working Directory..."),
	ANALYSIS_GOAL_LABEL("Select Analysis Goal"),
	ANALYSIS_GOAL_ONE("ONE"),
	ANALYSIS_GOAL_TWO("TWO"),
	PROLOG_INTERPRETER_LABEL("Select PROLOG Interpreter"),
	PROLOG_INTERPRETER_ONE("ONE"),
	PROLOG_INTERPRETER_TWO("TWO"),
	USAGE_MODEL_LABEL("Select Usage Model"),
	ALLOCATION_MODEL_LABEL("Select Allocation Model"),
	CHARACTERISTICS_MODEL_LABEL("Select Characteristics Model"),
	STATE_VARIABLE_QUERY("S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n" + 
			"operationStateType(CALLEE, SVAL, ST), dataTypeAttribute(ST, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n" + 
			"accessRights(CALLEE, R),\r\n" + 
			"isNoRoleAuthorizedStateVal(R, S, CALLEE, SVAL)."),
	RETURN_VARIABLE_QUERY("S=[CALLEE, CALL, OP|_], operationCall(OP, CALLEE, CALL),\r\n" + 
			"operationReturnValueType(CALLEE, RETVAL, RT), dataTypeAttribute(RT, 'EnumCharacteristicType AccessRights (_rkiSMFqnEeiY18w7ObeSrg)'),\r\n" + 
			"accessRights(OP, R),\r\n" + 
			"isNoRoleAuthorizedReturnVal(R, S, RETVAL).");
	private String attr;

	Constants(String attr) {
		this.attr = attr;
	}

	public String getConstant() {
		return attr;
	}
}
