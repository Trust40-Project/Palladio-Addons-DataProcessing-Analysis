/**
 * 
 */
package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System;
import org.prolog4j.Prover;
import org.prolog4j.Query;
import org.prolog4j.Solution;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * @author Mirko Sowa
 *
 */
public class EvaluateModelJob extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

	private AnalysisBlackboard blackboard = null;

	public EvaluateModelJob() {
		super("Evaluate translated system model with a prover and dedicated goal.");
	}

	@Override
	public void setBlackboard(AnalysisBlackboard blackboard) {
		this.blackboard = blackboard;

	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		System dataFlowSystem = blackboard.getDataFlowSystemModel();
		String testingCode = blackboard.getSystemTranslator().translate(dataFlowSystem).getCode();

		Prover myProver = blackboard.getProverFactory().createProver();
		myProver.addTheory(testingCode);
		myProver.addTheory(blackboard.getQuery().getAdditionalTheory());

		Query myQuery = myProver.query(blackboard.getQuery().getQueryString());
		Solution<Object> solution = myQuery.solve();

		if (solution.isSuccess()) {
			blackboard.setSolution(solution);

		}
	}

}
