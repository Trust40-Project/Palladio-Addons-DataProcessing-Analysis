package pcm.dataprocessing.analysis.launcher.workflow.job;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

/**
 * 
 * @author mirko
 *
 */
public class ModelResolverJob implements IJob {
	private URI uri = null;

	public ModelResolverJob(URI uri) {
		this.uri = uri;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		ResourceSet rs = new ResourceSetImpl();
		Resource resource = rs.createResource(uri);
		

		try {
			resource.load(null);

		} catch (IOException exception) {
			throw new JobFailedException("An IO-Exception occured.");
		}
		resource.getContents().get(0); //TODO return to workflow
		
		EcoreUtil.resolveAll(rs);
	}

	@Override
	public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
		uri = null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Model Resolver Job";
	}

}
