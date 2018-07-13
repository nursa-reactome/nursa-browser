package org.reactome.web.nursa.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

public class Comparison {

    /** The comparison operand labels presented to the user. */
    public static final String[] LABELS = { "First", "Second" };

    public static class Operand {
        public DataSet dataset;
        public Experiment experiment;

        Operand(DataSet dataset, Experiment experiment) {
            this.dataset = dataset;
            this.experiment = experiment;
        }
    }
    
    /** The comparison operands pair. */
    public Operand[] operands;
    
    public Comparison(
            DataSet dataset1, Experiment experiment1,
            DataSet dataset2, Experiment experiment2) {
        this(new Operand(dataset1, experiment1), new Operand(dataset2, experiment2));
    }
    
    private Comparison(final Operand... operands) {
        this.operands = operands;
    }
    
    public List<Experiment> experiments() {
        return Stream.of(this.operands)
                     .map(oper -> oper.experiment)
                     .collect(Collectors.toList());
    }

}
