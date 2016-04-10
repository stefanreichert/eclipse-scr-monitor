package net.wickedshell.scrmonitor.internal.ui.provider;

import java.util.Arrays;
import java.util.StringJoiner;

import org.apache.felix.scr.Component;
import org.apache.felix.scr.Reference;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

@Creatable
public class ComponentLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

	private static final int MISSING = -1;

	@Override
	public Color getForeground(Object element) {
		if (element instanceof Component) {
			Component component = (Component) element;
			if (component.getState() == Component.STATE_UNSATISFIED) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			} else if (component.getState() == Component.STATE_ACTIVE) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
			} else if (component.getState() == Component.STATE_REGISTERED) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
			}
		}
		if (element instanceof Reference) {
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		}
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Component) {
			Component component = (Component) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(component.getId() + 1);
			case 1:
				return component.getName();
			case 2:
				String[] services = component.getServices();
				if (services != null) {
					StringJoiner joiner = new StringJoiner(",");
					for (String string : services) {
						joiner.add(string);
					}
					return joiner.toString();
				}
				return "";
			case 3:
				return component.getBundle().getSymbolicName();
			case 4:
				return getStateLabel(component.getState());
			}
		} else if (element instanceof Reference) {
			Reference reference = (Reference) element;
			switch (columnIndex) {
			case 1:
				return reference.getName();
			case 2:
				return reference.getServiceName();
			case 4:
				return getStateLabel(MISSING);
			default:
				return "";
			}
		}
		return "unknown column or object";
	}

	private String getStateLabel(int state) {
		switch (state) {
		case MISSING:
			return "missing";
		case Component.STATE_ACTIVE:
			return "active";
		case Component.STATE_REGISTERED:
			return "registered";
		case Component.STATE_UNSATISFIED:
			return "unsatisfied";
		default:
			return "";
		}
	}

	public boolean isLabelMatch(Component component, String filter) {
		boolean bundleMatch = component.getBundle().getSymbolicName().toLowerCase().contains(filter);
		boolean nameMatch = component.getName().toLowerCase().contains(filter);
		boolean serviceMatch = Arrays.toString(component.getServices()).toLowerCase().contains(filter);
		boolean statusMatch = getStateLabel(component.getState()).contains(filter);
		return bundleMatch || nameMatch || serviceMatch || statusMatch;
	}

	public boolean isLabelMatch(Reference reference, String filter) {
		boolean nameMatch = reference.getName().toLowerCase().contains(filter);
		boolean serviceMatch = reference.getServiceName().toLowerCase().contains(filter);
		return nameMatch || serviceMatch;
	}

}
