package org.reactome.web.nursa.analysis.client.model;

import org.reactome.web.analysis.client.model.AnalysisSummary;

/**
 * This class is a minimal AnalysisSummary implementation which
 * has only a token and a settable species name.
 * 
 * The caller is responsible for ensuring that the token is distinct
 * for each submitted analysis input.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class PseudoAnalysisSummary implements AnalysisSummary {

    private String speciesName;
    private String token;

    public PseudoAnalysisSummary(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
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
