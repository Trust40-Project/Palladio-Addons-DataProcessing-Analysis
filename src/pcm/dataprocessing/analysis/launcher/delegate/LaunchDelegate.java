package pcm.dataprocessing.analysis.launcher.delegate;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.impl.TransformatorFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGeneratorRegistry;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.DefaultReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.UserDefinedReturnValueAssignmentsGenerator;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.prolog4j.manager.IProverManager;
import org.prolog4j.tuprolog.TuPrologProverFactory;
import org.prolog4j.IProverFactory;
import org.prolog4j.Prover;
import org.prolog4j.ProverInformation;

import pcm.dataprocessing.analysis.launcher.delegate.Activator;
import pcm.dataprocessing.analysis.launcher.query.IQueryInput;
import pcm.dataprocessing.analysis.launcher.query.IQueryManager;
import pcm.dataprocessing.analysis.launcher.query.QueryInformation;
import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;
import pcm.dataprocessing.analysis.launcher.constants.Constants;

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

	public LaunchDelegate() {
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		IProfileRegistry.eINSTANCE.getClass();

		this.resolvePaths(configuration);

		this.setupModels();

		org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = convertToSystemModel();

		SystemTranslator sysTranslator = getTranslator(configuration);

		this.translate(configuration, sysTranslator, dataFlowSystemModel);

	}

	private void resolvePaths(ILaunchConfiguration configuration) throws CoreException {

		try {
			usageModelPath = getUriFromText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
			allocModelPath = getUriFromText(
					configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
			chModelPath = getUriFromText(
					configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));
		} catch (MalformedURLException e) {
			// TODO build dialog
			e.printStackTrace();
		}

	}

	/**
	 * Gets and resolves the given paths for the respective models.
	 */
	private void setupModels() {
		if (usageModelPath != null && allocModelPath != null && chModelPath != null) {
			ResourceSet rs = new ResourceSetImpl();

			// TODO is das noch hardcoded?

			usageModel = (UsageModel) rs.getResource(usageModelPath, true).getContents().get(0);
			allocationModel = (Allocation) rs.getResource(allocModelPath, true).getContents().get(0);
			charTypeContainer = (CharacteristicTypeContainer) rs.getResource(chModelPath, true).getContents().get(0);

			EcoreUtil.resolveAll(rs);

		} else {
			// TODO Dialog, path not resolved
			// stop process
		}

	}

	/**
	 * Converts the given models to a compound system model, which is returned.
	 * 
	 * @return the system model
	 */
	private org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System convertToSystemModel() {
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

			TransformatorFactoryImpl transformFactory = new TransformatorFactoryImpl();
			ITransformator myTransformator = transformFactory.create(registry, null);

			return myTransformator.transform(usageModel, allocationModel, charTypeContainer);
		} else {
			// TODO stop
			return null;
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
	 * Translates the system model with a system translator and gets the analysis
	 * goal.
	 * 
	 * @param sysTranslator
	 * @throws CoreException
	 */
	private void translate(ILaunchConfiguration launchConfig, SystemTranslator sysTranslator,
			org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel)
			throws CoreException {

		String testingCode = sysTranslator.translate(dataFlowSystemModel).getCode();

		IProverManager proverManager = Activator.getInstance().getProverManagerInstance();
		IProverFactory proverFactory = null;

		String prologConfig = launchConfig.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), "default");

		if (!prologConfig.equals("default")) {
			for (Map.Entry<ProverInformation, IProverFactory> entry : proverManager.getProvers().entrySet()) {
				if (entry.getKey().getId().equals(prologConfig)) {
					proverFactory = entry.getValue();
				}
			}
		} else {
			// TODO find suitable standard factory
		}

		// TODO proverfactory can be null.

		Prover myProver = proverFactory.createProver();
		myProver.addTheory(testingCode);

		IQueryManager queryManager = Activator.getInstance().getQueryManagerInstance();
		IQueryInput queryInput = null;

		String analysisConfig = launchConfig.getAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), "default");

		if (!analysisConfig.equals("default")) {
			for (Map.Entry<QueryInformation, IQueryInput> entry : queryManager.getQueries().entrySet()) {
				if (entry.getKey().getId().equals(analysisConfig)) {
					queryInput = entry.getValue();
				}
			}
		} else {
			// TODO find standard query
		}

		// TODO query can be null.

		myProver.query(queryInput.getQuery());
	}

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

}
