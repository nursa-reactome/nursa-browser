package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class AnalysisCompletedEvent extends GwtEvent<AnalysisCompletedHandler> {
    public static Type<AnalysisCompletedHandler> TYPE = new GwtEvent.Type<AnalysisCompletedHandler>();

    @Override
    public Type<AnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted();
    }
}
