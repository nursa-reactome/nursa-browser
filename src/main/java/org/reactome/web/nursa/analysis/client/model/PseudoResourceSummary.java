package org.reactome.web.nursa.analysis.client.model;

import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.nursa.client.viewport.fireworks.NursaFireworksDisplay;
import org.reactome.web.nursa.client.viewport.fireworks.NursaFireworksPresenter;

public class PseudoResourceSummary implements ResourceSummary {

    /**
     * The 'pseudo' resource signals to the
     * {@link NursaFireworksDisplay} that the result is
     * derived from GSEA. The display uses this to avoid
     * filtering by species, which is a Reactome-specific
     * analysis operation that implicitly assumes that the
     * result was obtained from the Reactome analysis
     * client.
     * 
     * See the fuller discussion in
     * {@link NursaFireworksPresenter#onStateChanged(StateChangedEvent)}.
     */
    public static final String RESOURCE = "Pseudo";

    public PseudoResourceSummary() {
    }

    @Override
    public String getResource() {
        return PseudoResourceSummary.RESOURCE;
    }

    @Override
    public Integer getPathways() {
        return 0;
    }

    @Override
    public Integer getFiltered() {
        return 0;
    }

}
