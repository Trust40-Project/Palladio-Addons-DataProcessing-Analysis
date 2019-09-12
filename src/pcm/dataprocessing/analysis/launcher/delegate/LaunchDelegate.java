package pcm.dataprocessing.analysis.launcher.delegate;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import pcm.dataprocessing.analysis.launcher.constants.Constants;
/**
 * Simple launch delegate used for testing purposes.
 * Prints a String when called.
 * @author mirko
 *
 */
public class LaunchDelegate implements ILaunchConfigurationDelegate {

	public LaunchDelegate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		System.out.println("Hello there! :)" 
			+ configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), "failed to submit"));
	}

}
