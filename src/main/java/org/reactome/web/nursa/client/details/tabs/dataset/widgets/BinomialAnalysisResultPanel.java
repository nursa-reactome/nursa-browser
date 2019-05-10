package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.event.shared.EventBus;

public class BinomialAnalysisResultPanel extends AnalysisResultPanel<PathwaySummary, Long> {

    public BinomialAnalysisResultPanel(List<PathwaySummary> results, EventBus eventBus) {
        super(results, eventBus);
    }

    @Override
    protected BinomialExperimentTable createResultsTable(List<PathwaySummary> results) {
        return new BinomialExperimentTable(results);
    }

    public BinomialAnalysisResultPanel(List<PathwaySummary> results, double filter,
            EventBus eventBus) {
        super(results, filter, eventBus);
    }

    @Override
    protected double getFdr(PathwaySummary result) {
        return result.getEntities().getFdr();
    }

}
