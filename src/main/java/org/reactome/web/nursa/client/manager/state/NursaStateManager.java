package org.reactome.web.nursa.client.manager.state;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.pwp.client.manager.state.StateManager;

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
