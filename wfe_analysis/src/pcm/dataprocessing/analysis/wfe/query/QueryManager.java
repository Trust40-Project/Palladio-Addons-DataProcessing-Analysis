package pcm.dataprocessing.analysis.wfe.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.net4j.util.collection.BidiMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Implementation of the IQueryManger interface
 * 
 * @author Mirko Sowa
 *
 */
@Component(immediate = true)
public class QueryManager implements IQueryManager {

	private final Map<QueryInformation, IQuery> availableQueries = new HashMap<QueryInformation, IQuery>();

	@Override
	public Map<QueryInformation, IQuery> getQueries() {
		return Collections.unmodifiableMap(availableQueries);

	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void bindQuery(IQuery input, Map<String, String> serviceProperties) {
		String queryId = serviceProperties.get("id");
		String queryName = serviceProperties.get("name");
		availableQueries.put(new QueryInformation(queryId, queryName), input);
	}

	public void unbindQuery(IQuery input) {
		availableQueries.remove(input);
	}

	public void updatedQuery(IQuery input, Map<String, String> serviceProperties) {
		unbindQuery(input);
		bindQuery(input, serviceProperties);
	}

}
