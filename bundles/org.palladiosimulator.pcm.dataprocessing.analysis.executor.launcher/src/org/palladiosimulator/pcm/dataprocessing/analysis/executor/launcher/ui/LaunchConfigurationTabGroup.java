
package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * Parent Group of the tab configuration.
 * 
 * @author Mirko Sowa
 *
 */
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	/**
	 * Opens to tabs, {@link ModelInputTab} (the main tab) and a {@link TranslatorSettingsTab}
	 */
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		QueryChangeSubject querySubject = new QueryChangeSubject();
		QueryParametersTab queryParameterTab = new QueryParametersTab();
		querySubject.addListener(queryParameterTab);
		setTabs(new ILaunchConfigurationTab[] { new ModelInputTab(querySubject), queryParameterTab, new TranslatorSettingsTab() });
	}

}
