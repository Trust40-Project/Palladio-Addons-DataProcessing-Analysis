package pcm.dataprocessing.analysis.launcher.query;

import java.util.Map;

import org.prolog4j.Query;

public interface IQueryManager {
	Map<QueryInformation, Query> getQueries();
}
