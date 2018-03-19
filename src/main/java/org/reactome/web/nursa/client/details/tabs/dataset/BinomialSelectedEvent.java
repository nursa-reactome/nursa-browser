package org.reactome.web.nursa.client.details.tabs.dataset;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialSelectedEvent extends NursaPathwaySelectedEvent<Long> {
    public static Type<PathwayLoader> TYPE = new Type<PathwayLoader>();

    public BinomialSelectedEvent(Long dbId) {
        super(dbId);
    }

    @Override
    public Type<PathwayLoader> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PathwayLoader handler) {
        handler.load(getKey());
    }
}
