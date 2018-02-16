package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.analysis.client.model.AnalysisResult;

import com.google.gwt.event.shared.GwtEvent;

public class BinomialAnalysisCompletedEvent extends GwtEvent<BinomialAnalysisCompletedHandler> {
    private AnalysisResult result;

    public static Type<BinomialAnalysisCompletedHandler> TYPE = new GwtEvent.Type<BinomialAnalysisCompletedHandler>();

    public BinomialAnalysisCompletedEvent(AnalysisResult result) {
        super();
        this.result = result;
    }

    @Override
    public Type<BinomialAnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BinomialAnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted(result);
    }

    public AnalysisResult getResult() {
        return result;
    }
}
