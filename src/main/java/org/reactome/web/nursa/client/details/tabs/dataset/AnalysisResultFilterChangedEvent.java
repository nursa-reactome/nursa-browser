package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

public class AnalysisResultFilterChangedEvent
extends GwtEvent<AnalysisResultFilterChangedHandler> {

    public static Type<AnalysisResultFilterChangedHandler> TYPE =
            new GwtEvent.Type<AnalysisResultFilterChangedHandler>();

    private double filter;

    public AnalysisResultFilterChangedEvent(double filter) {
        this.filter = filter;
    }

    @Override
    public Type<AnalysisResultFilterChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisResultFilterChangedHandler handler) {
        handler.onFilterChanged(filter);
    }

}
