package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.delegate;

import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflow;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflowConfig;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.jobs.SequentialJob;

/**
 * Executes an {@link AnalysisWorkflow} with an {@link AnalysisBlackboard} and
 * uses an {@link OutputJob} to show its results.
 * 
 * @author mirko
 *
 */
public class WorkflowExecuter {
	private AnalysisWorkflowConfig wfeConfig;
	private AnalysisBlackboard blackboard;

	public WorkflowExecuter(AnalysisWorkflowConfig wfeConfig) {
		this.wfeConfig = wfeConfig;
		this.blackboard = new AnalysisBlackboard();

	}

	public void execute() {
		SequentialJob combinedJob = new SequentialJob();
		AnalysisWorkflow analysisWorkflow = new AnalysisWorkflow(wfeConfig);
		analysisWorkflow.setBlackboard(blackboard);
		combinedJob.add(analysisWorkflow);

		OutputJob outs = new OutputJob();
		outs.setBlackboard(blackboard);
		combinedJob.add(outs);

		Workflow w = new Workflow(combinedJob);
		w.run();
	}

}
