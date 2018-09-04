package org.reactome.web.nursa.fireworks.controls.settings;

import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.fireworks.controls.settings.HideableContainerPanel;
import org.reactome.web.fireworks.events.AnalysisPerformedEvent;
import org.reactome.web.fireworks.handlers.AnalysisPerformedHandler;
import org.reactome.web.fireworks.handlers.AnalysisResetHandler;
import org.reactome.web.nursa.analysis.client.model.ComparisonExpressionSummary;

import com.google.gwt.event.shared.EventBus;

public class NursaHideableContainerPanel extends HideableContainerPanel
implements AnalysisPerformedHandler, AnalysisResetHandler{

    public NursaHideableContainerPanel(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void onAnalysisReset() {
        profilesBtn.setVisible(true);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        boolean visible =
                !AnalysisType.DATASET_COMPARISON.equals(e.getAnalysisType());
        profilesBtn.setVisible(visible);
    }

}
