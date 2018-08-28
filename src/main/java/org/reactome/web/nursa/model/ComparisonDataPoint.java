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
     * @return the log10(pvalue1/pvalue2) ratio
     */
    public Double getPValueRatio() {
        DataPoint dp1 = getDataPoints()[0];
        Double value1 = getPvalue(dp1);
        DataPoint dp2 = getDataPoints()[1];
        Double value2 = getPvalue(dp2);
        return value1 == null || value2 == null ? null : Math.log10(value1 / value2);
    }
    
    /**
     * @return FC1 - FC2
     */
    public Double getFCDifference() {
        DataPoint dp1 = getDataPoints()[0];
        Double fc1 = dp1 == null ? null : dp1.getFoldChange();
        DataPoint dp2 = getDataPoints()[1];
        Double fc2 = dp2 == null ? null : dp2.getFoldChange();
        return fc1 == null || fc2 == null ? null : fc2 - fc1;
    }

    private Double getPvalue(DataPoint dp) {
        if (dp == null) {
             return null;
        }
        double pValue = dp.getPvalue();
        return pValue == 0.0 ? Double.MIN_VALUE : pValue;
    }

}
