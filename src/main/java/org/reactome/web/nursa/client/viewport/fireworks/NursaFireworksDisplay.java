package org.reactome.web.nursa.client.viewport.fireworks;

import org.reactome.nursa.fireworks.client.NursaFireworksViewer;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.pwp.client.viewport.fireworks.FireworksDisplay;

public class NursaFireworksDisplay extends FireworksDisplay {

    @Override
    protected FireworksViewer createFireworksViewer(String json) {
        return new NursaFireworksViewer(json);
    }

    public void setAnalysisResult(SpeciesFilteredResult result) {
        ((NursaFireworksViewer) this.fireworks).setPathwayAnalysisResult(result);
    }

}
