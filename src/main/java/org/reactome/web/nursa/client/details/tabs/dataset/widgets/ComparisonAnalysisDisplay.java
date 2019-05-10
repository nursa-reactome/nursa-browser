package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.function.Consumer;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonAnalysisCompletedEvent;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;

public class ComparisonAnalysisDisplay extends AnalysisDisplay {

    private Comparison comparison;

    public ComparisonAnalysisDisplay(Comparison comparison, EventBus eventBus) {
        super(eventBus);
        this.comparison = comparison;
    }

    @Override
    protected void binomialAnalyse() {
        List<List<DisplayableDataPoint>> exps = comparison.dataPoints();
        List<DisplayableDataPoint> exp1 = exps.get(0);
        List<DisplayableDataPoint> exp2 = exps.get(1);
        binomialAnalyse(exp1, new Consumer<AnalysisResult>() {
            
            @Override
            public void accept(AnalysisResult first) {
                binomialAnalyse(exp2, new Consumer<AnalysisResult>() {

                    @Override
                    public void accept(AnalysisResult second) {
                        double filter = getResultFilter();
                        BinomialComparisonPartition partition =
                                new BinomialComparisonPartition(
                                        first.getPathways(),
                                        second.getPathways(),
                                        filter);
                        showBinomialResult(partition);
                        ComparisonAnalysisCompletedEvent event =
                                new ComparisonAnalysisCompletedEvent(comparison, partition);
                        eventBus.fireEventFromSource(event, ComparisonAnalysisDisplay.this);
                    }

                });
            }
 
        });
    }

    @Override
    protected void gseaAnalyse() {
        List<List<DisplayableDataPoint>> dataPoints = comparison.dataPoints();
        List<DisplayableDataPoint> dps1 = dataPoints.get(0);
        List<DisplayableDataPoint> dps2 = dataPoints.get(1);
        gseaAnalyse(dps1, new Consumer<List<GseaAnalysisResult>>() {
            
            @Override
            public void accept(List<GseaAnalysisResult> first) {
                gseaAnalyse(dps2, new Consumer<List<GseaAnalysisResult>>() {

                    @Override
                    public void accept(List<GseaAnalysisResult> second) {
                        double filter = getResultFilter();
                        GseaComparisonPartition partition =
                                new GseaComparisonPartition(first, second, filter);
                        showGseaResult(partition);
                        ComparisonAnalysisCompletedEvent event =
                                new ComparisonAnalysisCompletedEvent(comparison, partition);
                        eventBus.fireEventFromSource(event, ComparisonAnalysisDisplay.this);
                    }

                });
            }
 
        });
    }

    private void showBinomialResult(BinomialComparisonPartition partition) {
        Widget panel = new BinomialComparisonPanel(comparison, partition, eventBus);
        resultsPanel.setWidget(panel);
    }

    private void showGseaResult(GseaComparisonPartition partition) {
        Widget panel = new GseaComparisonPanel(comparison, partition, eventBus);
        resultsPanel.setWidget(panel);
    }

}
