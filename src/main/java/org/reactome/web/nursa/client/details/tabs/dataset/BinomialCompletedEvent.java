package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.analysis.client.model.AnalysisResult;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialCompletedEvent extends GwtEvent<BinomialCompletedHandler> {
    private AnalysisResult result;

    public static Type<BinomialCompletedHandler> TYPE =
            new GwtEvent.Type<BinomialCompletedHandler>();

    public BinomialCompletedEvent(AnalysisResult result) {
        this.result = result;
    }

    @Override
    public Type<BinomialCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BinomialCompletedHandler handler) {
        handler.onAnalysisCompleted(result);
    }

    public AnalysisResult getResult() {
        return result;
    }
}
