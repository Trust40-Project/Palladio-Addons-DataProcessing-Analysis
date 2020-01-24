/**
 * 
 */
package pcm.dataprocessing.analysis.wfe.workflow;

import org.eclipse.emf.common.util.URI;
import org.prolog4j.IProverFactory;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;
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

	private AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI) {
		this.usageLocation = new ModelLocation(USAGE_ID, usageModelURI);
		this.allocLocation = new ModelLocation(ALLOC_ID, allocModelURI);
		this.characLocation = new ModelLocation(CHARAC_ID, characModelURI);

	}

	/**
	 * 
	 * @param usageModelURI
	 * @param allocModelURI
	 * @param characModelURI
	 */
	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query) {
		this(usageModelURI, allocModelURI, characModelURI);
		this.setTranslator(false, false, false);
		this.query = query;

		// TODO get standard proverFactory!
	}

	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query,
			IProverFactory proverFactory) {
		this(usageModelURI, allocModelURI, characModelURI);
		this.query = query;
		this.proverFactory = proverFactory;
		this.setTranslator(false, false, false);

	}

	public AnalysisWorkflowConfig(URI usageModelURI, URI allocModelURI, URI characModelURI, IQuery query,
			IProverFactory proverFactory, boolean returnValueIndexing, boolean optimNegation, boolean shortAssign) {
		this(usageModelURI, allocModelURI, characModelURI);
		this.query = query;
		this.proverFactory = proverFactory;
		this.setTranslator(returnValueIndexing, optimNegation, shortAssign);
	}

	private void setTranslator(boolean returnValueIndexing, boolean optimNegation, boolean shortAssign) {

		Configuration noOptimizationConfiguration = new Configuration();

		noOptimizationConfiguration.setArgumentAndReturnValueIndexing(returnValueIndexing);
		noOptimizationConfiguration.setOptimizedNegations(optimNegation);
		noOptimizationConfiguration.setShorterAssignments(shortAssign);

		sysTrans = new SystemTranslator(noOptimizationConfiguration);
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
