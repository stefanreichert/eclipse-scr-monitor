package net.wickedshell.scrmonitor.internal.ui.provider;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.felix.scr.Component;
import org.apache.felix.scr.Reference;
import org.apache.felix.scr.ScrService;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import net.wickedshell.scrmonitor.internal.ui.provider.node.SCRRoot;

@Creatable
public class ComponentContentProvider implements ITreeContentProvider {

	@Inject
	private ScrService registryService;

	@Inject
	private ComponentIndex componentIndex;

	private Component[] components = new Component[0];

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		componentIndex.clear();
		components = new Component[0];
		if (newInput != null && newInput instanceof SCRRoot) {
			components = registryService.getComponents();
			componentIndex.createFor(components);
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SCRRoot) {
			return components;
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Component) {
			Component component = (Component) parentElement;
			Reference[] references = component.getReferences();
			if (references != null) {
				Object[] array = Arrays.stream(references)
						.map(reference -> {
							Component referencedComponent = componentIndex.findByName(reference.getServiceName());
							if(referencedComponent != null){
								return referencedComponent;
							}
							return reference;
						})
						.filter(reference -> reference != null).toArray();
				return array;
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Component) {
			Component component = (Component) element;
			Reference[] references = component.getReferences();
			return references != null;
		}
		return false;
	}

}
