package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.analysis.client.model.PseudoPathwaySummary;

public class BinomialComparisonPartition extends ComparisonPartition<PathwaySummary> {

    public BinomialComparisonPartition(
            Iterable<PathwaySummary> result1,
            Iterable<PathwaySummary> result2) {
        super(result1, result2);
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
