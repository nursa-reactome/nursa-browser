package org.reactome.web.nursa.client.viewport.diagram;

import org.reactome.web.nursa.diagram.client.NursaDiagramViewer;
import org.reactome.web.pwp.client.common.AnalysisStatus;
import org.reactome.web.pwp.client.viewport.diagram.DiagramDisplay;

public class NursaDiagramDisplay extends DiagramDisplay {
    
    public NursaDiagramDisplay() {
        super(new NursaDiagramViewer());
    }

    @Override
    public void setAnalysisToken(AnalysisStatus analysisStatus) {
        ((NursaDiagramViewer) this.diagram).setAnalysisToken(analysisStatus.getToken(), analysisStatus.getResource());
    }

}
