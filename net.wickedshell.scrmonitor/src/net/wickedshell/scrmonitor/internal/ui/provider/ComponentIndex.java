package net.wickedshell.scrmonitor.internal.ui.provider;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.Component;
import org.eclipse.e4.core.di.annotations.Creatable;

@Creatable
public class ComponentIndex {

	private Map<String, Component> index = new HashMap<>();

	public void clear() {
		index.clear();
	}

	public void createFor(Component[] components) {
		for (Component component : components) {
			String[] services = component.getServices();
			if (services != null) {
				for (String service : services) {
					index.put(service, component);
				}
			}
		}
	}

	public Component findByName(String componentName) {
		return index.get(componentName);
	}

}
