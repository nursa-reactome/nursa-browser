package org.reactome.nursa.fireworks.client;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.EntityStatistics;

public class BinomialEntityStatistics implements EntityStatistics {

    private double pValue;
    private double fdr;

    public BinomialEntityStatistics(GseaAnalysisResult gsea) {
        pValue = gsea.getPvalue();
        fdr = gsea.getFdr();
    }

    @Override
    public String getResource() {
        return "GSEA";
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
