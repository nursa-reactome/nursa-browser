package org.reactome.web.nursa.client.details.tabs.dataset;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialHoveredEvent extends NursaPathwayHoveredEvent<Long> {
    public static Type<PathwayLoader> TYPE = new Type<PathwayLoader>();

    public BinomialHoveredEvent(Long dbId) {
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
