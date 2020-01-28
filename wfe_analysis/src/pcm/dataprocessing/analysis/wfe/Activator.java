package pcm.dataprocessing.analysis.wfe;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.prolog4j.manager.IProverManager;
import pcm.dataprocessing.analysis.wfe.query.IQueryManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "pcm.dataprocessing.analysis.wfe"; //$NON-NLS-1$

	// The shared instance
	private static Activator instance;

	// The shared instances of both the prover and the query manager
	private IProverManager proverManager;
	private volatile IQueryManager queryManager;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setInstance(instance);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		setInstance(null);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return instance;
	}

	public IProverManager getProverManagerInstance() {
		return proverManager;
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

	public IQueryManager getQueryManagerInstance() {
		IQueryManager foundQueryManager = queryManager;
		if (foundQueryManager == null) {
			synchronized (this) {
				foundQueryManager = queryManager;
				if (foundQueryManager == null) {
					BundleContext bundleContext = getInstance().getBundle().getBundleContext();
					ServiceReference<IQueryManager> queryManagerReference = bundleContext
							.getServiceReference(IQueryManager.class);
					foundQueryManager = bundleContext.getService(queryManagerReference);
					queryManager = foundQueryManager;
				}
			}
		}
		return foundQueryManager;
	}
}
