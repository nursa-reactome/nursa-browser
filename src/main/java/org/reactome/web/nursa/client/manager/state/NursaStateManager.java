package org.reactome.web.nursa.client.manager.state;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.pwp.client.common.events.*;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.client.manager.state.StateManager;
import java.util.List;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class NursaStateManager extends StateManager {

    public NursaStateManager(EventBus eventBus) {
        super(eventBus);
    }
    
    @Override
    protected DetailsTabType analysisTabType() {
        return DetailsTabType.DATASET;
    }
}
