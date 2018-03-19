package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.stream.Collectors;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class BinomialAnalysisResult implements AnalysisResult {
     
    private List<PathwaySummary> pathways;

    public BinomialAnalysisResult(List<GseaAnalysisResult> gseaResult) {
        pathways = gseaResult.stream()
                             .map(BinomialPathwaySummary::transform)
                             .collect(Collectors.toList());
    }

    @Override
    public AnalysisSummary getSummary() {
        return null;
    }

    @Override
    public Integer getPathwaysFound() {
        return 0;
    }

    @Override
    public Integer getIdentifiersNotFound() {
        return 0;
    }

    @Override
    public List<PathwaySummary> getPathways() {
        return pathways;
    }

    @Override
    public List<ResourceSummary> getResourceSummary() {
        return null;
    }

    @Override
    public ExpressionSummary getExpression() {
        return null;
    }

    @Override
    public List<String> getWarnings() {
        return null;
    }

}
