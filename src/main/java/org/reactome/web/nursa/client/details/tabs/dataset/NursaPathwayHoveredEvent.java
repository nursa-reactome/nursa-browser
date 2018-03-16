package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 *
 */
public class NursaPathwayHoveredEvent extends GwtEvent<NursaPathwayHoveredHandler> {
    public static Type<NursaPathwayHoveredHandler> TYPE = new Type<NursaPathwayHoveredHandler>();

    private String stId;

    public NursaPathwayHoveredEvent(String stId) {
        this.stId = stId;
    }

    @Override
    public Type<NursaPathwayHoveredHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NursaPathwayHoveredHandler handler) {
        handler.onPathwayHovered(stId);
    }
}
