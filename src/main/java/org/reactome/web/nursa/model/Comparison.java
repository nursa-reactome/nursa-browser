package org.reactome.web.nursa.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;

public class Comparison {

    /** The comparison operand labels presented to the user. */
    public static final String[] LABELS = { "First", "Second" };

    public static class Operand {
        public DataSet dataset;
        public Experiment experiment;
        public List<DisplayableDataPoint> dataPoints;

        Operand(DataSet dataset, Experiment experiment, List<DisplayableDataPoint> dataPoints) {
            this.dataset = dataset;
            this.experiment = experiment;
            this.dataPoints = dataPoints;
        }
    }
    
    /** The comparison operands pair. */
    public Operand[] operands;
    
    public Comparison(
            DataSet dataset1, Experiment experiment1, List<DisplayableDataPoint> dataPoints1,
            DataSet dataset2, Experiment experiment2, List<DisplayableDataPoint> dataPoints2) {
        this(new Operand(dataset1, experiment1, dataPoints1),
             new Operand(dataset2, experiment2, dataPoints2));
    }
    
    private Comparison(final Operand... operands) {
        this.operands = operands;
    }
    
    public List<List<DisplayableDataPoint>> dataPoints() {
        return Stream.of(this.operands)
                     .map(oper -> oper.dataPoints)
                     .collect(Collectors.toList());
    }

    public List<Experiment> experiments() {
        return Stream.of(this.operands)
                .map(oper -> oper.experiment)
                .collect(Collectors.toList());
    }

}
