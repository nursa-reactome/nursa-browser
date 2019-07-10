package org.reactome.web.nursa.fireworks.client;

import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.fireworks.client.FireworksCanvas;
import org.reactome.web.fireworks.legends.EnrichmentControl;
import org.reactome.web.fireworks.legends.EnrichmentLegend;
import org.reactome.web.fireworks.model.Edge;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.nursa.fireworks.controls.settings.NursaHideableContainerPanel;
import org.reactome.web.nursa.fireworks.legends.NursaEnrichmentLegend;

import com.google.gwt.canvas.dom.client.Context2d;
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

    @Override
    protected EnrichmentControl createEnrichmentControl(EventBus eventBus) {
        return new EnrichmentControl(eventBus);
    }

    @Override
    protected void drawNode(int column, Context2d ctx, Node node) {
        if (AnalysisType.DATASET_COMPARISON.equals(analysisInfo.getType())) {
            String nodeColour = node.getColour();
            ctx.setFillStyle(nodeColour);
            ctx.setStrokeStyle(nodeColour);
            node.draw(ctx);
       } else {
           super.drawNode(column, ctx, node);
       }
    }

    @Override
    protected void drawEdge(int column, Context2d ctx, Edge edge) {
        if (AnalysisType.DATASET_COMPARISON.equals(analysisInfo.getType())) {
            String edgeColour = edge.getColour();
            ctx.setStrokeStyle(edgeColour);
            edge.draw(ctx);
       } else {
           super.drawEdge(column, ctx, edge);
       }
   }

}
