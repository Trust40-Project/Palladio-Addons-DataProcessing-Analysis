package pcm.dataprocessing.analysis.wfe.query;

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
