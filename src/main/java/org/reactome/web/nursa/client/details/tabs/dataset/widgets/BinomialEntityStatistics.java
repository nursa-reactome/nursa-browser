package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.EntityStatistics;

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class BinomialEntityStatistics implements EntityStatistics {

    private double fdr;
    private double pValue;

    public BinomialEntityStatistics(GseaAnalysisResult result) {
        this.fdr = result.getFdr();
        this.pValue = result.getPvalue();
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
