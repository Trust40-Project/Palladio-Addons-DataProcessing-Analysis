package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.constants;
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
	PROLOG_INTERPRETER_LABEL("Select PROLOG Interpreter"),
	USAGE_MODEL_LABEL("Select Usage Model"),
	ALLOCATION_MODEL_LABEL("Select Allocation Model"),
	CHARACTERISTICS_MODEL_LABEL("Select Characteristics Model"),
	ADV_TAB_NAME("Advanced Configuration"),
	ADV_ARG_AND_RETURN("Argument and Return Indexing"),
	ADV_OPTIM_NEGATION("Optimized Negations"),
	ADV_SHORT_ASSIGN("Short Asssignments"),
	CONSOLE_ID("pcm.dataprocessing.analysis.launcher.console#"),
	DEFAULT_CONFIG_VALUE("default");
	

	private final String attr;

	Constants(String attr) {
		this.attr = attr;
	}

	public String getConstant() {
		return attr;
	}
}
