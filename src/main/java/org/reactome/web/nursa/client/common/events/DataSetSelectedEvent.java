package org.reactome.web.nursa.client.common.events;

import org.reactome.nursa.model.DataSet;
import org.reactome.web.nursa.client.common.handlers.DataSetSelectedHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSelectedEvent extends GwtEvent<DataSetSelectedHandler> {
    public static Type<DataSetSelectedHandler> TYPE = new Type<>();
    
    private DataSet dataset;

    public DataSetSelectedEvent(DataSet dataset) {
        this.dataset = dataset;
    }

    public DataSet getDataSet() {
        return dataset;
    }

    @Override
    public Type<DataSetSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(DataSetSelectedHandler handler) {
        handler.onDataSetSelected(this);
    }
}
