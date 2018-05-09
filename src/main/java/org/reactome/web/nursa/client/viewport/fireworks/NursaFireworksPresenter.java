package org.reactome.web.nursa.client.viewport.fireworks;

import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.client.manager.state.BinomialState;
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
    public void onStateChanged(StateChangedEvent event) {
        super.onStateChanged(event);
        if (this.display.isVisible()) {
            State state = event.getState();
            if (state instanceof BinomialState) {
                BinomialState binomialState = (BinomialState) state;
                SpeciesFilteredResult result = binomialState.getResult();
                ((NursaFireworksDisplay) this.display).setAnalysisResult(result);
            }
        }
    }

}
