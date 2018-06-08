package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaCompletedEvent extends GwtEvent<GseaCompletedHandler> {
    public static Type<GseaCompletedHandler> TYPE = new GwtEvent.Type<GseaCompletedHandler>();

    private List<GseaAnalysisResult> result;

    public GseaCompletedEvent(List<GseaAnalysisResult> result) {
        this.result = result;
    }

    @Override
    public Type<GseaCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GseaCompletedHandler handler) {
        handler.onAnalysisCompleted(result);
    }
}
