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
import edu.kit.ipd.sdq.dataflow.systemmodel.SystemTranslator;
import edu.kit.ipd.sdq.dataflow.systemmodel.configuration.Configuration;
import pcm.dataprocessing.analysis.launcher.constants.Constants;
import pcm.dataprocessing.analysis.launcher.constants.QueryInput;

/**
 * Launches a given launch configuration with an usage model, an allocation
 * model and a characteristics model.
 * 
 * 
 * @author Mirko Sowa
 *
 */
public class LaunchDelegate implements ILaunchConfigurationDelegate {

	public LaunchDelegate() {
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException { //TODO refractor: shorten method length.

		IProfileRegistry.eINSTANCE.getClass();

		URI usageModelPath = null;
		URI allocModelPath = null;
		URI chModelPath = null;
		try {
			usageModelPath = getUriFromText(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));
			allocModelPath = getUriFromText(
					configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
			chModelPath = getUriFromText(
					configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));
		} catch (MalformedURLException e) {
			// keine
		}

		if (usageModelPath != null && allocModelPath != null && chModelPath != null) {

			ResourceSet rs = new ResourceSetImpl();

			UsageModel usageModel = (UsageModel) rs.getResource(usageModelPath, true).getContents().get(0);

			Allocation allocationModel = (Allocation) rs.getResource(allocModelPath, true).getContents().get(0);

			CharacteristicTypeContainer characteristicTypeContainer = (CharacteristicTypeContainer) rs
					.getResource(chModelPath, true).getContents().get(0);

			EcoreUtil.resolveAll(rs);

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

			org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = myTransformator
					.transform(usageModel, allocationModel, characteristicTypeContainer);

			Configuration noOptimizationConfiguration = new Configuration();
			noOptimizationConfiguration.setArgumentAndReturnValueIndexing(false);
			noOptimizationConfiguration.setOptimizedNegations(false);
			noOptimizationConfiguration.setShorterAssignments(false);

			SystemTranslator sysTranslator = new SystemTranslator(noOptimizationConfiguration);

			String testingCode = sysTranslator.translate(dataFlowSystemModel).getCode();

			// get the Prover
			
			IProverManager proverManager = Activator.getInstance().getProverManagerInstance();		
			IProverFactory proverFactory = null;
			
			String analysisConfig = configuration.getAttribute(Constants.ANALYSIS_GOAL_LABEL.getConstant(), "default");
			
			//TODO remove hardcoded
			
			if(!analysisConfig.equals("default")) {
				for(Map.Entry<ProverInformation, IProverFactory> entry : proverManager.getProvers().entrySet()) {
					if(entry.getKey().getId().equals(analysisConfig)) {
						proverFactory = entry.getValue();
					}
				}
			}else {
				//TODO find suitable standard factory
				proverFactory = new TuPrologProverFactory();
			}
						
			Prover myProver = proverFactory.createProver();
			
			myProver.addTheory(testingCode);
			
			String queryToApply = "";
			String prologConfig = configuration.getAttribute(Constants.PROLOG_INTERPRETER_LABEL.getConstant(), "");
				for(QueryInput a : QueryInput.values()) {
					if(a.getName().equals(prologConfig)) {
						queryToApply = a.getQuery();
					}
				}
				
			org.prolog4j.Query query = myProver.query(queryToApply);
			
		} else {
			// at least one path is null
		}

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
