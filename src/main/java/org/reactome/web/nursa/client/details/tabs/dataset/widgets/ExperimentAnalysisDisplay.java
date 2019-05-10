package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.function.Consumer;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.Experiment;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedEvent;

import com.google.gwt.event.shared.EventBus;

public class ExperimentAnalysisPanel extends AnalysisDisplay {

    private Experiment experiment;

    public ExperimentAnalysisPanel(Experiment experiment, EventBus eventBus) {
        super(eventBus);
        this.experiment = experiment;
    }

    @Override
    protected void binomialAnalyse() {
        super.binomialAnalyse(experiment, new Consumer<AnalysisResult>() {

            @Override
            public void accept(AnalysisResult result) {
                showBinomialResult(result.getPathways());
                BinomialCompletedEvent event = new BinomialCompletedEvent(result);
                eventBus.fireEventFromSource(event, ExperimentAnalysisPanel.this);
            }
        });
    }

    @Override
    protected void gseaAnalyse() {
        super.gseaAnalyse(experiment, new Consumer<List<GseaAnalysisResult>>() {

            @Override
            public void accept(List<GseaAnalysisResult> result) {
                showGseaResult(result);
                GseaCompletedEvent event = new GseaCompletedEvent(result);
                eventBus.fireEventFromSource(event, ExperimentAnalysisPanel.this);
            }
        });
    }

}
