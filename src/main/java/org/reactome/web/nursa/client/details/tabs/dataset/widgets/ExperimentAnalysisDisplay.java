package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.function.Consumer;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedEvent;

import com.google.gwt.event.shared.EventBus;

public class ExperimentAnalysisDisplay extends AnalysisDisplay {

    private List<DisplayableDataPoint> dataPoints;

    public ExperimentAnalysisDisplay(List<DisplayableDataPoint> dataPoints, EventBus eventBus) {
        super(eventBus);
        this.dataPoints = dataPoints;
    }

    @Override
    protected void binomialAnalyse() {
        Consumer<AnalysisResult> consumer = new Consumer<AnalysisResult>() {

            @Override
            public void accept(AnalysisResult result) {
                showBinomialResult(result.getPathways());
                BinomialCompletedEvent event = new BinomialCompletedEvent(result);
                eventBus.fireEventFromSource(event, ExperimentAnalysisDisplay.this);
            }
        };

        super.binomialAnalyse(dataPoints, consumer);
    }

    @Override
    protected void gseaAnalyse() {
        Consumer<List<GseaAnalysisResult>> consumer = new Consumer<List<GseaAnalysisResult>>() {

            @Override
            public void accept(List<GseaAnalysisResult> result) {
                showGseaResult(result);
                GseaCompletedEvent event = new GseaCompletedEvent(result);
                eventBus.fireEventFromSource(event, ExperimentAnalysisDisplay.this);
            }
        };
        
        super.gseaAnalyse(dataPoints, consumer);
    }

}
