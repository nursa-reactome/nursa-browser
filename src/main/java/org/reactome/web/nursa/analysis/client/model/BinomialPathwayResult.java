package org.reactome.web.nursa.analysis.client.model;

import org.reactome.web.analysis.client.model.EntityStatistics;
import org.reactome.web.analysis.client.model.PathwayBase;
import org.reactome.web.analysis.client.model.PathwaySummary;

public class BinomialPathwayResult implements PathwayBase {

    private String stId;
    private EntityStatistics stats;

    public BinomialPathwayResult(PathwaySummary summary) {
        stId = summary.getStId();
        stats = summary.getEntities();
    }

    @Override
    public String getStId() {
        return stId;
    }

    @Override
    public Long getDbId() {
        return null;
    }

    @Override
    public EntityStatistics getEntities() {
        return stats;
    }

}
