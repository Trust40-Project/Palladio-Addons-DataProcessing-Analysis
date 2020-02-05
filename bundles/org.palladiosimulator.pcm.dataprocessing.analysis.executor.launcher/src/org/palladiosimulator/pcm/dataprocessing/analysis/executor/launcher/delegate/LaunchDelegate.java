package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.delegate;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.Activator;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.constants.Constants;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.QueryInformation;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.impl.IQueryManager;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflow;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisWorkflowConfig;
import org.prolog4j.IProverFactory;
import org.prolog4j.ProverInformation;
import org.prolog4j.manager.IProverManager;

import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;

/**
 * Launches a given launch configuration with an usage model,an allocation model
 * and a characteristics model.
 * 
 * @author Mirko Sowa
 * 
 */
public class LaunchDelegate implements ILaunchConfigurationDelegate {

	private URI usageModelPath = null;
	private URI allocModelPath = null;
	private URI chModelPath = null;

	org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = null;
	SystemTranslator sysTranslator = null;

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		IProfileRegistry.eINSTANCE.getClass();

		boolean returnValueIndexing = configuration.getAttribute(Constants.ADV_ARG_AND_RETURN.getConstant(), false);
		boolean optimNegation = configuration.getAttribute(Constants.ADV_OPTIM_NEGATION.getConstant(), false);
		boolean shortAssign = configuration.getAttribute(Constants.ADV_SHORT_ASSIGN.getConstant(), false);

		resolvePaths(configuration);

		AnalysisWorkflowConfig wfeConfig = new AnalysisWorkflowConfig(usageModelPath, allocModelPath, chModelPath,
				getAnalysisGoal(configuration), getProverFactory(configuration), returnValueIndexing, optimNegation,
				shortAssign);

		AnalysisWorkflow analysisWorkflow = new AnalysisWorkflow(wfeConfig);

		analysisWorkflow.launch();

	}

	/**
	 * 
	 * @param configuration
	 * @throws CoreException
	 */
	private void resolvePaths(ILaunchConfiguration configuration) throws CoreException {

		try {
			usageModelPath = getUriFromText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
			allocModelPath = getUriFromText(
					configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
			chModelPath = getUriFromText(
					configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));

		} catch (MalformedURLException e) {
			throw new CoreException(
					new Status(IStatus.ERROR, "pcm.dataprocessing.analysis.launcher", "Could not resolve paths."));
		}

	}

	/*
	 * private void resolveModels() throws CoreException {
	 * 
	 * ResourceSet rs = new ResourceSetImpl(); Resource usageResource =
	 * rs.createResource(usageModelPath); Resource allocationResource =
	 * rs.createResource(allocModelPath); Resource charTypeResource =
	 * rs.createResource(chModelPath);
	 * 
	 * try { usageResource.load(null); allocationResource.load(null);
	 * charTypeResource.load(null); } catch (IOException exception) { throw new
	 * CoreException( new Status(IStatus.ERROR,
	 * "pcm.dataprocessing.analysis.launcher", "Could not resolve ressource.")); }
	 * usageModel = (UsageModel) usageResource.getContents().get(0); allocationModel
	 * = (Allocation) allocationResource.getContents().get(0); charTypeContainer =
	 * (CharacteristicTypeContainer) charTypeResource.getContents().get(0);
	 * 
	 * EcoreUtil.resolveAll(rs);
	 * 
	 * }
	 */

	/**
	 * 
	 * @param launchConfig
	 * @return
	 * @throws CoreException
	 */
	private IProverFactory getProverFactory(ILaunchConfiguration launchConfig) throws CoreException {
		IProverManager proverManager = Activator.getInstance().getProverManagerInstance();
		IProverFactory myProverFactory = null;

		String prologConfig = launchConfig.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), "default");

		for (Map.Entry<ProverInformation, IProverFactory> entry : proverManager.getProvers().entrySet()) {
			if (entry.getKey().getId().equals(prologConfig)) {
				myProverFactory = entry.getValue();
			}
		}

		return myProverFactory;

	}

	/**
	 * 
	 * @param launchConfig
	 * @return
	 * @throws CoreException
	 */
	private IQuery getAnalysisGoal(ILaunchConfiguration launchConfig) throws CoreException {
		IQueryManager queryManager = Activator.getInstance().getQueryManagerInstance();
		IQuery queryInput = null;

		String analysisConfig = launchConfig.getAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), "default");

		if (!analysisConfig.equals("default")) {
			for (Map.Entry<QueryInformation, IQuery> entry : queryManager.getQueries().entrySet()) {
				if (entry.getKey().getId().equals(analysisConfig)) {
					queryInput = entry.getValue();
				}
			}
		}

		return queryInput;

	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws MalformedURLException
	 */
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

	
	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		for (org.eclipse.ui.console.IConsole console1 : conMan.getConsoles())
			if (name.equals(console1.getName()))
				return (MessageConsole) console1;
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new org.eclipse.ui.console.IConsole[] { myConsole });
		return myConsole;
	}
}