package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 *
 */
public class NursaPathwaySelectedEvent extends GwtEvent<NursaPathwaySelectedHandler> {
    public static Type<NursaPathwaySelectedHandler> TYPE = new Type<NursaPathwaySelectedHandler>();

    private String stId;

    public NursaPathwaySelectedEvent(String stId) {
        this.stId = stId;
    }

    @Override
    public Type<NursaPathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NursaPathwaySelectedHandler handler) {
        handler.onPathwaySelected(stId);
    }
}
