package org.reactome.web.nursa.client.details.tabs.dataset;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaHoveredEvent extends NursaPathwayHoveredEvent<String> {
    public static Type<PathwayLoader> TYPE = new Type<PathwayLoader>();

    public GseaHoveredEvent(String stId) {
        super(stId);
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
