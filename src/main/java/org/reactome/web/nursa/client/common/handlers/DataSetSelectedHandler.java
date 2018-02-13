package org.reactome.web.nursa.client.common.handlers;

import org.reactome.web.nursa.client.common.events.DataSetSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface DataSetSelectedHandler extends EventHandler {

    void onDataSetSelected(DataSetSelectedEvent datasetSelectedEvent);

}
