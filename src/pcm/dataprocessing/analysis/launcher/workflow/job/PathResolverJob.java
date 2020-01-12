package pcm.dataprocessing.analysis.launcher.workflow.job;

import java.io.File;
import java.net.MalformedURLException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * 
 * @author mirko
 *
 */
public class PathResolverJob implements IJob {
	private String path = "";

	public PathResolverJob(String path) {
		this.path = path;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		try {

			URI uriFromText = getUriFromText(path); //TODO return to workflow

		} catch (MalformedURLException e) {
			throw new JobFailedException("Specified URI does not exist");
		}
	}

	private URI getUriFromText(String text) throws MalformedURLException {

		URI result;
		File usageFile = new File(text);
		if (usageFile != null && usageFile.exists()) {
			result = URI.createFileURI(usageFile.getAbsolutePath());
		} else {
			result = URI.createURI(text);
		}
		return result;

	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		path = "";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Paths Resolver Job";
	}

}
