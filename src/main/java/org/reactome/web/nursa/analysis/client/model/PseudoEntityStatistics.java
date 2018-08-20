package org.reactome.web.nursa.analysis.client.model;

import java.util.Arrays;
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
        return 1;
    }

    @Override
    public Integer getFound() {
        return 1;
    }

    @Override
    public Double getRatio() {
        return 1.0;
    }

    @Override
    public Integer getCuratedTotal() {
        return 1;
    }

    @Override
    public Integer getCuratedFound() {
        return 1;
    }

    @Override
    public Integer getInteractorsTotal() {
        return 1;
    }

    @Override
    public Integer getInteractorsFound() {
        return 1;
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
        return Arrays.asList(0.0);
    }

}
