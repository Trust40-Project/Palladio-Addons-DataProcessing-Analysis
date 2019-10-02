/**
 * 
 */
package pcm.dataprocessing.analysis.launcher.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * Parent Group of the tab configuration.
 * @author Mirko Sowa
 *
 */
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	/**
	 * Opens to tabs, configurationTab (Main Tab) and a common tab.
	 */
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        setTabs(new ILaunchConfigurationTab[] { new ConfigurationTab(), new CommonTab() });

	}

}
