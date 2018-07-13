package org.reactome.web.nursa.fireworks.client;

import org.reactome.web.fireworks.client.FireworksCanvas;
import org.reactome.web.fireworks.legends.EnrichmentLegend;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.nursa.fireworks.legends.NursaEnrichmentLegend;

import com.google.gwt.event.shared.EventBus;

public class NursaFireworksCanvas extends FireworksCanvas {

    NursaFireworksCanvas(EventBus eventBus, Graph graph)
            throws FireworksCanvas.CanvasNotSupportedException {
        super(eventBus, graph);
    }

    @Override
    protected EnrichmentLegend createLegendPanel(EventBus eventBus) {
        return new NursaEnrichmentLegend(eventBus);
    }

}
