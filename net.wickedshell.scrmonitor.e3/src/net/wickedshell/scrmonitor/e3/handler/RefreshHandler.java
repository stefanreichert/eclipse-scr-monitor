package net.wickedshell.scrmonitor.e3.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventAdmin;

public class RefreshHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EventAdmin eventAdmin = PlatformUI.getWorkbench().getService(EventAdmin.class);
		new net.wickedshell.scrmonitor.internal.ui.handler.RefreshHandler(eventAdmin).execute();
		return null;
	}

}
