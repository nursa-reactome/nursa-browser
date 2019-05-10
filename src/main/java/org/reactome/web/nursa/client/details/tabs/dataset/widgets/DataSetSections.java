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
    private static String ANALYSIS_TITLE = "Analysis";
    
    private List<Widget> sections;
    private Widget overview;
    private Widget dataPoints;
    private Widget analysis;
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
            // The dataset overview section is null if the dataset doesn't
            // have a description.
            if (overview != null) {
                sections.add(overview);
  
            }
            dataPoints = createDataPointsSection();
            sections.add(dataPoints);
            analysis = createAnalysisSection(eventBus);
            sections.add(analysis);
            
        }
        return sections.iterator();
    }

    abstract protected Widget createOverviewPanel();

    abstract protected Widget createDataPointsPanel();
    
    abstract protected Widget createAnalysisPanel(EventBus eventBus);

    protected String overviewTitle() {
        return OVERVIEW_TITLE;
    }

    private Widget createOverviewSection() {
        Widget panel = createOverviewPanel();
        if (panel == null) {
            return null;
        }
        panel.setStyleName(DataSetPanel.RESOURCES.getCSS().overview());
        return createDataSetSection(overviewTitle(), panel);
    }

    private Widget createDataPointsSection() {
        Widget panel = createDataPointsPanel();
        return createDataSetSection(GENES_TITLE, panel);
    }

    private Widget createAnalysisSection(EventBus eventBus) {
        Widget panel = createAnalysisPanel(eventBus);
        return createDataSetSection(ANALYSIS_TITLE, panel);
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
