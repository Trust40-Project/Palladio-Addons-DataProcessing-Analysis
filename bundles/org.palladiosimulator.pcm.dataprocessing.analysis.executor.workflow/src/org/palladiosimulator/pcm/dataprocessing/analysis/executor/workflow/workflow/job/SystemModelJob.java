package org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.QueryParameterType;
import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.workflow.AnalysisBlackboard;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformationTrace;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformator;
import org.palladiosimulator.pcm.dataprocessing.analysis.transformation.basic.ITransformatorFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import de.uka.ipd.sdq.identifier.Identifier;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.ModelLocation;

/**
 * Gets the SystemMode from the usageModel , allocationModel and CharacteristicsType.
 * 
 * @author mirko
 *
 */
public class SystemModelJob extends SequentialBlackboardInteractingJob<AnalysisBlackboard> {

    private AnalysisBlackboard blackboard = null;

    private final ModelLocation usageLoc;
    private final ModelLocation allocLoc;
    private final ModelLocation characLoc;

    private UsageModel usageModel = null;
    private Allocation allocModel = null;
    private CharacteristicTypeContainer characModel = null;

    private final String partitionID;

    /**
     * 
     * @param usageLoc
     * @param allocLoc
     * @param characLoc
     * @param goal
     */
    public SystemModelJob(ModelLocation usageLoc, ModelLocation allocLoc, ModelLocation characLoc) {
        super("Get System from Models");
        this.partitionID = usageLoc.getPartitionID();
        this.usageLoc = usageLoc;
        this.allocLoc = allocLoc;
        this.characLoc = characLoc;
    }

    @Override
    public void setBlackboard(AnalysisBlackboard blackboard) {
        this.blackboard = blackboard;
        usageModel = (UsageModel) blackboard.getPartition(partitionID)
            .getFirstContentElement(usageLoc.getModelID());
        allocModel = (Allocation) blackboard.getPartition(partitionID)
            .getFirstContentElement(allocLoc.getModelID());
        characModel = (CharacteristicTypeContainer) blackboard.getPartition(partitionID)
            .getFirstContentElement(characLoc.getModelID());

    }

    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        if (usageModel == null || allocModel == null || characModel == null) {
            throw new JobFailedException("Could not transform models");
        }

        // transform models
        ITransformator myTransformator = ITransformatorFactory.getInstance()
            .create();
        Mutable<ITransformationTrace> traceContainer = new MutableObject<>();
        org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System transformed = myTransformator
            .transform(usageModel, allocModel, characModel, traceContainer);
        ITransformationTrace trace = traceContainer.getValue();
        blackboard.setDataFlowSystemModel(transformed);

        // calculate new parameter mapping
        Map<String, String> parameterMapping = calculateParameterMapping(trace);
        blackboard.setParameters(parameterMapping);
    }

    protected Map<String, String> calculateParameterMapping(ITransformationTrace trace) {
        // parameter placeholders to parameter value mapping
        Map<String, String> parameterMapping = new HashMap<>();

        // find parameter placeholders required by query
        Map<QueryParameterType, Set<String>> parameterPlaceholders = blackboard.getQuery()
            .getParameters()
            .entrySet()
            .stream()
            .collect(Collectors.groupingBy(Entry::getValue, Collectors.mapping(Entry::getKey, Collectors.toSet())));

        // map characteristic ID parameter
        ResourceSet rs = blackboard.getPartition(partitionID)
            .getResourceSet();
        Set<String> characteristicPlaceholders = parameterPlaceholders
            .getOrDefault(QueryParameterType.MODEL_ID_CHARACTERISTIC, Collections.emptySet());
        for (String characteristicPlaceholder : characteristicPlaceholders) {
            String pcmId = blackboard.getParameters()
                .get(characteristicPlaceholder);
            if (pcmId != null) {
                Optional<Identifier> findResult = findById(rs, pcmId);
                if (findResult.isPresent()) {
                    Identifier foundIdentifier = findResult.get();
                    Optional<String> resolutionResult = trace.resolveId(foundIdentifier);
                    if (resolutionResult.isPresent()) {
                        String foundId = resolutionResult.get();
                        parameterMapping.put(characteristicPlaceholder, foundId);
                        continue;
                    }
                }
            }
            parameterMapping.put(characteristicPlaceholder, null);
        }

        // map string parameters
        Set<String> stringPlaceholders = parameterPlaceholders.getOrDefault(QueryParameterType.STRING,
                Collections.emptySet());
        for (String stringPlaceholder : stringPlaceholders) {
            parameterMapping.put(stringPlaceholder, blackboard.getParameters()
                .get(stringPlaceholder));
        }
        return parameterMapping;
    }

    private static Optional<Identifier> findById(ResourceSet rs, String id) {
        for (TreeIterator<Notifier> iter = rs.getAllContents(); iter.hasNext();) {
            Notifier n = iter.next();
            if (n instanceof Identifier) {
                Identifier i = (Identifier) n;
                if (id.equals(i.getId())) {
                    return Optional.of(i);
                }
            }
        }
        return Optional.empty();
    }

}
