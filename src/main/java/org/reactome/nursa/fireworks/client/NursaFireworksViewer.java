package org.reactome.nursa.fireworks.client;

import org.reactome.web.fireworks.client.FireworksViewerImpl;
import org.reactome.web.nursa.analysis.client.model.BinomialResult;

public class NursaFireworksViewer extends FireworksViewerImpl {

    public NursaFireworksViewer(String json) {
        super(json);
    }

    @Override
    protected void filterResultBySpecies(String token, String resource) {
        // This is a no-op for GSEA.
        if (!BinomialResult.GSEA_RESOURCE.equals(resource)) {
            super.filterResultBySpecies(token, resource);
        }
    }

}
