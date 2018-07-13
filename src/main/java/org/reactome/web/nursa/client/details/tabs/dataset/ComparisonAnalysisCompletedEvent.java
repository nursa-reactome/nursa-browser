package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.nursa.model.Comparison;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ComparisonAnalysisCompletedEvent extends GwtEvent<ComparisonAnalysisCompletedHandler> {

    public static Type<ComparisonAnalysisCompletedHandler> TYPE =
            new GwtEvent.Type<ComparisonAnalysisCompletedHandler>();

    private Comparison comparison;
    
    private ComparisonPartition<?> partition;

    public ComparisonAnalysisCompletedEvent(Comparison comparison, ComparisonPartition<?> partition) {
        this.comparison = comparison;
        this.partition = partition;
    }

    @Override
    public Type<ComparisonAnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ComparisonAnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted(comparison, partition);
    }
}
