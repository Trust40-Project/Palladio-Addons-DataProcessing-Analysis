/**
 * 
 */
package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.emf.common.util.URI;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.Activator;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;
import org.prolog4j.IProverFactory;
import org.prolog4j.ProverInformation;
import org.prolog4j.manager.IProverManager;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;

/**
 * This class encapsulates most of the attributes needed for an
 * {@link AnalysisWorkflow}.
 * 
 * @author Mirko Sowa
 *
 */
public class AnalysisWorkflowConfig {

	private ModelLocation usageLocation = null;
	private ModelLocation allocLocation = null;
	private ModelLocation characLocation = null;

	private IQuery query = null;

	private IProverFactory proverFactory = null;

	private SystemTranslator sysTrans = null;

	private static final String USAGE_ID = "usageID";
	private static final String ALLOC_ID = "allocID";
	private static final String CHARAC_ID = "characID";

	/**
	 * Constructor for the {@link AnalysisWorkflowConfig}
	 * 
	 * @param usageModelURI       URI of the location of the Usage Model, mandatory
	 * @param allocModelURI       URI of the location of the Allocation Model,
	 *                            mandatory
	 * @param characModelURI      URI of the location of the Characteristics Model,
	 *                            mandatory
	 * @param query               Query that will evaluate the system model,
	 *                            mandatory
	 * @param proverFactory       ProverFactory to build dedicated prover, optional,
	 *                            will choose a Prover according to target platform
	 * @param returnValueIndexing Boolean if value indexing should be returned in
	 *                            query, optional, default false
	 * @param optimNegation       Boolean if Negations should be optimised in query,
	 *                            optional, default false
	 * @param shortAssign         Boolean if short assignments should be used in
	 *                            query, optional, default false
	 * 
	 * @throws IllegalArgumentException if one of the mandatory arguments is null
	 */
	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query,
			IProverFactory proverFactory, boolean returnValueIndexing, boolean optimNegation, boolean shortAssign)
			throws IllegalArgumentException {
		if (usageModelURI != null && allocModelURI != null && characModelURI != null && query != null) {
			this.usageLocation = new ModelLocation(USAGE_ID, usageModelURI);
			this.allocLocation = new ModelLocation(ALLOC_ID, allocModelURI);
			this.characLocation = new ModelLocation(CHARAC_ID, characModelURI);
			this.query = query;
			this.setTranslator(returnValueIndexing, optimNegation, shortAssign);

			if (proverFactory != null) {
				this.proverFactory = proverFactory;
			} else {
				this.findStandardProver();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query) {
		this(usageModelURI, allocModelURI, characModelURI, query, null, false, false, false);

	}

	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query,
			IProverFactory proverFactory) {
		this(usageModelURI, allocModelURI, characModelURI, query, proverFactory, false, false, false);

	}

	/**
	 * Configures the SystemTranslator with the given parameters
	 * 
	 * @param returnValueIndexing Boolean if value indexing should be returned in
	 *                            query
	 * @param optimNegation       Boolean if Negations should be optimised in query
	 * @param shortAssign         Boolean, if short assignments should be used in
	 *                            query
	 */
	private void setTranslator(boolean returnValueIndexing, boolean optimNegation, boolean shortAssign) {

		Configuration noOptimizationConfiguration = new Configuration();

		noOptimizationConfiguration.setArgumentAndReturnValueIndexing(returnValueIndexing);
		noOptimizationConfiguration.setOptimizedNegations(optimNegation);
		noOptimizationConfiguration.setShorterAssignments(shortAssign);

		sysTrans = new SystemTranslator(noOptimizationConfiguration);
	}

	/**
	 * Gets a standard prover according to the target platform
	 */
	private void findStandardProver() {
		IProverManager proverManager = Activator.getInstance().getProverManagerInstance();

		LinkedList<ProverInformation> availableProversInformation = new LinkedList<ProverInformation>(
				proverManager.getProvers().keySet());
		for (ProverInformation i : availableProversInformation) {
			if (i.needsNativeExecutables()) {
				availableProversInformation.remove(i);
			}
		}
		Comparator<ProverInformation> compareByName = Comparator.comparing(e -> e.getName());
		Collections.sort(availableProversInformation, compareByName);

		if (availableProversInformation.get(0) != null) {
			this.proverFactory = proverManager.getProvers().get(availableProversInformation.get(0));
		}

	}

	// package-private getters

	ModelLocation getUsageLocation() {
		return usageLocation;
	}

	ModelLocation getAllocLocation() {
		return allocLocation;
	}

	ModelLocation getCharacLocation() {
		return characLocation;
	}

	IQuery getQuery() {
		return query;
	}

	IProverFactory getProverFactory() {
		return proverFactory;
	}

	SystemTranslator getSysTrans() {
		return sysTrans;
	}

}
