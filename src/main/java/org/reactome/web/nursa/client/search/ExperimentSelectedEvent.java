package org.reactome.web.nursa.client.search;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ExperimentSelectedEvent extends GwtEvent<ExperimentSelectedHandler> {
    public static Type<ExperimentSelectedHandler> TYPE = new Type<>();
    
    private DataSet dataset;
    private Experiment experiment;

    public ExperimentSelectedEvent(DataSet dataset, Experiment experiment) {
        this.dataset = dataset;
        this.experiment = experiment;
    }

    public DataSet getDataSet() {
        return dataset;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public Type<ExperimentSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(ExperimentSelectedHandler handler) {
        handler.onExperimentSelected(this);
    }
}
