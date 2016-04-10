package net.wickedshell.scrmonitor.internal.ui.part;

import javax.inject.Inject;

import org.apache.felix.scr.Component;
import org.apache.felix.scr.Reference;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

import net.wickedshell.scrmonitor.internal.ui.event.Topic;
import net.wickedshell.scrmonitor.internal.ui.provider.ComponentLabelProvider;
import net.wickedshell.scrmonitor.internal.ui.provider.node.SCRRoot;

@Creatable
public class ComponentFilter extends ViewerFilter implements ModifyListener {

	private String filter = "";

	@Inject
	private ComponentLabelProvider labelProvider;

	@Inject
	private IEventBroker eventBroker;

	@Override
	public void modifyText(ModifyEvent event) {
		String newFilter = ((Text) event.widget).getText();
		if (!filter.equals(newFilter)) {
			filter = newFilter.toLowerCase().trim();
			eventBroker.post(Topic.FILTER_CHANGED, filter);
		}
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (!(parentElement instanceof SCRRoot)) {
			return true;
		}
		if (element instanceof Component) {
			return labelProvider.isLabelMatch((Component) element, filter);
		} else if (element instanceof Reference) {
			return labelProvider.isLabelMatch((Reference) element, filter);
		}
		return false;
	}
}