package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformatorFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;

/**
 * Gets the SystemMode from the usageModel , allocationModel and
 * CharacteristicsType.
 * 
 * @author mirko
 *
 */
public class SystemModelJob extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

	private AnalysisBlackboard blackboard = null;

	private final ModelLocation usageLoc;
	private final ModelLocation allocLoc;
	private final ModelLocation characLoc;

	private UsageModel usageModel = null;
	private Allocation allocModel = null;
	private CharacteristicTypeContainer characModel = null;

	private final String partitionID;

	/**
	 * 
	 * @param usageLoc
	 * @param allocLoc
	 * @param characLoc
	 * @param goal
	 */
	public SystemModelJob(ModelLocation usageLoc, ModelLocation allocLoc, ModelLocation characLoc) {
		super("Get System from Models");
		this.partitionID = usageLoc.getPartitionID();
		this.usageLoc = usageLoc;
		this.allocLoc = allocLoc;
		this.characLoc = characLoc;
	}

	@Override
	public void setBlackboard(AnalysisBlackboard blackboard) {
		this.blackboard = blackboard;
		usageModel = (UsageModel) blackboard.getPartition(partitionID).getFirstContentElement(usageLoc.getModelID());
		allocModel = (Allocation) blackboard.getPartition(partitionID).getFirstContentElement(allocLoc.getModelID());
		characModel = (CharacteristicTypeContainer) blackboard.getPartition(partitionID)
				.getFirstContentElement(characLoc.getModelID());

	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

		if (usageModel != null && allocModel != null && characModel != null) {
			ITransformator myTransformator = ITransformatorFactory.getInstance().create();
			org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System transformed = myTransformator
					.transform(usageModel, allocModel, characModel);
			blackboard.setDataFlowSystemModel(transformed);
		} else
			throw new JobFailedException("Could not transform models");
	}

}
