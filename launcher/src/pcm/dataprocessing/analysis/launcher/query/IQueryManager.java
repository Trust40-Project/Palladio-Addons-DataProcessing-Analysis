package pcm.dataprocessing.analysis.launcher.query;

import java.util.Map;

import org.prolog4j.Query;
/**
 * 
 * 
 * @author Mirko Sowa
 *
 */
public interface IQueryManager {
	Map<QueryInformation, IQuery> getQueries();
}
