package org.reactome.web.nursa.client.manager.state;

import com.google.gwt.event.shared.EventBus;

import java.util.List;

import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.nursa.analysis.client.model.BinomialResult;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.manager.state.StateManager;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class NursaStateManager extends StateManager {

    public NursaStateManager(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onAnalysisCompleted(AnalysisCompletedEvent event) {
        // Code below is adapted from the superclass for the DataSet tab
        // and the possibility of a GSEA BinomialState.
        AnalysisResult result = event.getAnalysisResult();
        List<ResourceSummary> resources = result.getResourceSummary();
        ResourceSummary resource = resources.get(0);
        State desiredState = null;
        if (result instanceof BinomialResult) {
            BinomialResult binomialResult = (BinomialResult) result;
            desiredState = new BinomialState(this.currentState, binomialResult);
        } else {
            desiredState = new State(this.currentState);
        }
        desiredState.setDetailsTab(DetailsTabType.DATASET);
        AnalysisSummary summary = result.getSummary();
        desiredState.setAnalysisParameters(summary.getToken(), resource.getResource());
        this.eventBus.fireEventFromSource(new StateChangedEvent(desiredState), this);
    }

}
