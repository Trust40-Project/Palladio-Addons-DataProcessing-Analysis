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
	CHARACTERISTICS_MODEL_LABEL("Select Characteristics Model");

	private String attr;

	Constants(String attr) {
		this.attr = attr;
	}

	public String getConstant() {
		return attr;
	}
}
