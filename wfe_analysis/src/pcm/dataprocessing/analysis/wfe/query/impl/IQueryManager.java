package pcm.dataprocessing.analysis.wfe.query.impl;

import java.util.Map;

import pcm.dataprocessing.analysis.wfe.query.IQuery;
import pcm.dataprocessing.analysis.wfe.query.QueryInformation;

/**
 * 
 * 
 * @author Mirko Sowa
 *
 */
public interface IQueryManager {
	Map<QueryInformation, IQuery> getQueries();
}
