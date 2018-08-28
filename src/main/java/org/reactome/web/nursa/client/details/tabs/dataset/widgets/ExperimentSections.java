package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.web.pwp.client.details.common.widgets.panels.TextPanel;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;

public class ExperimentSections extends DataSetSections {

    private DataSet dataset;
    private Experiment experiment;

    public ExperimentSections(DataSet dataset, Experiment experiment, EventBus eventBus) {
        super(eventBus);
        this.dataset = dataset;
        this.experiment = experiment;
    }

    @Override
    protected Widget createOverviewPanel() {
        return new TextPanel(dataset.getDescription());
    }

    @Override
    protected Widget createGenesPanel() {
        ExperimentDataPointsPanel panel = new ExperimentDataPointsPanel(experiment);
        return panel.asWidget();
    }

    @Override
    protected Widget createAnalysisPanel(EventBus eventBus) {
        return new ExperimentAnalysisPanel(experiment, eventBus);
    }

}
