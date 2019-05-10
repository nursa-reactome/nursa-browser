package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

public interface NursaPathwaySelectedHandler extends EventHandler {

    void onPathwaySelected(Long dbId);

    void onPathwaySelected(String stId);

}
