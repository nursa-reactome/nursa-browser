package org.reactome.web.nursa.client.details.tabs.dataset;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaSelectedEvent extends NursaPathwaySelectedEvent<String> {
    public static Type<NursaPathwaySelectedHandler> TYPE =
            new Type<NursaPathwaySelectedHandler>();

    public GseaSelectedEvent(String stId) {
        super(stId);
    }

    @Override
    public Type<NursaPathwaySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NursaPathwaySelectedHandler handler) {
        handler.onPathwaySelected(getKey());
    }

}
