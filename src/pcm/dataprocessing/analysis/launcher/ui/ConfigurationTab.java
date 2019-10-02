package pcm.dataprocessing.analysis.launcher.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import pcm.dataprocessing.analysis.launcher.constants.Constants;
import de.uka.ipd.sdq.workflow.launchconfig.tabs.TabHelper;

/**
 * Main configuration tab for the launch configuration
 * 
 * @author Mirko Sowa
 *
 */
public class ConfigurationTab extends AbstractLaunchConfigurationTab {

	private Composite comp;

	private Text usageText;
	private Text allocText;
	private Text chText;

	
	private Combo prologCombo;
	private Combo analysisCombo;

	private Group analysisGroup;
	private Group prologGroup;

	public ConfigurationTab() {

	}

	@Override
	public String getName() {
		return Constants.NAME.getConstant();
	}

	@Override
	public String getMessage() {
		return "Please select specified files.";
	}

	@Override
	public boolean isValid(final ILaunchConfiguration launchConfig) {
		return true;

	}

	@Override
	public boolean canSave() {
		return !usageText.getText().isEmpty() && !allocText.getText().isEmpty() && !chText.getText().isEmpty();
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), 0);
		configuration.setAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), 0);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		usageText.setText("");
		allocText.setText("");
		chText.setText("");
		prologCombo.select(0);
		analysisCombo.select(0);
		try {
			usageText.setText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
			allocText.setText(configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
			chText.setText(configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));
			analysisCombo.select(configuration.getAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), 0));
			prologCombo.select(configuration.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), 0));

		} catch (CoreException e) {
			// ignored
		}

	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), usageText.getText());
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), allocText.getText());
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), chText.getText());
		configuration.setAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), analysisCombo.getSelectionIndex());
		configuration.setAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), prologCombo.getSelectionIndex());
	}

	@Override
	public void createControl(Composite parent) {

		final ModifyListener modifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
			//	ConfigurationTab.this.setDirty(true);
			//	ConfigurationTab.this.updateLaunchConfigurationDialog();
			}
		};

	
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));

		/* Usage Model */
		
		usageText = new Text(comp, SWT.BORDER);
		usageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		TabHelper.createFileInputSection(comp, modifyListener, Constants.USAGE_MODEL_LABEL.getConstant(),
				new String[] { "*.usagemodel" }, usageText, Display.getCurrent().getActiveShell(), "");

		/* Allocation-Model */
		
		allocText = new Text(comp, SWT.BORDER);
		allocText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		TabHelper.createFileInputSection(comp, modifyListener, Constants.ALLOCATION_MODEL_LABEL.getConstant(),
				new String[] { "*.allocation" }, allocText, Display.getCurrent().getActiveShell(), "");

		/* Characteristics-Type-Model */
	
		chText = new Text(comp, SWT.BORDER);
		chText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		TabHelper.createFileInputSection(comp, modifyListener, Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(),
				new String[] { "*.xmi" }, chText, Display.getCurrent().getActiveShell(), "");
		
		/* Analysis Goal */

		analysisGroup = new Group(comp, SWT.NONE);
		analysisGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		analysisGroup.setText(Constants.ANALYSIS_GOAL_LABEL.getConstant());
		analysisGroup.setLayout(new GridLayout(1, true));

		analysisCombo = new Combo(analysisGroup, SWT.DROP_DOWN);
		analysisCombo.add(Constants.ANALYSIS_GOAL_ONE.getConstant());
		analysisCombo.add(Constants.ANALYSIS_GOAL_TWO.getConstant());
		analysisCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		/* Prolog Interpreter */
		
		prologGroup = new Group(comp, SWT.NONE);
		prologGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prologGroup.setText(Constants.PROLOG_INTERPRETER_LABEL.getConstant());
		prologGroup.setLayout(new GridLayout(1, true));

		prologCombo = new Combo(prologGroup, SWT.DROP_DOWN);
		prologCombo.add(Constants.PROLOG_INTERPRETER_ONE.getConstant());
		prologCombo.add(Constants.PROLOG_INTERPRETER_TWO.getConstant());
		prologCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		setControl(comp);

	}

}
