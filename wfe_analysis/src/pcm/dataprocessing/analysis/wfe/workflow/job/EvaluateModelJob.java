/**
 * 
 */
package pcm.dataprocessing.analysis.wfe.workflow.job;

import java.util.Map.Entry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.prolog4j.Prover;
import org.prolog4j.Query;
import org.prolog4j.Solution;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import pcm.dataprocessing.analysis.wfe.workflow.AnalysisBlackboard;

/**
 * @author mirko
 *
 */
public class EvaluateModelJob extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

	private AnalysisBlackboard blackboard = null;

	public EvaluateModelJob() {

	}

	@Override
	public void setBlackboard(AnalysisBlackboard blackboard) {
		this.blackboard = blackboard;

	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {

		String testingCode = blackboard.getSystemTranslator().translate(blackboard.getDataFlowSystemModel()).getCode();

		Prover myProver = blackboard.getProverFactory().createProver();
		myProver.addTheory(testingCode);

		Query myQuery = myProver.query(blackboard.getQuery().getQuery()); // TODO refractor method name?
		Solution<Object> solution = myQuery.solve();
		// MessageConsole myConsole = findConsole(Constants.CONSOLE_ID.getConstant());
		// MessageConsoleStream out = myConsole.newMessageStream();

		if (!solution.isSuccess()) {
			// out.println("Query solution had success: " + solution.isSuccess());
		} else {

			for (Entry<String, String> t : blackboard.getQuery().getResultVars().entrySet()) {
				// out.println(solution.get(t.getValue()));
			}

		}
	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		// TODO what's to cleanup?

	}

	@Override
	public String getName() {
		return "Evaluate translated system model with a prover and dedicated goal.";
	}

}
