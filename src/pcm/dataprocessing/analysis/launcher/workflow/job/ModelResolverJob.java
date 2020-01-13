package pcm.dataprocessing.analysis.launcher.workflow.job;

import java.io.IOException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * 
 * @author mirko
 *
 */
public class ModelResolverJob implements IBlackboardInteractingJob<Blackboard<Object>> {
	private URI uri = null;
	private Blackboard<Object> blackboard = null;
	private String key = "";

	public ModelResolverJob(Blackboard<Object> blackboard, String key) {
		this.blackboard = blackboard;
		this.key = key;
		uri = (URI) this.blackboard.getPartition(key);
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		if (uri.hasPath()) {

			this.blackboard.removePartition(key);
			this.blackboard.addPartition(key, "OK");
			System.out.println("ok");
		} else {
			ResourceSet rs = new ResourceSetImpl();
			Resource resource = rs.createResource(uri);

			try {
				resource.load(null);

			} catch (IOException exception) {
				throw new JobFailedException("An IO-Exception occured.");
			}
			Object o = resource.getContents().get(0);

			EcoreUtil.resolveAll(rs);

			this.blackboard.removePartition(key);
			this.blackboard.addPartition(key, o);
		}
	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		uri = null;
		key = null;
	}

	@Override
	public String getName() {
		return "Model Resolver Job";
	}

	@Override
	public void setBlackboard(Blackboard<Object> blackboard) {

	}

}
