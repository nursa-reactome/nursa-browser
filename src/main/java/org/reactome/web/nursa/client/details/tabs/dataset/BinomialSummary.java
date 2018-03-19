package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.analysis.client.model.AnalysisSummary;

/**
 * 
 * Minimal AnalysisSummary implementation. Reactome raises an error
 * if an analysis result does not have a summary with a token.
 * This implementation has an empty token.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialSummary implements AnalysisSummary {

    private String speciesName;

    @Override
    public String getToken() {
        return "";
    }

    @Override
    public Boolean getProjection() {
        return false;
    }

    @Override
    public Boolean getInteractors() {
        return false;
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public Long getSpecies() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public String getSampleName() {
        return null;
    }

    @Override
    public void setSpeciesName(String name) {
        speciesName = name;
    }

    @Override
    public String getSpeciesName() {
        return speciesName;
    }

}
