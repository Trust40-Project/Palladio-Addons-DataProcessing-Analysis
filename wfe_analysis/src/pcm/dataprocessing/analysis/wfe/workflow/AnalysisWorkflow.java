package pcm.dataprocessing.analysis.wfe.workflow;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;
import pcm.dataprocessing.analysis.wfe.workflow.job.EvaluateModelJob;
import pcm.dataprocessing.analysis.wfe.workflow.job.SystemModelJob;

/**
 * 
 * @author Mirko Sowa
 *
 */
public class AnalysisWorkflow {

	ModelLocation usageLocation = null;
	ModelLocation allocLocation = null;
	ModelLocation characLocation = null;

	
	private static final String SYSTEM_ID = "systemID";


	private AnalysisBlackboard myBlackboard = new AnalysisBlackboard();

	public AnalysisWorkflow(AnalysisWorkflowConfig config) {
		
	}
	/**
	 * 
	 */
	public void launch() {
		if (usageLocation.getModelID() != null && allocLocation.getModelID() != null
				&& characLocation.getModelID() != null) {
			// TODO  set up a basic logging configuration?

			BasicConfigurator.resetConfiguration();
			BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));

			// initialise blackboard
			initBlackboard();
			
			//add a new location for the data flow system
			ModelLocation systemLocation = new ModelLocation(SYSTEM_ID, null);
					
			
			SequentialJob sequence = new SequentialJob();
			
			sequence.add(new SystemModelJob(usageLocation, allocLocation, characLocation, systemLocation));
			sequence.add(new EvaluateModelJob());

			Workflow myWorkflow = new Workflow(sequence);
			myWorkflow.run();

		}
	}


	/**
	 * 
	 */
	private void initBlackboard() {
		addToBlackboard(allocLocation);
		addToBlackboard(characLocation);
		addToBlackboard(usageLocation);
	}
	/**
	 * 
	 * @param loc
	 */
	private void addToBlackboard(ModelLocation loc) {
		ResourceSetPartition part = new ResourceSetPartition();
		part.loadModel(loc.getModelID());
		part.resolveAllProxies();
		myBlackboard.addPartition(loc.getPartitionID(), part);

	}
}
