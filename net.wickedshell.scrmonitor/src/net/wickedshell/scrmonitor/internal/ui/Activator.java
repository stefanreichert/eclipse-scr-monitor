package net.wickedshell.scrmonitor.internal.ui;

import java.util.HashMap;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

import net.wickedshell.scrmonitor.internal.ui.part.SCRMonitorPart;
import net.wickedshell.scrmonitor.internal.ui.provider.node.SCRRoot;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		EventAdmin eventAdmin = context.getService(context.getServiceReference(EventAdmin.class));
		context.addBundleListener((event) -> {
			HashMap<String, Object> properties = new HashMap<>();
			properties.put(EventConstants.EVENT_TOPIC, SCRMonitorPart.SCR_MONITOR_REFRESH);
			properties.put(IEventBroker.DATA, new SCRRoot());
			eventAdmin.postEvent(new Event(SCRMonitorPart.SCR_MONITOR_REFRESH, properties));
		});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
