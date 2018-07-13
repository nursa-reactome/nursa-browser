package org.reactome.web.nursa.analysis.client.model;

import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwayBase;

/**
 * An {@link ExpressionSummary} for a gene data set comparison.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface ComparisonExpressionSummary extends ExpressionSummary {
    
    String SHARED_LABEL = "Shared";
    
    // These values are constrained by the base Reactome,
    // which hard-codes the threshold values in various places.
    double[] UNSHARED_PSEUDO_VALUES = { 0.0, 0.5 };
    
    // Midway between the unshared values.
    double SHARED_PSEUDO_VALUE = 0.025;
    
    String[] UNSHARED_COLOURS = { "MidnightBlue", "MediumSeaGreen" };
    
    String SHARED_COLOUR = "Maroon";
    
    public static String getColour(PathwayBase pathway) {
        double pValue = pathway.getEntities().getpValue();
        for (int i=0; i < UNSHARED_PSEUDO_VALUES.length; i++) {
            if (pValue == UNSHARED_PSEUDO_VALUES[i]) {
                return UNSHARED_COLOURS[i];
            }
        }
        if (pValue == SHARED_PSEUDO_VALUE) {
            return SHARED_COLOUR;
        }
        String message =
                "The pathway p-value is not a comparison p-value: " + pValue;
        throw new IllegalArgumentException(message);
    }

}
