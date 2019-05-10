package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.analysis.client.model.PseudoPathwaySummary;

public class BinomialComparisonPartition extends ComparisonPartition<PathwaySummary> {

    public BinomialComparisonPartition(List<PathwaySummary> result1, List<PathwaySummary> result2,
            double filter) {
        super(result1, result2, filter);
    }

    @Override
    protected String getKey(PathwaySummary result) {
        return result.getName();
    }

    @Override
    protected double getPvalue(PathwaySummary result) {
        return result.getEntities().getpValue();
    }

    @Override
    protected double getFdr(PathwaySummary result) {
        return result.getEntities().getFdr();
    }

    @Override
    protected PseudoPathwaySummary createPathwaySummary(
            PathwaySummary result, double pValue) {
        return new PseudoPathwaySummary(result, pValue);
    }

}
