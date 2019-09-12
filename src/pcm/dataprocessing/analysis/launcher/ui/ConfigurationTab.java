package pcm.dataprocessing.analysis.launcher.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import pcm.dataprocessing.analysis.launcher.constants.Constants;

/**
 * Main Tab for the launch configuration
 * 
 * @author mirko
 *
 */
public class ConfigurationTab extends AbstractLaunchConfigurationTab {

	private Composite comp;

	private Group usageGroup;
	private Group allocGroup;
	private Group chGroup;
	private Group analysisGroup;
	private Group prologGroup;

	private Text usageText;
	private Text allocText;
	private Text chText;

	private Combo prologCombo;
	private Combo analysisCombo;

	private Button usageBrowseBtn;
	private Button allocBrowseBtn;
	private Button chBrowseBtn;

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

		try {
			// TODO check if filetype is valid and all required fields are not empty

		} catch (Exception e) {
			setErrorMessage("Invalid file selected.");
		}

		return true;
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), "");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		usageText.setText("");
		allocText.setText("");
		chText.setText("");
		prologCombo.select(0);
		analysisCombo.select(0);

		// TODO initialise with config attributes

	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), usageText.getText());
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), allocText.getText());
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), chText.getText());
	}

	@Override
	public void createControl(Composite parent) {

		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));

		/* Usage Model */

		usageGroup = new Group(comp, SWT.NONE);
		usageGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		usageGroup.setText(Constants.USAGE_MODEL_LABEL.getConstant());
		usageGroup.setLayout(new GridLayout(2, false));

		usageText = new Text(usageGroup, SWT.BORDER);
		usageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		usageBrowseBtn = new Button(usageGroup, SWT.NONE);
		usageBrowseBtn.setText(Constants.BUTTON_BROWSE_TEXT.getConstant());
		usageBrowseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
				dlg.setFilterNames(new String[] { "Encore Model (*.ecore)" });

				dlg.setFilterExtensions(new String[] { "*.ecore" });
				String fileName = dlg.open();
				if (fileName != null) {
					usageText.setText(fileName);
				}

			}
		});

		/* Allocation-Model */

		allocGroup = new Group(comp, SWT.NONE);
		allocGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		allocGroup.setText(Constants.ALLOCATION_MODEL_LABEL.getConstant());
		allocGroup.setLayout(new GridLayout(2, false));

		allocText = new Text(allocGroup, SWT.BORDER);
		allocText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		allocBrowseBtn = new Button(allocGroup, SWT.NONE);
		allocBrowseBtn.setText(Constants.BUTTON_BROWSE_TEXT.getConstant());
		allocBrowseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
				dlg.setFilterNames(new String[] { "Encore Model (*.ecore)" });

				dlg.setFilterExtensions(new String[] { "*.ecore" });
				String fileName = dlg.open();
				if (fileName != null) {
					allocText.setText(fileName);
				}

			}
		});

		/* Characteristics-Type-Model */

		chGroup = new Group(comp, SWT.NONE);
		chGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		chGroup.setText(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant());
		chGroup.setLayout(new GridLayout(2, false));

		chText = new Text(chGroup, SWT.BORDER);
		chText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		chBrowseBtn = new Button(chGroup, SWT.NONE);
		chBrowseBtn.setText(Constants.BUTTON_BROWSE_TEXT.getConstant());
		chBrowseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
				dlg.setFilterNames(new String[] { "Encore Model (*.ecore)" });

				dlg.setFilterExtensions(new String[] { "*.ecore" });
				String fileName = dlg.open();
				if (fileName != null) {
					chText.setText(fileName);
				}

			}
		});

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
