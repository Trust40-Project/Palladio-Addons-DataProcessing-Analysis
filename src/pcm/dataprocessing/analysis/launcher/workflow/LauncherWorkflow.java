package pcm.dataprocessing.analysis.launcher.workflow;

import org.apache.log4j.BasicConfigurator;

import org.apache.log4j.ConsoleAppender;

import org.apache.log4j.PatternLayout;
import org.eclipse.emf.common.util.URI;

import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.blackboard.Blackboard;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.ParallelJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import pcm.dataprocessing.analysis.launcher.workflow.job.ModelResolverJob;
import pcm.dataprocessing.analysis.launcher.workflow.job.PathResolverJob;

public class LauncherWorkflow {
	public void launch() {

		// set up a basic logging configuration

		BasicConfigurator.resetConfiguration();

		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));

		Blackboard<Object> myBlackboard = new Blackboard<Object>();
		String pathKey = "path";
		String modelKey = "model";
		
		myBlackboard.addPartition(modelKey, URI.createURI("test"));
//		    PathResolver job = new PathResolver( input path);
//
		ParallelJob parallelJob = new ParallelJob();
		
		// parallelJob.add(new PathResolverJob(input path))
		
		parallelJob.add(new ModelResolverJob(myBlackboard, modelKey));
		
		
		Workflow myWorkflow = new Workflow(parallelJob);
		myWorkflow.run();
		
		

		
	}
}
