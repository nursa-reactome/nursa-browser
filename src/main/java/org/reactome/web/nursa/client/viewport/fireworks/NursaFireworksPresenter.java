package org.reactome.web.nursa.client.viewport.fireworks;

import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.client.manager.state.PseudoState;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.viewport.fireworks.Fireworks.Display;
import org.reactome.web.pwp.client.viewport.fireworks.FireworksPresenter;

import com.google.gwt.event.shared.EventBus;

public class NursaFireworksPresenter extends FireworksPresenter {

    public NursaFireworksPresenter(EventBus eventBus, Display display) {
        super(eventBus, display);
    }

    @Override
    /**
     * The state can be built from a Reactome binomial analysis,
     * a GSEA analysis or an analysis comparison. If it is a
     * binomial analyisis, then the superclass state changed
     * handler filters for species, which in turn sets the
     * viewer analysis result. If GSEA or comparison, then
     * the analysis result is mocked as a BinomialResult.
     * The NursaStateManager analysis completed handler checks
     * for a BinomialResult and makes a BinomialState rather than
     * a normal Reactome State. The BinomialState makes a
     * BinomialSpeciesFilteredResult which mocks the normal
     * Reactome SpeciesFilteredResult. The code below then
     * detects a GSEA analysis and compensates for bypassing
     * the species filtering performed on a normal Reactome
     * analysis result in the superclass state changed handler
     * by directly setting the display result to the pseudo-filtered
     * result created by the BinomialState. The display is
     * thereby enabled to show the over-representation overlay.
     */
    public void onStateChanged(StateChangedEvent event) {
        super.onStateChanged(event);
        if (this.display.isVisible()) {
            State state = event.getState();
            if (state instanceof PseudoState) {
                setBinomialAnalysisResult((PseudoState) state);
            }
        }
    }

    private void setBinomialAnalysisResult(PseudoState state) {
        SpeciesFilteredResult result = state.getResult();
        ((NursaFireworksDisplay) this.display).setAnalysisResult(result);
    }

}
