package org.reactome.web.nursa.analysis.client.model;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.EntityStatistics;

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class PseudoEntityStatistics implements EntityStatistics {

    private double fdr;
    private double pValue;

    public PseudoEntityStatistics(double fdr, double pValue) {
        this.fdr = fdr;
        this.pValue = pValue;
    }

    public PseudoEntityStatistics(GseaAnalysisResult result) {
        this(result.getFdr(), result.getPvalue());
    }

    @Override
    public String getResource() {
        return null;
    }

    @Override
    public Integer getTotal() {
        return null;
    }

    @Override
    public Integer getFound() {
        return null;
    }

    @Override
    public Double getRatio() {
        return null;
    }

    @Override
    public Integer getCuratedTotal() {
        return null;
    }

    @Override
    public Integer getCuratedFound() {
        return null;
    }

    @Override
    public Integer getInteractorsTotal() {
        return null;
    }

    @Override
    public Integer getInteractorsFound() {
        return null;
    }

    @Override
    public Double getpValue() {
        return pValue;
    }

    @Override
    public Double getFdr() {
        return fdr;
    }

    @Override
    public List<Double> getExp() {
        return null;
    }

}
