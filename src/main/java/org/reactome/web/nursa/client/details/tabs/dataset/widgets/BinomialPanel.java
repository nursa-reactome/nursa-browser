package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.event.shared.EventBus;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialPanel extends AnalysisResultPanel<PathwaySummary, Long> {

    public BinomialPanel(BinomialTable table, final EventBus eventBus) {
        super(table, eventBus);
     }
}