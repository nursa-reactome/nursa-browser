package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.reactome.web.pwp.client.details.common.widgets.panels.TextPanel;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSections implements Iterable<Widget> {

    private static String OVERVIEW_TITLE = "Overview";
    private static String GENE_LIST_TITLE = "Experiments";
    private static String PATHWAY_TITLE = "Pathway";
    
    private List<Widget> sections;
    private Widget overview;
    private Widget dataPoints;
    private Widget pathways;

    public DataSetSections(DataSet dataset, EventBus eventBus) {
        sections = new ArrayList<Widget>();
        overview = createOverviewSection(dataset);
        sections.add(overview);
        dataPoints = createDataPointsSection(dataset, eventBus);
        sections.add(dataPoints);
        // The first experiment is the default.
        Experiment experiment = dataset.getExperiments().get(0);
        pathways = createPathwaySection(experiment, eventBus);
        sections.add(pathways);
    }

    public Widget getOverviewSection() {
        return overview;
    }

    public Widget getPathwaysSection() {
        return pathways;
    }

    public Widget getDataPointsSection() {
        return dataPoints;
    }

    @Override
    public Iterator<Widget> iterator() {
        return sections.iterator();
    }

    private static Widget createOverviewSection(DataSet dataset) {
        Widget panel = new TextPanel(dataset.getDescription());
        panel.setStyleName(DataSetPanel.RESOURCES.getCSS().overview());
        return createDataSetSection(OVERVIEW_TITLE, panel);
    }

    private static Widget createDataPointsSection(DataSet dataset, EventBus eventBus) {
        Widget panel = new DataPointsPanel(dataset, eventBus);
        return createDataSetSection(GENE_LIST_TITLE, panel);
    }

    private static Widget createPathwaySection(Experiment experiment, EventBus eventBus) {
        Widget panel = new PathwayPanel(experiment, eventBus);
        return createDataSetSection(PATHWAY_TITLE, panel);
    }

    private static Widget createDataSetSection(String title, Widget child) {
        VerticalPanel content = new VerticalPanel();
        content.add(child);
        content.setWidth("100%");
        DataSetSection section = new DataSetSection();
        section.setWidth("100%");
        section.add(title, content);

        return section;
    }
}
