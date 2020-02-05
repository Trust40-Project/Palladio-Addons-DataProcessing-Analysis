package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformatorFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;

/**
 * 
 * @author mirko
 *
 */
public class SystemModelJob extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

	private AnalysisBlackboard blackboard = null;

	private UsageModel usageModel = null;
	private Allocation allocModel = null;
	private CharacteristicTypeContainer characModel = null;

	/**
	 * 
	 * @param usageLoc
	 * @param allocLoc
	 * @param characLoc
	 * @param goal
	 */
	public SystemModelJob(ModelLocation usageLoc, ModelLocation allocLoc, ModelLocation characLoc,
			ModelLocation goal) {
			usageModel = (UsageModel) blackboard.getPartition(usageLoc.getPartitionID())
					.getFirstContentElement(usageLoc.getModelID());
			allocModel = (Allocation) blackboard.getPartition(allocLoc.getPartitionID())
					.getFirstContentElement(allocLoc.getModelID());
			characModel = (CharacteristicTypeContainer) blackboard.getPartition(characLoc.getPartitionID())
					.getFirstContentElement(characLoc.getModelID());
			//this.destination = blackboard.getPartition(goal.getPartitionID());
	}
	

	@Override
	public void setBlackboard(AnalysisBlackboard blackboard) {
		this.blackboard = blackboard;

	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		if (usageModel != null && allocModel != null && characModel != null) {
			ITransformator myTransformator = ITransformatorFactory.getInstance().create();
			blackboard.setDataFlowSystemModel(myTransformator.transform(usageModel, allocModel, characModel));
		} else
			throw new JobFailedException("Could not transform models");
	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		// TODO what's to cleanup?

	}

	@Override
	public String getName() {
		return "Get System from Models";
	}

}
