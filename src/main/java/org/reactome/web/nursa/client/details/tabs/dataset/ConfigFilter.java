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

    private ConfigFilter(Double[] cutoffs, List<BiPredicate<Double, Double>> comparators,
            Conjunction[] conjunctions, int index) {
        while (cutoffs[index] == null && index < cutoffs.length) {
            index++;
        }
        if (index == cutoffs.length) {
            // The trivial filter.
            this.comparator = (value, cutoff) -> true;
        } else {
            this.cutoff = cutoffs[index];
            while (cutoffs[index] == null && index < cutoffs.length) {
                index++;
            }
            if (index == cutoffs.length - 1) {
                this.comparator = comparators.get(index);
            } else {
                this.other = new ConfigFilter(cutoffs, comparators, conjunctions, index + 1);
                this.comparator = comparators.get(index);
                this.conjunction = conjunctions[index];
            }
        }
    }

    public ConfigFilter(Double[] cutoffs, List<BiPredicate<Double, Double>> comparators,
            Conjunction[] conjunctions) {
        this(cutoffs, comparators, conjunctions, 0);
    }
    
    public boolean test(Double ...values) {
        boolean result = comparator.test(values[0], cutoff);
        if (this.other == null) {
            return result;
        }
        Double[] rest = Arrays.copyOfRange(values, 1, values.length);
        switch (this.conjunction) {
        case AND:
            return result && other.test(rest);
        case OR:
            return result || other.test(rest);
        default:
            String msg = "Unrecognized conjunction: " + this.conjunction;
            throw new IllegalArgumentException(msg);
        }
    }
    
}