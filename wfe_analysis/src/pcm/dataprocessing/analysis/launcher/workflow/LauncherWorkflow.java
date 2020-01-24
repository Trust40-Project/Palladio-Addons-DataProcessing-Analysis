package pcm.dataprocessing.analysis.launcher.workflow;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.eclipse.emf.common.util.URI;
import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import pcm.dataprocessing.analysis.launcher.workflow.job.SystemModelJob;

/**
 * 
 * @author Mirko Sowa
 *
 */
public class LauncherWorkflow {

	ModelLocation usageLocation = null;
	ModelLocation allocLocation = null;
	ModelLocation characLocation = null;

	private static final String USAGE_ID = "usageID";
	private static final String ALLOC_ID = "allocID";
	private static final String CHARAC_ID = "characID";
	
	private static final String SYSTEM_ID = "systemID";


	private AnalysisBlackboard myBlackboard = new AnalysisBlackboard();

	/**
	 * 
	 */
	public void launch() {
		if (usageLocation.getModelID() != null && allocLocation.getModelID() != null
				&& characLocation.getModelID() != null) {
			// set up a basic logging configuration

			BasicConfigurator.resetConfiguration();
			BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));

			// initialise blackboard
			initBlackboard();
			
			//add a new location for the data flow system
			ModelLocation systemLocation = new ModelLocation(SYSTEM_ID, null);
			
			
			
			SequentialJob sequence = new SequentialJob();
			
			sequence.add(new SystemModelJob(usageLocation, allocLocation, characLocation, systemLocation));

			Workflow myWorkflow = new Workflow(sequence);
			myWorkflow.run();

		}
	}

	/**
	 * 
	 * @param usageModelURI
	 * @param allocModelURI
	 * @param characModelURI
	 */
	public void setURIs(URI usageModelURI, URI allocModelURI, URI characModelURI) {
		this.usageLocation = new ModelLocation(USAGE_ID, usageModelURI);
		this.allocLocation = new ModelLocation(ALLOC_ID, allocModelURI);
		this.characLocation = new ModelLocation(CHARAC_ID, characModelURI);

	}

	/**
	 * 
	 */
	private void initBlackboard() {
		// TODO add to loop?
		AnalysisPartition usagePartition = new AnalysisPartition();
		usagePartition.loadModel(usageLocation.getModelID());
		usagePartition.resolveAllProxies();
		myBlackboard.addPartition(USAGE_ID, usagePartition);

		AnalysisPartition allocPartition = new AnalysisPartition();
		allocPartition.loadModel(allocLocation.getModelID());
		allocPartition.resolveAllProxies();
		myBlackboard.addPartition(ALLOC_ID, allocPartition);

		AnalysisPartition characPartition = new AnalysisPartition();
		characPartition.resolveAllProxies();
		characPartition.loadModel(characLocation.getModelID());
		myBlackboard.addPartition(CHARAC_ID, characPartition);
	}
}
