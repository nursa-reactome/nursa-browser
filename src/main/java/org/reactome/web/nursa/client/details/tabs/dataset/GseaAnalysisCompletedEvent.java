package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaAnalysisCompletedEvent extends GwtEvent<GseaAnalysisCompletedHandler> {
    public static Type<GseaAnalysisCompletedHandler> TYPE = new GwtEvent.Type<GseaAnalysisCompletedHandler>();

    private List<GseaAnalysisResult> result;

    public GseaAnalysisCompletedEvent(List<GseaAnalysisResult> result) {
        this.result = result;
    }

    public List<GseaAnalysisResult> getResult() {
        return result;
    }

    @Override
    public Type<GseaAnalysisCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GseaAnalysisCompletedHandler handler) {
        handler.onAnalysisCompleted(result);
    }
}
