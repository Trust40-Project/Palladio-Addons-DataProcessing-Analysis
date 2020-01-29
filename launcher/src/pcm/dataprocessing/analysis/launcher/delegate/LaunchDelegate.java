package pcm.dataprocessing.analysis.launcher.delegate;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformatorFactory;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.impl.TransformatorFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGeneratorRegistry;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.DefaultReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.UserDefinedReturnValueAssignmentsGenerator;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.prolog4j.IProverFactory;
import org.prolog4j.Prover;
import org.prolog4j.Query;
import org.prolog4j.Solution;

import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;
import pcm.dataprocessing.analysis.launcher.constants.Constants;
import pcm.dataprocessing.analysis.wfe.query.IQuery;
import pcm.dataprocessing.analysis.wfe.workflow.AnalysisWorkflowConfig;

/**
 * Launches a given launch configuration with an usage model, an allocation
 * model and a characteristics model.
 * 
 * 
 * @author Mirko Sowa
 *
 */
public class LaunchDelegate implements ILaunchConfigurationDelegate {

	private URI usageModelPath = null;
	private URI allocModelPath = null;
	private URI chModelPath = null;

	private UsageModel usageModel = null;
	private Allocation allocationModel = null;
	private CharacteristicTypeContainer charTypeContainer = null;

	private IProverFactory proverFactory = null;
	private IQuery query = null;
	org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = null;
	SystemTranslator sysTranslator = null;

	public LaunchDelegate() {
		new AnalysisWorkflowConfig(allocModelPath, allocModelPath, allocModelPath, null);
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		IProfileRegistry.eINSTANCE.getClass();

		resolvePaths(configuration);

		resolveModels();

		this.dataFlowSystemModel = convertToSystemModel();

		this.sysTranslator = getTranslator(configuration);

		//this.proverFactory = getProverFactory(configuration);

		//this.query = getAnalysisGoal(configuration);

		evaluateModel(configuration, sysTranslator, dataFlowSystemModel);

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

	private void resolveModels() throws CoreException {

		ResourceSet rs = new ResourceSetImpl();
		Resource usageResource = rs.createResource(usageModelPath);
		Resource allocationResource = rs.createResource(allocModelPath);
		Resource charTypeResource = rs.createResource(chModelPath);

		try {
			usageResource.load(null);
			allocationResource.load(null);
			charTypeResource.load(null);
		} catch (IOException exception) {
			throw new CoreException(
					new Status(IStatus.ERROR, "pcm.dataprocessing.analysis.launcher", "Could not resolve ressource."));
		}
		usageModel = (UsageModel) usageResource.getContents().get(0);
		allocationModel = (Allocation) allocationResource.getContents().get(0);
		charTypeContainer = (CharacteristicTypeContainer) charTypeResource.getContents().get(0);

		EcoreUtil.resolveAll(rs);

	}

	/**
	 * Converts the given models to a compound system model, which is returned.
	 * 
	 * @return the system model extracted from the given models
	 * @throws CoreException if the models could not be resolved to a working model
	 */
	private org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System convertToSystemModel()
			throws CoreException {
		if (usageModel != null && allocationModel != null && charTypeContainer != null) {
			IReturnValueAssignmentGeneratorRegistry registry = new IReturnValueAssignmentGeneratorRegistry() {
				@Override
				public Iterable<IReturnValueAssignmentGenerator> getGenerators() {
					Collection<IReturnValueAssignmentGenerator> generators = new ArrayList<>();
					generators.add(new DefaultReturnValueAssignmentGenerator());
					generators.add(new UserDefinedReturnValueAssignmentsGenerator());
					return generators;
				}
			};

			ITransformatorFactory transformatorFactory = new TransformatorFactoryImpl();

			ITransformator myTransformator = transformatorFactory.create(registry, null);

			return myTransformator.transform(usageModel, allocationModel, charTypeContainer);

		} else {
			throw new CoreException(
					new Status(IStatus.ERROR, "pcm.dataprocessing.analysis.launcher", "Could not transform models."));
		}
	}

	/**
	 * Gets a system translator with parameters specified in the launch
	 * configuration.
	 * 
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	private SystemTranslator getTranslator(ILaunchConfiguration launchConfig) throws CoreException {

		Configuration noOptimizationConfiguration = new Configuration();

		boolean shortAssign = false;
		boolean optimNegation = false;
		boolean returnValueIndexing = false;

		returnValueIndexing = launchConfig.getAttribute(Constants.ADV_ARG_AND_RETURN.getConstant(), false);
		optimNegation = launchConfig.getAttribute(Constants.ADV_OPTIM_NEGATION.getConstant(), false);
		shortAssign = launchConfig.getAttribute(Constants.ADV_SHORT_ASSIGN.getConstant(), false);

		noOptimizationConfiguration.setArgumentAndReturnValueIndexing(returnValueIndexing);
		noOptimizationConfiguration.setOptimizedNegations(optimNegation);
		noOptimizationConfiguration.setShorterAssignments(shortAssign);

		return new SystemTranslator(noOptimizationConfiguration);
	}

	/**
	 * 
	 * @param launchConfig
	 * @return
	 * @throws CoreException
	 
	private IProverFactory getProverFactory(ILaunchConfiguration launchConfig) throws CoreException {
		IProverManager proverManager = Activator.getInstance().getProverManagerInstance();
		IProverFactory myProverFactory = null;

		String prologConfig = launchConfig.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), "default");

		if (!prologConfig.equals("default")) {
			for (Map.Entry<ProverInformation, IProverFactory> entry : proverManager.getProvers().entrySet()) {
				if (entry.getKey().getId().equals(prologConfig)) {
					myProverFactory = entry.getValue();
				}
			}
		} else {
			LinkedList<ProverInformation> availableProversInformation = new LinkedList<ProverInformation>(
					proverManager.getProvers().keySet());
			for (ProverInformation i : availableProversInformation) {
				if (i.needsNativeExecutables()) {
					availableProversInformation.remove(i);
				}
			}
			Comparator<ProverInformation> compareByName = Comparator.comparing(e -> e.getName());
			Collections.sort(availableProversInformation, compareByName);

			if (availableProversInformation.get(0) != null) {
				myProverFactory = proverManager.getProvers().get(availableProversInformation.get(0));
			}
		}

		return myProverFactory;

	}*/

	/**
	 * 
	 * @param launchConfig
	 * @return
	 * @throws CoreException
	 
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

	}*/

	/**
	 * 
	 * @param launchConfig
	 * @param sysTranslator
	 * @param dataFlowSystemModel
	 * @throws CoreException
	 */
	private void evaluateModel(ILaunchConfiguration launchConfig, SystemTranslator sysTranslator,
			org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel)
			throws CoreException {

		String testingCode = sysTranslator.translate(dataFlowSystemModel).getCode();

		Prover myProver = proverFactory.createProver();
		myProver.addTheory(testingCode);

		Query myQuery = myProver.query(query.getQuery());
		Solution<Object> solution = myQuery.solve();
		MessageConsole myConsole = findConsole(Constants.CONSOLE_ID.getConstant());
		MessageConsoleStream out = myConsole.newMessageStream();

		if (!solution.isSuccess()) {
			out.println("Query solution had success: " + solution.isSuccess());
		} else {

			for(Entry<String, String> t : query.getResultVars().entrySet()) {
				out.println(solution.get(t.getValue()));
			}
			

		}
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
