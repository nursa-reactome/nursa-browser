package org.reactome.web.nursa.client.viewport.diagram;

import org.reactome.web.pwp.client.viewport.diagram.Diagram.Display;
import org.reactome.web.nursa.client.manager.state.PseudoState;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.viewport.diagram.DiagramPresenter;
import com.google.gwt.event.shared.EventBus;

public class NursaDiagramPresenter extends DiagramPresenter {

    public NursaDiagramPresenter(EventBus eventBus, Display display) {
        super(eventBus, display);
    }

    @Override
    /**
     * @see {@link NursaFireworksPresenter#onStateChanged(StateChangedEvent)
     */
    public void onStateChanged(StateChangedEvent event) {
        super.onStateChanged(event);
        if (this.display.isVisible()) {
            State state = event.getState();
            if (state instanceof PseudoState) {
                setBinomialAnalysisToken((PseudoState) state);
            }
        }
    }

    private void setBinomialAnalysisToken(PseudoState state) {
        ((NursaDiagramDisplay) this.display).setAnalysisToken(state.getAnalysisStatus());
    }

}
