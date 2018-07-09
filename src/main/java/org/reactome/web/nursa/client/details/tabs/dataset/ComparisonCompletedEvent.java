package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ComparisonCompletedEvent extends GwtEvent<ComparisonCompletedHandler> {

    public static Type<ComparisonCompletedHandler> TYPE =
            new GwtEvent.Type<ComparisonCompletedHandler>();

    private Comparison comparison;
    
    private ComparisonPartition<?> partition;

    public ComparisonCompletedEvent(Comparison comparison, ComparisonPartition<?> partition) {
        this.comparison = comparison;
        this.partition = partition;
    }

    @Override
    public Type<ComparisonCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ComparisonCompletedHandler handler) {
        handler.onAnalysisCompleted(comparison, partition);
    }
}
