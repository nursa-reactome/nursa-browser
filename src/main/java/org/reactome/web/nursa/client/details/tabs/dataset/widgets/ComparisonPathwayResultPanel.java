package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reactome.web.nursa.client.details.tabs.dataset.AnalysisResultFilterChangedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.AnalysisResultFilterChangedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.model.Comparison.Operand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;

abstract public class ComparisonPathwayResultPanel<R, K> extends VerticalPanel
implements AnalysisResultFilterChangedHandler {

    private ArrayList<AnalysisResultPanel<R, K>> unshared;
    private AnalysisResultTable<Entry<String, List<R>>, K> sharedTable;
    private ComparisonPartition<R> partition;
    private SimplePager sharedPager;
    private EventBus eventBus;
    private Widget unsharedPanel;
    private Widget sharedPanel;

    public ComparisonPathwayResultPanel(Comparison comparison, ComparisonPartition<R> partition,
            EventBus eventBus) {
        this.partition = partition;
        this.eventBus = eventBus;
        unsharedPanel = createUnsharedPanel(comparison, partition, eventBus);
        add(unsharedPanel);
        sharedPanel = createSharedPanel(partition, eventBus);
        add(sharedPanel);
        eventBus.addHandler(AnalysisResultFilterChangedEvent.TYPE, this);
    }

    @Override
    public void onFilterChanged(double filter) {
        repartition(filter);
    }

    abstract protected AnalysisResultPanel<R, K>
    createUnsharedResultsPanel(List<R> results, EventBus eventBus);

    abstract protected AnalysisResultTable<Map.Entry<String, List<R>>, K>
    createSharedResultsTable(Map<String, List<R>> results, EventBus eventBus);
    
    private void repartition(double filter) {
        partition.repartition(filter);
        for (int i = 0; i < partition.getUnshared().size(); i++) {
            AnalysisResultPanel<R, K> panel = unshared.get(i);
            List<R> results = partition.getUnshared().get(i);
            panel.setResults(results, eventBus);
        }
        remove(sharedPanel);
        sharedPanel = createSharedPanel(partition, eventBus);
        add(sharedPanel);
    }

    private Widget createUnsharedPanel(Comparison comparison,
            ComparisonPartition<R> partition, EventBus eventBus) {
        VerticalPanel body = new VerticalPanel();
        body.addStyleName("elv-Details-OverviewRow");
        HorizontalPanel tableHeading = new HorizontalPanel();
        HTMLPanel title = new HTMLPanel("Unshared Pathways");
        tableHeading.add(title);
        title.addStyleName(DataSetSection.RESOURCES.getCSS().subtitle());
        body.add(tableHeading);
        unshared = new ArrayList<AnalysisResultPanel<R, K>>();
        for (int i = 0; i < comparison.operands.length; i++) {
            body.add(createOverviewPanel(comparison.operands[i]));
            List<R> results = partition.getUnshared().get(i);
            AnalysisResultPanel<R, K> unsharedPanel =
                    createUnsharedResultsPanel(results, eventBus);
            unshared.add(unsharedPanel);
            body.add(unsharedPanel);
        }
        
        return body;
    }

    private Widget createSharedPanel(ComparisonPartition<R> partition, EventBus eventBus) {
        VerticalPanel body = new VerticalPanel();
        body.addStyleName("elv-Details-OverviewRow");
        HTMLPanel title = new HTMLPanel("Shared Pathways");
        title.addStyleName(DataSetSection.RESOURCES.getCSS().subtitle());
        body.add(title);
        sharedTable = createSharedResultsTable(partition.getShared(), eventBus);
        SelectionChangeEvent.Handler selectHandler = new SelectionChangeEvent.Handler() {
            
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                int pageRow = sharedTable.getKeyboardSelectedRow();
                K key = ComparisonPathwayResultPanel.this.getKey(pageRow);
                // Relay the select event up to the tab presenter.
                NursaPathwaySelectedEvent<K> pathwayEvent =
                        sharedTable.createSelectedEvent(key);
                eventBus.fireEventFromSource(pathwayEvent, this);
            }
        };
        sharedTable.getSelectionModel().addSelectionChangeHandler(selectHandler);

        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        sharedPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        sharedPager.setDisplay(sharedTable);
        sharedPager.setPageSize(sharedTable.getPageSize());
        
        body.add(sharedTable);
        body.add(sharedPager);
        
        return body;
    }

    private K getKey(int pageRow) {
        // The row in the backing pathway list.
        int absRow = sharedPager.getPageStart() + pageRow;
        // The stable id for the hovered row.
        return sharedTable.getKey(absRow);
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
