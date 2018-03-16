package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.gsea.model.GseaAnalysisResult.Pathway;
import org.reactome.web.analysis.client.model.EntityStatistics;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ReactionStatistics;
import org.reactome.web.analysis.client.model.SpeciesSummary;

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class BinomialPathwaySummary implements PathwaySummary {
    
    static public BinomialPathwaySummary transform(GseaAnalysisResult result) {
        return new BinomialPathwaySummary(result);
    }
    
    private EntityStatistics entities;
    private Pathway pathway;
    private Long dbId;

    public BinomialPathwaySummary(GseaAnalysisResult result) {
        entities = new BinomialEntityStatistics(result);
        pathway = result.getPathway();
        // TODO - Look up the db id.
    }

    @Override
    public String getStId() {
        return pathway.getStId();
    }

    @Override
    public Long getDbId() {
        return dbId;
    }

    @Override
    public String getName() {
        return pathway.getName();
    }

    @Override
    public boolean getLlp() {
        return false;
    }

    @Override
    public SpeciesSummary getSpecies() {
        return null;
    }

    @Override
    public EntityStatistics getEntities() {
        return entities;
    }

    @Override
    public ReactionStatistics getReactions() {
        return null;
    }

}
