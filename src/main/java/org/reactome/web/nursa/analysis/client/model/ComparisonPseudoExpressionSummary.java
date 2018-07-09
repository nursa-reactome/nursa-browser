package org.reactome.web.nursa.analysis.client.model;

import java.util.List;

import org.reactome.web.analysis.client.model.ComparisonExpressionSummary;
import org.reactome.web.analysis.client.model.ExpressionSummary;

/**
 * An {@link ExpressionSummary} with data set descriptors for the
 * expression legend labels and a min/max range of [0, .05].
 *  
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ComparisonPseudoExpressionSummary extends PseudoExpressionSummary
implements ComparisonExpressionSummary {

    public static final double COMPARISON_EXPRESSION_MIN = 0.0;
    public static final double COMPARISON_EXPRESSION_MAX = 0.05;
    
    private List<String> dataSetDescriptors;

    public ComparisonPseudoExpressionSummary(List<String> dataSetDescriptors) {
        // Base Reactome hard-codes min to 0 and max to 0.05 in various
        // places, so we must go along with that here.
        super(COMPARISON_EXPRESSION_MIN, COMPARISON_EXPRESSION_MAX);
        this.dataSetDescriptors = dataSetDescriptors;
    }

    @Override
    public List<String> getDataSetDescriptors() {
        return dataSetDescriptors;
    }

}
