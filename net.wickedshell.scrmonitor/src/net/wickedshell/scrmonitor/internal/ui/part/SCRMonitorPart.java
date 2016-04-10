
package net.wickedshell.scrmonitor.internal.ui.part;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import net.wickedshell.scrmonitor.internal.ui.event.Topic;
import net.wickedshell.scrmonitor.internal.ui.provider.ComponentContentProvider;
import net.wickedshell.scrmonitor.internal.ui.provider.ComponentLabelProvider;
import net.wickedshell.scrmonitor.internal.ui.provider.node.SCRRoot;

public class SCRMonitorPart {

	// collaborators
	@Inject
	private ComponentContentProvider contentProvider;
	@Inject
	private ComponentLabelProvider labelProvider;
	@Inject
	private ComponentSorter componentSorter;
	@Inject
	private ComponentFilter componentFilter;

	// widgets
	private Text textFilter;
	private TreeViewer viewerComponents;


	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Label labelHeader = new Label(parent, SWT.NONE);
		labelHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelHeader.setText("Components managed by the Service Component Registry (SCR)");

		textFilter = new Text(parent, SWT.BORDER);
		textFilter.setToolTipText("Filter for name, state, bundle or class");
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		textFilter.addModifyListener(componentFilter);

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
		viewerComponents.setSorter(componentSorter);
		viewerComponents.addFilter(componentFilter);

		viewerComponents.setInput(new SCRRoot());
	}

	@Inject
	@Optional
	public void handleBundlesChanged(@UIEventTopic(Topic.BUNDLES_CHANGED) SCRRoot root) {
		viewerComponents.getTree().setRedraw(false);
		viewerComponents.setInput(root);
		viewerComponents.getTree().setRedraw(true);
	}

	@Inject
	@Optional
	public void handleFilterChanged(@UIEventTopic(Topic.FILTER_CHANGED) String filter) {
		viewerComponents.refresh();
	}

	@Focus
	public void onFocus() {
		viewerComponents.getTree().forceFocus();
	}
}