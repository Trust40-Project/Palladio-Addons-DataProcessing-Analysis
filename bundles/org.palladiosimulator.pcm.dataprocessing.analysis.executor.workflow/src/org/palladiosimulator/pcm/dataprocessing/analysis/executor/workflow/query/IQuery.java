package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query;

import java.util.Map;

/**
 * 
 * @author Mirko Sowa
 *
 */
public interface IQuery {

	/**
	 * Gets an additional part of theory that is required by the query.
	 * @return String of the additional Prolog Theory
	 */
	public default String getAdditionalTheory() {
		return "";
	}
	
	/**
	 * Gets the attached query 
	 * @return String of the Prolog Query
	 */
	public String getQueryString();

	/**
	 * Gets the relevant result variables
	 * 
	 * @return Map of result variables: <VariableID, Variable>
	 */
	public Map<String, String> getResultVars();

}
