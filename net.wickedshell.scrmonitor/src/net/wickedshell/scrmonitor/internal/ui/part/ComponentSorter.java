package net.wickedshell.scrmonitor.internal.ui.part;

import org.apache.felix.scr.Component;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

@Creatable
public class ComponentSorter extends ViewerSorter {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Component && e2 instanceof Component) {
			Component o1 = (Component) e1;
			Component o2 = (Component) e2;
			return Long.valueOf(o1.getId()).compareTo(o2.getId());
		}
		return super.compare(viewer, e1, e2);
	}
}