package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

public interface AnalysisResultFilterChangedHandler extends EventHandler {

    public void onFilterChanged(double filter);
    
}
