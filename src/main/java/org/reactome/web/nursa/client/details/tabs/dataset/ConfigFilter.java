package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

public class ConfigFilter {
    
    public static enum Conjunction {
        AND,
        OR
    }
    
    private Double cutoff;
    private BiPredicate<Double, Double> comparator;
    private ConfigFilter other;
    private Conjunction conjunction;

    /**
     * Creates a value array filter.
     * 
     * @param cutoffs the N values to compare against
     * @param comparators the N value/cutofff comparison predicates
     * @param conjunctions the N-1 AND/OR compound filter logical operations
     */
    public ConfigFilter(Double[] cutoffs, List<BiPredicate<Double, Double>> comparators,
            Conjunction[] conjunctions) {
        this(cutoffs, comparators, conjunctions, 0);
    }

    private ConfigFilter(Double[] cutoffs, List<BiPredicate<Double, Double>> comparators,
            Conjunction[] conjunctions, int index) {
        cutoff = cutoffs[index];
        comparator = comparators.get(index);
        if (index < cutoffs.length - 1) {
            // Make another filter for the remaining cutoffs.
            other = new ConfigFilter(cutoffs, comparators, conjunctions, index + 1);
            // AND/OR the current comparison with the other filter.
            if (cutoff != null) {
                conjunction = conjunctions[index];
            }
        }
    }
    
    public boolean test(Double ...values) {
        // The remaining values to test.
        Double[] rest = Arrays.copyOfRange(values, 1, values.length);
        // A missing cutoff always passes the test.
        if (cutoff == null) {
            return other == null || other.test(rest);
        }
        boolean result = comparator.test(values[0], cutoff);
        if (other == null) {
            return result;
        }
        // AND/OR the first test result against the other filter result.
        switch (this.conjunction) {
        case AND:
            return result && other.test(rest);
        case OR:
            return result || other.test(rest);
        default:
            // Should never occur by construction.
            String msg = "Unrecognized conjunction: " + this.conjunction;
            throw new IllegalArgumentException(msg);
        }
    }
    
}