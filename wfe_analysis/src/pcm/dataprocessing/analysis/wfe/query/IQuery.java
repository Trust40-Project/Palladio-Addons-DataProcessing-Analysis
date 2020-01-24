package pcm.dataprocessing.analysis.wfe.query;

import java.util.Map;

/**
 * 
 * @author Mirko Sowa
 *
 */
public interface IQuery {

	/**
	 * Gets the attached query 
	 * @return String of the Prolog Query
	 */
	public String getQuery();

	/**
	 * Gets the relevant result variables
	 * 
	 * @return Map of result variables: <VariableID, Variable>
	 */
	public Map<String, String> getResultVars();

}
