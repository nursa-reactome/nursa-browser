package org.reactome.web.nursa.client.common.events;

import org.reactome.nursa.model.Experiment;
import org.reactome.web.nursa.client.common.handlers.ExperimentSelectedHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ExperimentSelectedEvent extends GwtEvent<ExperimentSelectedHandler> {
    public static Type<ExperimentSelectedHandler> TYPE = new Type<>();
    
    private Experiment experiment;

    public ExperimentSelectedEvent(Experiment experiment) {
        this.experiment = experiment;
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
