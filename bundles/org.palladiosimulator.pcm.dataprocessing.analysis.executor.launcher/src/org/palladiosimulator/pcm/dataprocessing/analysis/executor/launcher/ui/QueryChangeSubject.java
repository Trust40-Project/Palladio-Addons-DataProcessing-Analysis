package org.palladiosimulator.pcm.dataprocessing.analysis.executor.launcher.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import org.palladiosimulator.pcm.dataprocessing.analysis.executor.workflow.query.IQuery;

public class QueryChangeSubject {

	private final Collection<Consumer<IQuery>> listeners = new ArrayList<>();
	
	public synchronized void notify(IQuery newQuery) {
		listeners.forEach(l -> l.accept(newQuery));
	}
	
	public synchronized void addListener(Consumer<IQuery> listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeListener(Consumer<IQuery> listener) {
		listeners.remove(listener);
	}
	
}
