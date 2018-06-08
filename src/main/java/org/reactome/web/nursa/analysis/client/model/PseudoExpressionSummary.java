package org.reactome.web.nursa.analysis.client.model;

import java.util.ArrayList;
import java.util.List;

import org.reactome.web.analysis.client.model.ExpressionSummary;

class PseudoExpressionSummary implements ExpressionSummary {
    
    private Double min;
    private Double max;
    private List<String> columns;
    
    PseudoExpressionSummary(Double min, Double max) {
        this.min = min;
        this.max = max;
        this.columns = new ArrayList<>();
    }
    
    @Override
    public Double getMin() {
        return min;
    }
    
    @Override
    public Double getMax() {
        return max;
    }
    
    @Override
    public List<String> getColumnNames() {
        return this.columns;
    }

}