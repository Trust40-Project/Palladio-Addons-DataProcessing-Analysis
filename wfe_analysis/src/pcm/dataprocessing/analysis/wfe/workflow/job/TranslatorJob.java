package pcm.dataprocessing.analysis.wfe.workflow.job;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
/**
 * 
 * @author mirko
 *
 */
public class TranslatorJob implements IJob {

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
