package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.delegate;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflow;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflowConfig;

public class WorkflowExecuter {
	private AnalysisWorkflowConfig wfeConfig;
	private AnalysisBlackboard blackboard;
	private IProgressMonitor progressMonitor = null;

	public WorkflowExecuter(AnalysisWorkflowConfig wfeConfig) {
		this.wfeConfig = wfeConfig;
		this.blackboard = new AnalysisBlackboard();

	}

	public void execute() {
		AnalysisWorkflow analysisWorkflow = new AnalysisWorkflow(wfeConfig, progressMonitor); //FIXME PROGRESSMONITOR is null
		analysisWorkflow.setBlackboard(blackboard);
		analysisWorkflow.launch();
		analysisWorkflow.add(new OutputJob());

	}

}
