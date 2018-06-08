package org.reactome.web.nursa.analysis.client.model;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.EntityStatistics;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ReactionStatistics;
import org.reactome.web.analysis.client.model.SpeciesSummary;

public class PseudoPathwaySummary implements PathwaySummary {

    private String stId;
    private Long dbId;
    private String name;
    private EntityStatistics entities;
    
    public PseudoPathwaySummary(PathwaySummary result, double pseudoPvalue) {
        this.name = result.getName();
        this.stId = result.getStId();
        this.dbId = result.getDbId();
        this.entities = new PseudoEntityStatistics(0.0, pseudoPvalue);
    }
    
    public PseudoPathwaySummary(GseaAnalysisResult result) {
        this.name = result.getPathway().getName();
        this.stId = result.getPathway().getStId();
        this.entities = new PseudoEntityStatistics(result);
    }
    
    public PseudoPathwaySummary(GseaAnalysisResult result, double pseudoPvalue) {
        this.name = result.getPathway().getName();
        this.stId = result.getPathway().getStId();
        this.entities = new PseudoEntityStatistics(0.0, pseudoPvalue);
    }

    @Override
    public String getStId() {
        return stId;
    }

    @Override
    public Long getDbId() {
        return dbId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EntityStatistics getEntities() {
        return entities;
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
    public ReactionStatistics getReactions() {
        return null;
    }

}