package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaComparisonCompletedEvent extends GwtEvent<GseaComparisonCompletedHandler> {

    public static Type<GseaComparisonCompletedHandler> TYPE =
            new GwtEvent.Type<GseaComparisonCompletedHandler>();

    private Comparison comparison;
    
    private GseaComparisonPartition partition;

    public GseaComparisonCompletedEvent(Comparison comparison, GseaComparisonPartition partition) {
        this.comparison = comparison;
        this.partition = partition;
    }

    @Override
    public Type<GseaComparisonCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GseaComparisonCompletedHandler handler) {
        handler.onAnalysisCompleted(comparison, partition);
    }
}
