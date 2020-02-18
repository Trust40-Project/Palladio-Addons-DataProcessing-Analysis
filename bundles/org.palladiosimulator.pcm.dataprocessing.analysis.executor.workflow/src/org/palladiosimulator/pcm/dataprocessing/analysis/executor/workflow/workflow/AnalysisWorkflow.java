package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job.EvaluateModelJob;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job.SystemModelJob;
import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.WorkflowExceptionHandler;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;

/**
 * This workflow enables to evaluate models as to a query goal.
 * 
 * @author Mirko Sowa
 *
 */
public class AnalysisWorkflow extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

	private static final String NAME = "AnalysisWorkflow";

	private final ModelLocation usageLocation;
	private final ModelLocation allocLocation;
	private final ModelLocation characLocation;

	private IProgressMonitor myMonitor;

	private static final String SYSTEM_ID = "systemID";

	/**
	 * Constructor for an AnalysisWorkflow, takes an AnalysisWorkflowConfig and a
	 * IProgressMonitor as parameter. Also set the blackboard.
	 * 
	 * @param config  {@link AnalysisWorkflow} encapsulates most of the attributes
	 *                needed for a launch
	 * @param monitor {@link IProgressMonitor} to track progress and end of this
	 *                workflow
	 */
	public AnalysisWorkflow(AnalysisWorkflowConfig config, IProgressMonitor monitor) {
		super(NAME);
		this.usageLocation = config.getUsageLocation();
		this.allocLocation = config.getAllocLocation();
		this.characLocation = config.getCharacLocation();
		this.myMonitor = monitor;
	}

	/**
	 * Method to launch the Workflow
	 */
	public void launch() {
		if (usageLocation.getModelID() != null && allocLocation.getModelID() != null
				&& characLocation.getModelID() != null) {

			
			// TODO set up a basic logging configuration?
			// BasicConfigurator.resetConfiguration();
			// BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));

			// initialise blackboard
			if (this.getBlackboard() == null) {
				this.setBlackboard(new AnalysisBlackboard());
			}
			initBlackboard();

			// add a new location for the data flow system
			ModelLocation systemLocation = new ModelLocation(SYSTEM_ID, null);

			// creates a new sequence of jobs
			// SequentialJob sequence = new SequentialJob();

			SystemModelJob sysJob = new SystemModelJob(usageLocation, allocLocation, characLocation, systemLocation);
			sysJob.setBlackboard(getBlackboard());
			this.add(sysJob);
			EvaluateModelJob evalJob = new EvaluateModelJob();
			evalJob.setBlackboard(getBlackboard());
			this.add(evalJob);

			Workflow myWorkflow = new Workflow(this, myMonitor, new WorkflowExceptionHandler(false));
			// executes sequence
			myWorkflow.run();

		}
	}

	/**
	 * Initialises the blackboard with the model locations
	 */
	private void initBlackboard() {
		addToBlackboard(allocLocation);
		addToBlackboard(characLocation);
		addToBlackboard(usageLocation);
	}

	/**
	 * Adds a model location to the blackboard
	 * 
	 * @param loc ModelLocation to be added
	 */
	private void addToBlackboard(ModelLocation loc) {
		ResourceSetPartition part = new ResourceSetPartition();
		Resource r = part.loadModel(loc.getModelID());
		part.resolveAllProxies();
		getBlackboard().addPartition(loc.getPartitionID(), part); // FIXME, Model not loaded yet);

	}
}
