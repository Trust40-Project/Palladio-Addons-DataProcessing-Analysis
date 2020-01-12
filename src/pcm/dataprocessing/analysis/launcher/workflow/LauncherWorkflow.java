package pcm.dataprocessing.analysis.launcher.workflow;

import org.apache.log4j.BasicConfigurator;

import org.apache.log4j.ConsoleAppender;

import org.apache.log4j.PatternLayout;

import de.uka.ipd.sdq.workflow.Workflow;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;

import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

public class LauncherWorkflow {
	public static void main(String[] args) {

		// set up a basic logging configuration

		BasicConfigurator.resetConfiguration();

		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));

//		    HelloWorldJob job = new HelloWorldJob("World");
//
		// Workflow myWorkflow = new Workflow(job);

		// myWorkflow.run();

	}
}
