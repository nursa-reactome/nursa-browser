package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
abstract public class DataSetSections implements Iterable<Widget> {

    private static String OVERVIEW_TITLE = "Overview";
    private static String GENES_TITLE = "Genes";
    private static String PATHWAYS_TITLE = "Pathways";
    
    private List<Widget> sections;
    private Widget overview;
    private Widget dataPoints;
    private Widget pathways;
    private EventBus eventBus;

    public DataSetSections(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Iterator<Widget> iterator() {
        // Build the sections on demand.
        if (sections == null) {
            sections = new ArrayList<Widget>();
            overview = createOverviewSection();
            sections.add(overview);
            dataPoints = createGenesSection();
            sections.add(dataPoints);
            pathways = createPathwaysSection(eventBus);
            sections.add(pathways);
            
        }
        return sections.iterator();
    }

    abstract protected Widget createOverviewPanel();

    abstract protected Widget createGenesPanel();
    
    abstract protected Widget createPathwaysPanel(EventBus eventBus);

    protected String overviewTitle() {
        return OVERVIEW_TITLE;
    }

    private Widget createOverviewSection() {
        Widget panel = createOverviewPanel();
        panel.setStyleName(DataSetPanel.RESOURCES.getCSS().overview());
        return createDataSetSection(overviewTitle(), panel);
    }

    private Widget createGenesSection() {
        Widget panel = createGenesPanel();
        return createDataSetSection(GENES_TITLE, panel);
    }

    private Widget createPathwaysSection(EventBus eventBus) {
        Widget panel = createPathwaysPanel(eventBus);
        return createDataSetSection(PATHWAYS_TITLE, panel);
    }

    private Widget createDataSetSection(String title, Widget child) {
        VerticalPanel content = new VerticalPanel();
        content.add(child);
        content.setWidth("100%");
        DataSetSection section = new DataSetSection();
        section.setWidth("100%");
        section.add(title, content);

        return section;
    }
}
