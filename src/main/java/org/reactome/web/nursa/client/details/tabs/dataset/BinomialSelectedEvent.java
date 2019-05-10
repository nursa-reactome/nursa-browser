package org.reactome.web.nursa.client.details.tabs.dataset;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialSelectedEvent extends NursaPathwaySelectedEvent<Long> {
    public static Type<NursaPathwaySelectedHandler> TYPE =
            new Type<NursaPathwaySelectedHandler>();

    public BinomialSelectedEvent(Long dbId) {
        super(dbId);
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
