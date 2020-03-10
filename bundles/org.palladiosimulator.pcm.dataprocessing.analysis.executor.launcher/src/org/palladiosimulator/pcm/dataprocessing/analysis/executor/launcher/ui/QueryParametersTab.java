package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;

import com.google.common.base.Strings;

public class QueryParametersTab extends AbstractLaunchConfigurationTab implements Consumer<IQuery> {

	private final InitTaskExecutor initTaskExecutor = new InitTaskExecutor();
	private final ModifyListener modifyListener;
	private final Map<String, Text> parameterNameToTextfield = new HashMap<>(); 
	private IQuery query;
	private Composite rootComposite;
	
	public QueryParametersTab() {
		this.modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!initTaskExecutor.isInitTaskRunning()) {
					setDirty(true);
					updateLaunchConfigurationDialog();					
				}
			}
		};
	}
	
	@Override
	public void createControl(Composite parent) {
		rootComposite = new Composite(parent, SWT.NONE);
		fillRootComposite(rootComposite);
		setControl(rootComposite);
	}

	private void fillRootComposite(Composite rootComposite) {
		// clear composite
		Arrays.asList(rootComposite.getChildren()).forEach(Widget::dispose);
		rootComposite.layout(true);
		
		// set grid layout
		GridLayout layout = new GridLayout();
		rootComposite.setLayout(layout);
		setControl(rootComposite);
		
		// parameters
		List<String> paramNames = Optional.ofNullable(query).map(IQuery::getParameters).map(Map::keySet).map(ArrayList::new).map(l -> (List<String>)l).orElse(Collections.emptyList());
		paramNames.sort(String::compareTo);
		parameterNameToTextfield.clear();
		for (String paramName : paramNames) {
			
			final Group paramGroup = new Group(rootComposite, SWT.NONE);
			final GridLayout paramGroupLayout = new GridLayout();
			paramGroupLayout.numColumns = 1;
			paramGroup.setLayout(paramGroupLayout);
			paramGroup.setText(paramName); // The group name
			paramGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false));
			Text paramTextField = new Text(paramGroup, SWT.BORDER);
			final GridData gridDataTextParam = new GridData(SWT.FILL,
					SWT.CENTER, true, false);
			gridDataTextParam.widthHint = 200;
			paramTextField.setLayoutData(gridDataTextParam);
			paramTextField.addModifyListener(modifyListener);
			parameterNameToTextfield.put(paramName, paramTextField);
		}
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		initTaskExecutor.runInitTask(() -> {
			for (String paramName : parameterNameToTextfield.keySet()) {
				configuration.setAttribute(paramName, "");
			}
		});
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		initTaskExecutor.runInitTask(() -> {
			for (String paramName : parameterNameToTextfield.keySet()) {
				String paramValue = "";
				try {
					paramValue = configuration.getAttribute(paramName, "");
				} catch (CoreException e) {
					// ignore problem
				}
				if (!Strings.isNullOrEmpty(paramValue)) {
					parameterNameToTextfield.get(paramName).setText(paramValue);
				}
			}
		});
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		for (String paramName : parameterNameToTextfield.keySet()) {
			configuration.setAttribute(paramName, parameterNameToTextfield.get(paramName).getText());
		}
	}

	@Override
	public String getName() {
		return "Query Parameters";
	}

	@Override
	public void accept(IQuery query) {
		this.query = query;
		fillRootComposite((Composite)getControl());
	}

}
