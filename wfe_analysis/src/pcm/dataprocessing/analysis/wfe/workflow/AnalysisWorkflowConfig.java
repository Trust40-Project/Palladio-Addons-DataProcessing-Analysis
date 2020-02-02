/**
 * 
 */
package pcm.dataprocessing.analysis.wfe.workflow;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.emf.common.util.URI;
import org.prolog4j.IProverFactory;
import org.prolog4j.ProverInformation;
import org.prolog4j.manager.IProverManager;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;
import pcm.dataprocessing.analysis.wfe.Activator;
import pcm.dataprocessing.analysis.wfe.query.IQuery;

/**
 * @author Mirko Sowa
 *
 */
public class AnalysisWorkflowConfig {

	ModelLocation usageLocation = null;
	ModelLocation allocLocation = null;
	ModelLocation characLocation = null;

	IQuery query = null;

	IProverFactory proverFactory = null;

	SystemTranslator sysTrans = null;

	private static final String USAGE_ID = "usageID";
	private static final String ALLOC_ID = "allocID";
	private static final String CHARAC_ID = "characID";

	/**
	 * 
	 * @param usageModelURI
	 * @param allocModelURI
	 * @param characModelURI
	 * @param query
	 * @param proverFactory
	 * @param returnValueIndexing
	 * @param optimNegation
	 * @param shortAssign
	 */
	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query,
			IProverFactory proverFactory, boolean returnValueIndexing, boolean optimNegation, boolean shortAssign)
			throws IllegalArgumentException {
		if (usageLocation != null && allocLocation != null && characLocation != null && query != null) {
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
	 * 
	 * @param returnValueIndexing
	 * @param optimNegation
	 * @param shortAssign
	 */
	private void setTranslator(boolean returnValueIndexing, boolean optimNegation, boolean shortAssign) {

		Configuration noOptimizationConfiguration = new Configuration();

		noOptimizationConfiguration.setArgumentAndReturnValueIndexing(returnValueIndexing);
		noOptimizationConfiguration.setOptimizedNegations(optimNegation);
		noOptimizationConfiguration.setShorterAssignments(shortAssign);

		sysTrans = new SystemTranslator(noOptimizationConfiguration);
	}

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
