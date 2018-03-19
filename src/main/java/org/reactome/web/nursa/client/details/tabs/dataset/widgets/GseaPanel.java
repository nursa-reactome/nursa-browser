package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.gsea.model.GseaAnalysisResult;
import com.google.gwt.event.shared.EventBus;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaPanel extends AnalysisResultPanel<GseaAnalysisResult, String> {

    public GseaPanel(GseaTable table, final EventBus eventBus) {
        super(table, eventBus);
     }
}