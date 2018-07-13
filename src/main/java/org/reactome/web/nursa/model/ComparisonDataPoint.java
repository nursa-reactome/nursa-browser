package org.reactome.web.nursa.model;

import org.reactome.nursa.model.DataPoint;

public class ComparisonDataPoint {

    private String symbol;
    private DataPoint[] dps;
    
    public ComparisonDataPoint(String symbol, DataPoint[] dataPoints) {
        this.symbol = symbol;
        this.dps = dataPoints;
    }
    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public DataPoint[] getDataPoints() {
        return dps;
    }
    
    /**
     * @return the log10(FDR1/FDR2) ratio
     */
    public Double getRatio() {
        DataPoint dp1 = getDataPoints()[0];
        Double value1 = getFDR(dp1);
        DataPoint dp2 = getDataPoints()[1];
        Double value2 = getFDR(dp2);
        return value1 == null || value2 == null ? null : Math.log10(value1 / value2);
    }

    protected Double getFDR(DataPoint dp) {
        if (dp == null) {
             return null;
        }
        double fdr = dp.getPvalue();
        return fdr == 0.0 ? Double.MIN_VALUE : fdr;
    }

}
