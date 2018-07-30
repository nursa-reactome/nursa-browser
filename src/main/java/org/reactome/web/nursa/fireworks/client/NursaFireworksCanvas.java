package org.reactome.web.nursa.fireworks.client;

import org.reactome.web.fireworks.client.FireworksCanvas;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.nursa.fireworks.controls.settings.NursaHideableContainerPanel;
import org.reactome.web.nursa.fireworks.legends.NursaEnrichmentLegend;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Panel;

public class NursaFireworksCanvas extends FireworksCanvas {

    NursaFireworksCanvas(EventBus eventBus, Graph graph)
            throws FireworksCanvas.CanvasNotSupportedException {
        super(eventBus, graph);
    }

    @Override
    protected Panel createHideablePanel(EventBus eventBus) {
        return new NursaHideableContainerPanel(eventBus);
    }

    @Override
    protected Panel createLegendPanel(EventBus eventBus) {
        return new NursaEnrichmentLegend(eventBus);
    }

}
