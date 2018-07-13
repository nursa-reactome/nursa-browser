package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.model.Comparison.Operand;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

abstract public class ComparisonPathwayResultPanel<R, K> extends VerticalPanel {

    private EventBus eventBus;

    public ComparisonPathwayResultPanel(
            Comparison comparison,
            ComparisonPartition<R> partition,
            EventBus eventBus
            ) {
        this.eventBus = eventBus;
        add(createUnsharedPanel(comparison, partition));
        add(createSharedPanel(partition));
    }

    abstract protected AnalysisResultTable<R, K>
    createUnsharedResultsTable(List<R> results);

    abstract protected AnalysisResultTable<Map.Entry<String, List<R>>, K>
    createSharedResultsTable(Map<String, List<R>> map);

    private Widget createUnsharedPanel(
            Comparison comparison,
            ComparisonPartition<R> partition) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewRow");
        HTMLPanel title = new HTMLPanel("Unshared");
        title.addStyleName(DataSetSection.RESOURCES.getCSS().subtitle());
        vp.add(title);
        for (int i=0; i < comparison.operands.length; i++) {
            vp.add(createOverviewPanel(comparison.operands[i]));
            List<R> results = partition.getUnshared().get(i);
            AnalysisResultTable<R, K> table = createUnsharedResultsTable(results);
            AnalysisResultPanel<R, K> panel =
                    new AnalysisResultPanel<R, K>(table, eventBus);
            vp.add(panel);
        }
        return vp;
    }

    private Widget createSharedPanel(ComparisonPartition<R> partition) {
        VerticalPanel vp = new VerticalPanel();
        vp.addStyleName("elv-Details-OverviewRow");
        HTMLPanel title = new HTMLPanel("Shared");
        title.addStyleName(DataSetSection.RESOURCES.getCSS().subtitle());
        vp.add(title);
        AnalysisResultTable<Entry<String, List<R>>, K> table =
                createSharedResultsTable(partition.getShared());
        AnalysisResultPanel<Entry<String, List<R>>, K> panel =
                new AnalysisResultPanel<Entry<String, List<R>>, K>(table, eventBus);
        vp.add(panel);
        return vp;
    }

    private Widget createOverviewPanel(Operand operand) {
        HorizontalPanel overview = new HorizontalPanel();
        Widget doi = new HTMLPanel("DOI: " + operand.dataset.getDoi());
        doi.setStyleName(DataSetPanel.RESOURCES.getCSS().doi());
        overview.add(doi);
        Widget exp = new HTMLPanel("Experiment: " + operand.experiment.getName());
        exp.setStyleName(DataSetPanel.RESOURCES.getCSS().experiment());
        overview.add(exp);
        return overview;
    }

}
