package org.reactome.web.nursa.client.hierarchy;

import java.util.ArrayList;
import java.util.List;

import org.reactome.web.pwp.client.hierarchy.HierarchyDisplay;
import org.reactome.web.pwp.model.client.classes.Event;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.TreeItem;

public class NursaHierarchyDisplay extends HierarchyDisplay {

    /**
     * The substring which marks a hierarchical item as a nuclear receptor.
     */
    private static final String NUCLEAR_RECEPTOR_SUBSTR = "Nuclear Receptor";
    private int nuclearReceptorItemCount;

    @Override
    protected void loadHierarchy(List<? extends Event> tlps) throws Exception {
        List<Event> tlpsSorted = sortTlps(tlps);
        this.hierarchyTree.loadPathwayChildren(null, tlpsSorted);
        // Add the nuclear receptor style.
        for (int i=0; i < nuclearReceptorItemCount; i++) {
            TreeItem item = this.hierarchyTree.getItem(i);
            item.addStyleName(RESOURCES.getCSS().nuclearReceptorTree());
        }
    }

    private List<Event> sortTlps(List<? extends Event> tlps) {
        // Place the Nuclear Receptors item first.
        List<Event> tlpsSorted = new ArrayList<>();
        for (Event tlp: tlps) {
            if (isNuclearReceptorItem(tlp)) {
                tlpsSorted.add(0, tlp);
                nuclearReceptorItemCount++;
            } else {
                tlpsSorted.add(tlp);
            }
        }
        
        return tlpsSorted;
    }

    private boolean isNuclearReceptorItem(Event tlp) {
        return tlp.getDisplayName().contains(NUCLEAR_RECEPTOR_SUBSTR);
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("nursa-Hierarchy")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "NursaHierarchy.css";

        String nuclearReceptorTree();
    }
}
