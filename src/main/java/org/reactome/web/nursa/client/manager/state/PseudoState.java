package org.reactome.web.nursa.client.manager.state;

import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.analysis.client.model.PseudoAnalysisResult;
import org.reactome.web.pwp.client.manager.state.State;

public class PseudoState extends State {
    
    private SpeciesFilteredResult result;

    public PseudoState(State currentState, PseudoAnalysisResult analysisResult) {
        super(currentState);
        result = analysisResult.asSpeciesFilteredResult();
    }

    public SpeciesFilteredResult getResult() {
        return result;
    }

}
