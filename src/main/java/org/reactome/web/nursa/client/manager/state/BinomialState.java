package org.reactome.web.nursa.client.manager.state;

import org.reactome.nursa.fireworks.client.BinomialSpeciesFilteredResult;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.analysis.client.model.BinomialResult;
import org.reactome.web.pwp.client.manager.state.State;

public class BinomialState extends State {
    
    private SpeciesFilteredResult result;

    public BinomialState(State currentState, BinomialResult analysisResult) {
        super(currentState);
        result = new BinomialSpeciesFilteredResult(analysisResult);
    }

    public SpeciesFilteredResult getResult() {
        return result;
    }

}
