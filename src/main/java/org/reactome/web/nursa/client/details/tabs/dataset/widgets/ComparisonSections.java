package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.model.Comparison.Operand;
import org.reactome.web.nursa.model.ComparisonDataPoint;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ComparisonSections extends DataSetSections {

    private static final String OVERVIEW_TITLE = "Compare To:";

    private Comparison comparison;

    public ComparisonSections(Comparison comparison, EventBus eventBus) {
        super(eventBus);
        this.comparison = comparison;
    }

    @Override
    protected Widget createOverviewPanel() {
        HorizontalPanel overview = new HorizontalPanel();
        Operand other = comparison.operands[1];
        Widget name = new HTMLPanel(other.dataset.getName());
        name.setStyleName("elv-Details-Title-Wrap");
        overview.add(name);
        Widget doi = new HTMLPanel("DOI: " + other.dataset.getDoi());
        doi.setStyleName(DataSetPanel.RESOURCES.getCSS().doi());
        overview.add(doi);
        Widget exp = new HTMLPanel("Experiment: " + other.experiment.getName());
        exp.setStyleName(DataSetPanel.RESOURCES.getCSS().experiment());
        overview.add(exp);
        return overview;
    }

    @Override
    protected String overviewTitle() {
        return OVERVIEW_TITLE;
    }

    @Override
    protected Widget createDataPointsPanel() {
        DataPanel<ComparisonDataPoint> panel = new ComparisonDataPointsPanel(comparison);
        return panel.asWidget();
    }

    @Override
    protected Widget createAnalysisPanel(EventBus eventBus) {
        return new ComparisonAnalysisPanel(comparison, eventBus);
    }

}
