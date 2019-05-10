package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.nursa.client.search.DataPointsLoadedEvent;
import org.reactome.web.pwp.client.details.common.widgets.panels.TextPanel;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;

public class ExperimentSections extends DataSetSections {

    private DataSet dataset;
    private List<DisplayableDataPoint> dataPoints;

    public ExperimentSections(DataPointsLoadedEvent event, EventBus eventBus) {
        super(eventBus);
        this.dataset = event.getDataSet();
        this.dataPoints = event.getDataPoints();
    }

    @Override
    protected Widget createOverviewPanel() {
        String text = dataset.getDescription();
        return text == null ? null : new TextPanel(text);
    }

    @Override
    protected Widget createDataPointsPanel() {
        DataPanel<DisplayableDataPoint> panel = new ExperimentDataPointsPanel(dataPoints);
        return panel.asWidget();
    }

    @Override
    protected Widget createAnalysisPanel(EventBus eventBus) {
        return new ExperimentAnalysisDisplay(dataPoints, eventBus);
    }

}
