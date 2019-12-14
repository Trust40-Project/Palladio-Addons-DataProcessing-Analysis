package pcm.dataprocessing.analysis.launcher.query;

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
	 * @return String of result variables
	 */
	public String getResultVars();

}
