package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.nursa.analysis.client.model.PseudoPathwaySummary;

public class GseaComparisonPartition extends ComparisonPartition<GseaAnalysisResult> {

    public GseaComparisonPartition(List<GseaAnalysisResult> result1, List<GseaAnalysisResult> result2,
            double filter) {
        super(result1, result2, filter);
    }

    @Override
    protected String getKey(GseaAnalysisResult result) {
        return result.getPathway().getName();
    }

    @Override
    protected double getPvalue(GseaAnalysisResult result) {
        return result.getPvalue();
    }

    @Override
    protected double getFdr(GseaAnalysisResult result) {
        return result.getFdr();
    }

    @Override
    protected PseudoPathwaySummary createPathwaySummary(
            GseaAnalysisResult result, double pValue) {
        return new PseudoPathwaySummary(result, pValue);
    }

}
