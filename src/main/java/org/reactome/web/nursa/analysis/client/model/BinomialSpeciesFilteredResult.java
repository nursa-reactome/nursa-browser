package org.reactome.web.nursa.analysis.client.model;

import java.util.List;
import java.util.stream.Collectors;

import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwayBase;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;

public class BinomialSpeciesFilteredResult implements SpeciesFilteredResult {

    private List<PathwayBase> pathways;

    public BinomialSpeciesFilteredResult(PseudoAnalysisResult analysisResult) {
        pathways = analysisResult.getPathways().stream()
                .map(pathway -> new BinomialPathwayResult(pathway))
                .collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return getAnalysisType().toString();
    }

    @Override
    public ExpressionSummary getExpressionSummary() {
        return null;
    }

    @Override
    public List<PathwayBase> getPathways() {
        return pathways;
    }

    @Override
    public AnalysisType getAnalysisType() {
        return AnalysisType.OVERREPRESENTATION;
    }

    @Override
    public void setAnalysisType(AnalysisType analysisType) {
        if (analysisType != AnalysisType.OVERREPRESENTATION) {
            throw new UnsupportedOperationException("Analysis type is always " + getType());
        }
    }
}
