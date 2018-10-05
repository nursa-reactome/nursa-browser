package org.reactome.web.nursa.client.search;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ExperimentLoadedEvent extends GwtEvent<ExperimentLoadedHandler> {
    public static Type<ExperimentLoadedHandler> TYPE = new Type<>();
    
    private DataSet dataset;
    private Experiment experiment;

    public ExperimentLoadedEvent(DataSet dataset, Experiment experiment) {
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
    public Type<ExperimentLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(ExperimentLoadedHandler handler) {
        handler.onExperimentLoaded(this);
    }
}
