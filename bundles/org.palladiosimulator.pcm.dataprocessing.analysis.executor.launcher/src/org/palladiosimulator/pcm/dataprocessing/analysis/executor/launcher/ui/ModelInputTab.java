package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.ui;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.Activator;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.constants.Constants;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.QueryInformation;
import org.prolog4j.IProverFactory;
import org.prolog4j.ProverInformation;

import de.uka.ipd.sdq.workflow.launchconfig.tabs.TabHelper;

/**
 * Main configuration tab for the launch configuration
 * 
 * @author Mirko Sowa
 *
 */
public class ModelInputTab extends AbstractLaunchConfigurationTab {

	private Composite comp;

	private Text usageText;
	private Text allocText;
	private Text chText;

	private Combo prologCombo;
	private Combo analysisCombo;

	private Group analysisGroup;
	private Group prologGroup;

	private Map<ProverInformation, IProverFactory> proversMap;
	private Map<QueryInformation, IQuery> queryMap;

	public ModelInputTab() {
		Activator sharedInstance = Activator.getInstance();
		if (sharedInstance != null) {
			proversMap = sharedInstance.getProverManagerInstance().getProvers();
			queryMap = sharedInstance.getQueryManagerInstance().getQueries();
		}
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

		return !usageText.getText().isEmpty() && !allocText.getText().isEmpty() && !chText.getText().isEmpty()
				&& analysisCombo.getSelectionIndex() != -1 && prologCombo.getSelectionIndex() != -1
				&& isURIexistent(usageText.getText()) && isURIexistent(allocText.getText())
				&& isURIexistent(chText.getText());

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), "");
		configuration.setAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(),
				Constants.DEFAULT_CONFIG_VALUE.getConstant()); // Saving id of
																// QueryInformation
		configuration.setAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(),
				Constants.DEFAULT_CONFIG_VALUE.getConstant()); // Saving the ID of the
		// ProverInformation
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		usageText.setText("");
		allocText.setText("");
		chText.setText("");
		analysisCombo.select(0);
		prologCombo.select(0);

		try {
			usageText.setText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
			allocText.setText(configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
			chText.setText(configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));

			String analysisConfig = configuration.getAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(),
					Constants.DEFAULT_CONFIG_VALUE.getConstant());

			if (!analysisConfig.equals(Constants.DEFAULT_CONFIG_VALUE.getConstant())) {
				for (Entry<QueryInformation, IQuery> entry : queryMap.entrySet()) {
					if ((entry.getKey().getId()).equals(analysisConfig)) {
						analysisCombo.select(analysisCombo.indexOf(entry.getKey().getName()));
					}
					;
				}
			}

			String prologCongfig = configuration.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(),
					Constants.DEFAULT_CONFIG_VALUE.getConstant());

			if (!prologCongfig.equals(Constants.DEFAULT_CONFIG_VALUE.getConstant())) {
				for (Map.Entry<ProverInformation, IProverFactory> entry : proversMap.entrySet()) {
					if ((entry.getKey().getId()).equals(prologCongfig)) {
						prologCombo.select(prologCombo.indexOf(entry.getKey().getName()));
					}
					;
				}
			}

		} catch (CoreException e) {
			// ignored
		}

	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), usageText.getText());
		configuration.setAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), allocText.getText());
		configuration.setAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), chText.getText());

		String analysisName = analysisCombo.getItem(analysisCombo.getSelectionIndex());
		String analysisId = (String) analysisCombo.getData(analysisName);

		for (Entry<QueryInformation, IQuery> entry : queryMap.entrySet()) {
			if ((entry.getKey().getId()).equals(analysisId)) {
				configuration.setAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), entry.getKey().getId());
			}
		}

		String prologName = prologCombo.getItem(prologCombo.getSelectionIndex());
		String prologId = (String) prologCombo.getData(prologName);

		for (Map.Entry<ProverInformation, IProverFactory> entry : proversMap.entrySet()) {
			if ((entry.getKey().getId()).equals(prologId)) {
				configuration.setAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), entry.getKey().getId());
			}
		}

	}

	@Override
	public void createControl(Composite parent) {

		/* Modify listener for text input changes, sets dirty */
		final ModifyListener modifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}

		};

		comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		comp.setLayout(layout);
		setControl(comp);
		
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

		/* Selection Listener for Combo boxes, sets dirty */

		final SelectionListener selectionListener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		};
		/* Analysis Goal */

		analysisGroup = new Group(comp, SWT.NONE);
		analysisGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		analysisGroup.setText(Constants.ANALYSIS_GOAL_LABEL.getConstant());
		analysisGroup.setLayout(layout);

		analysisCombo = new Combo(analysisGroup, SWT.DROP_DOWN);

		for (Entry<QueryInformation, IQuery> entry : queryMap.entrySet()) {
			String s = entry.getKey().getName();
			analysisCombo.add(s);
			analysisCombo.setData(s, entry.getKey().getId());
		}

		analysisCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		analysisCombo.addSelectionListener(selectionListener);
		analysisCombo.clearSelection();

		/* Prolog Interpreter */

		prologGroup = new Group(comp, SWT.NONE);
		prologGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prologGroup.setText(Constants.PROLOG_INTERPRETER_LABEL.getConstant());
		prologGroup.setLayout(layout);

		prologCombo = new Combo(prologGroup, SWT.DROP_DOWN);

		for (Map.Entry<ProverInformation, IProverFactory> entry : proversMap.entrySet()) {
			String s = entry.getKey().getName();
			prologCombo.add(s);
			prologCombo.setData(s, entry.getKey().getId());
		}

		prologCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prologCombo.addSelectionListener(selectionListener);
	}
	/**
	 * Checks if a given URI is existent in the file system
	 * @param s String of the URI
	 * @return true if URI exists, else false.
	 */
	private boolean isURIexistent(String s) {
		URIConverter uriConverter = new ResourceSetImpl().getURIConverter();
		URI uriFromText = URI.createURI(usageText.getText());
		if (uriConverter.exists(uriFromText, null)) {
			return true;
		}
		uriFromText = null;
		File usageFile = new File(s);
		if (usageFile != null && usageFile.exists()) {
			uriFromText = URI.createFileURI(usageFile.getAbsolutePath());
			if (uriConverter.exists(uriFromText, null)) {
				return true;
			}
		}
		return false;
	}

}
