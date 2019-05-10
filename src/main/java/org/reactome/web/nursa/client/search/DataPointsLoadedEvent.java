package org.reactome.web.nursa.client.search;

import java.util.List;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataPointsLoadedEvent extends GwtEvent<ExperimentLoadedHandler> {
    public static Type<ExperimentLoadedHandler> TYPE = new Type<>();
    
    private DataSet dataset;
    private Experiment experiment;
    private List<DisplayableDataPoint> dataPoints;

    public DataPointsLoadedEvent(DataSet dataset, Experiment experiment, List<DisplayableDataPoint> dataPoints) {
        this.dataset = dataset;
        this.experiment = experiment;
        this.dataPoints = dataPoints;
    }

    public DataSet getDataSet() {
        return dataset;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public List<DisplayableDataPoint> getDataPoints() {
        return dataPoints;
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
