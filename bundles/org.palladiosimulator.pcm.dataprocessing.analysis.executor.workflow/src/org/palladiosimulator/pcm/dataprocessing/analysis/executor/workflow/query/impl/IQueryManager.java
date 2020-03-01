package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.impl;

import java.util.Map;

import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.QueryInformation;

/**
 * Manages the {@link IQuery} with its corresponding {@link QueryInformation}
 * 
 * @author Mirko Sowa
 *
 */
public interface IQueryManager {
	Map<QueryInformation, IQuery> getQueries();
}
