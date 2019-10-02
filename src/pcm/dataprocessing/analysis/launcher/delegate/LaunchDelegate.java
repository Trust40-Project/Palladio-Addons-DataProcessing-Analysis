package pcm.dataprocessing.analysis.launcher.delegate;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.impl.TransformatorFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.IReturnValueAssignmentGeneratorRegistry;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.DefaultReturnValueAssignmentGenerator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.characteristics.impl.UserDefinedReturnValueAssignmentsGenerator;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import pcm.dataprocessing.analysis.launcher.constants.Constants;

/**
 * Launches a given launch configuration with an usage model, an allocation model and a characteristics model.
 * 
 * 
 * @author mirko
 *
 */
public class LaunchDelegate implements ILaunchConfigurationDelegate {

	public LaunchDelegate() {
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		URI usageModelPath = URI
				.createFileURI(configuration.getAttribute(Constants.USAGE_MODEL_LABEL.getConstant(), ""));

		URI allocModelPath = URI
				.createFileURI(configuration.getAttribute(Constants.ALLOCATION_MODEL_LABEL.getConstant(), ""));
		
		URI chModelPath = URI
				.createFileURI(configuration.getAttribute(Constants.CHARACTERISTICS_MODEL_LABEL.getConstant(), ""));

		
		if (usageModelPath != null && allocModelPath != null && chModelPath != null) {

			ResourceSet rs = new ResourceSetImpl();

			UsageModel usageModel = (UsageModel) rs.getResource(usageModelPath, true).getContents().get(0);

			Allocation allocationModel = (Allocation) rs.getResource(allocModelPath, true).getContents().get(0);

			CharacteristicTypeContainer characteristicTypeContainer = (CharacteristicTypeContainer) rs
					.getResource(chModelPath, true).getContents().get(0);

			EcoreUtil.resolveAll(rs);

			System.out.println("usageModel  " + usageModel.toString());

			//?
			
			IReturnValueAssignmentGeneratorRegistry registry = new IReturnValueAssignmentGeneratorRegistry() {
				@Override
				public Iterable<IReturnValueAssignmentGenerator> getGenerators() {
					Collection<IReturnValueAssignmentGenerator> generators = new ArrayList<>();
					generators.add(new DefaultReturnValueAssignmentGenerator());
					generators.add(new UserDefinedReturnValueAssignmentsGenerator());
					
					return generators;
				}
			};

			TransformatorFactoryImpl factory = new TransformatorFactoryImpl();
			ITransformator myTransformator = factory.create(registry, null);

			org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = myTransformator
					.transform(usageModel, allocationModel, characteristicTypeContainer);

		

		}

	}

}
