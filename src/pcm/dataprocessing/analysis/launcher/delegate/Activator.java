package pcm.dataprocessing.analysis.launcher.delegate;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.prolog4j.manager.IProverManager;

import pcm.dataprocessing.analysis.launcher.query.IQueryManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "testDiscovery"; //$NON-NLS-1$

	// The shared instance
	private static Activator instance;

	private IProverManager proverManager;
	private IQueryManager queryManager;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setInstance(this);

		ServiceReference<IProverManager> proverManagerReference = context.getServiceReference(IProverManager.class);
		ServiceReference<IQueryManager> queryManagerReference = context.getServiceReference(IQueryManager.class);
		// -> error
		proverManager = context.getService(proverManagerReference);
		queryManager = context.getService(queryManagerReference);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		setInstance(null);
		super.stop(context);
	}
	
	

	private static void setInstance(Activator instance) {
		Activator.instance = instance;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	
	public static Activator getInstance() {
		return Activator.instance;
	}

	public IProverManager getProverManagerInstance() {
		return proverManager;
	}
	
	public IQueryManager getQueryMangerInstance() {
		return queryManager;
	}

}
