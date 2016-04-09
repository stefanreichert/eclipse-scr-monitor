 
package net.wickedshell.scrmonitor.internal.ui.part;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.felix.scr.Component;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import net.wickedshell.scrmonitor.internal.ui.provider.ComponentContentProvider;
import net.wickedshell.scrmonitor.internal.ui.provider.ComponentLabelProvider;
import net.wickedshell.scrmonitor.internal.ui.provider.node.SCRRoot;

public class SCRMonitorPart {
	
	public static final String SCR_MONITOR_REFRESH = "net/wickedshell/srcmonitor/refresh";
	
	private TreeViewer viewerComponents;
	
	@Inject
	private ComponentContentProvider contentProvider;

	@Inject
	private ComponentLabelProvider labelProvider;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);
		
		Label labelHeader = new Label(parent, SWT.NONE);
		labelHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelHeader.setText("Components managed by the Service Component Registry (SCR)");
		
		viewerComponents = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = viewerComponents.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		
		TreeColumn columnId = new TreeColumn(tree, SWT.NONE);
		columnId.setWidth(65);
		columnId.setText("Id");
		
		TreeColumn columnName = new TreeColumn(tree, SWT.NONE);
		columnName.setWidth(350);
		columnName.setText("Name");
		
		TreeColumn columnInterface = new TreeColumn(tree, SWT.NONE);
		columnInterface.setWidth(350);
		columnInterface.setText("Interface Class(es)");
		
		TreeColumn columnBundle = new TreeColumn(tree, SWT.NONE);
		columnBundle.setWidth(200);
		columnBundle.setText("Bundle");
		
		TreeColumn columnState = new TreeColumn(tree, SWT.NONE);
		columnState.setWidth(100);
		columnState.setText("Status");
		
		viewerComponents.setContentProvider(contentProvider);
		viewerComponents.setLabelProvider(labelProvider);
		viewerComponents.setSorter(new ViewerSorter(){
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof Component && e2 instanceof Component){
					Component o1 = (Component) e1;
					Component o2 = (Component) e2;
					return Long.valueOf(o1.getId()).compareTo(o2.getId());
				}
				return super.compare(viewer, e1, e2);
			}
		});
		
		viewerComponents.setInput(new SCRRoot());
	}
	
	@Inject
	@Optional
	public void refresh(@UIEventTopic(SCR_MONITOR_REFRESH) SCRRoot root){
		viewerComponents.getTree().setRedraw(false);
		viewerComponents.setInput(root);
		viewerComponents.getTree().setRedraw(true);
	}
	
	@Focus
	public void onFocus() {
		viewerComponents.getTree().forceFocus();
	}
}