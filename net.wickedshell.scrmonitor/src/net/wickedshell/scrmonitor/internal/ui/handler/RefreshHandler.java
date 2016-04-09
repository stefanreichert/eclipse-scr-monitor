
package net.wickedshell.scrmonitor.internal.ui.handler;

import java.util.HashMap;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import net.wickedshell.scrmonitor.internal.ui.part.SCRMonitorPart;

public class RefreshHandler {

	private EventAdmin eventAdmin;

	@Inject
	public RefreshHandler(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	@Execute
	public void execute() {
		eventAdmin.postEvent(new Event(SCRMonitorPart.SCR_MONITOR_REFRESH, new HashMap<>()));
	}
}
