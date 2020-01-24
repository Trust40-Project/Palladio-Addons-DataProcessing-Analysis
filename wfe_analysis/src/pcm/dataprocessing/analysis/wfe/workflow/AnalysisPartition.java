/**
 * 
 */
package pcm.dataprocessing.analysis.wfe.workflow;

import org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System;

import de.uka.ipd.sdq.workflow.mdsd.blackboard.ResourceSetPartition;

/**
 * 
 * 
 * @author Mirko Sowa
 *
 */
public class AnalysisPartition extends ResourceSetPartition {
	org.palladiosimulator.pcm.dataprocessing.prolog.prologmodel.System dataFlowSystemModel = null;
	/**
	 * 
	 * @return
	 */
	public System getDataFlowSystemModel() {
		return dataFlowSystemModel;
	}
	/**
	 * 
	 * @param system
	 */
	public void setDataFlowSystemModel(System system) {
		dataFlowSystemModel = system;
	}
}
