package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaAnalysisCompletedEvent extends GwtEvent<GseaAnalysisCompletedHandler> {
    public static Type<GseaAnalysisCompletedHandler> TYPE = new GwtEvent.Type<GseaAnalysisCompletedHandler>();

    @Override
    public Type<GseaAnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GseaAnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted();
    }
}
