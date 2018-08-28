package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.function.Consumer;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.Experiment;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonAnalysisCompletedEvent;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;

public class ComparisonPathwayPanel extends PathwayPanel {

    private Comparison comparison;

    public ComparisonPathwayPanel(Comparison comparison, EventBus eventBus) {
        super(eventBus);
        this.comparison = comparison;
    }

    @Override
    protected void binomialAnalyse() {
        List<Experiment> exps = comparison.experiments();
        Experiment exp1 = exps.get(0);
        Experiment exp2 = exps.get(1);
        binomialAnalyse(exp1, new Consumer<AnalysisResult>() {
            
            @Override
            public void accept(AnalysisResult first) {
                binomialAnalyse(exp2, new Consumer<AnalysisResult>() {

                    @Override
                    public void accept(AnalysisResult second) {
                        BinomialComparisonPartition partition =
                                new BinomialComparisonPartition(
                                        first.getPathways(),
                                        second.getPathways());
                        showBinomialResult(partition);
                        ComparisonAnalysisCompletedEvent event =
                                new ComparisonAnalysisCompletedEvent(comparison, partition);
                        eventBus.fireEventFromSource(event, ComparisonPathwayPanel.this);
                    }

                });
            }
 
        });
    }

    @Override
    protected void gseaAnalyse() {
        List<Experiment> exps = comparison.experiments();
        Experiment exp1 = exps.get(0);
        Experiment exp2 = exps.get(1);
        gseaAnalyse(exp1, new Consumer<List<GseaAnalysisResult>>() {
            
            @Override
            public void accept(List<GseaAnalysisResult> first) {
                gseaAnalyse(exp2, new Consumer<List<GseaAnalysisResult>>() {

                    @Override
                    public void accept(List<GseaAnalysisResult> second) {
                        GseaComparisonPartition partition =
                                new GseaComparisonPartition(first, second);
                        showGseaResult(partition);
                        ComparisonAnalysisCompletedEvent event =
                                new ComparisonAnalysisCompletedEvent(comparison, partition);
                        eventBus.fireEventFromSource(event, ComparisonPathwayPanel.this);
                    }

                });
            }
 
        });
    }

    private void showBinomialResult(ComparisonPartition<PathwaySummary> partition) {
        Widget panel = new BinomialComparisonPanel(comparison, partition, eventBus);
        analysisPanel.setWidget(panel);
    }

    private void showGseaResult(GseaComparisonPartition partition) {
        Widget panel = new GseaComparisonPanel(comparison, partition, eventBus);
        analysisPanel.setWidget(panel);
    }

}
