package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.stream.Collectors;

import org.reactome.web.nursa.client.details.tabs.dataset.AnalysisResultFilterChangedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.AnalysisResultFilterChangedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class AnalysisResultPanel<R, K> extends VerticalPanel
implements AnalysisResultFilterChangedHandler {

    private static final String EMPTY_RESULT_MSG =
            "No pathways match the analysis criteria";

    private AnalysisResultTable<R, K> table;
    private SimplePager pager;
    private List<R> unfilteredResults;
    private EventBus eventBus;

    protected AnalysisResultPanel(List<R> results, EventBus eventBus) {
        this.eventBus = eventBus;
        initDisplay(results, eventBus);
    }

    protected AnalysisResultPanel(List<R> results, double filter, EventBus eventBus) {
        this.eventBus = eventBus;
        unfilteredResults = results;
        filterResults(filter);
        eventBus.addHandler(AnalysisResultFilterChangedEvent.TYPE, this);
    }

    public void setResults(List<R> results, EventBus eventBus) {
        remove(table);
        remove(pager);
        unfilteredResults = results;
        initDisplay(results, eventBus);
    }

    abstract protected AnalysisResultTable<R, K> createResultsTable(List<R> results);

    private void initDisplay(List<R> results, EventBus eventBus) {
        if (results.size() == 0) {
            Label label = new Label(EMPTY_RESULT_MSG);
            add(label);
            return;
        }
        table = createResultsTable(results);
        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);
        pager.setPageSize(table.getPageSize());

        // Assemble the widget.
        add(table);
        add(pager);
         
        // Add the hover handler.
        RowHoverEvent.Handler hoverHandler = new RowHoverEvent.Handler() {

            @Override
            public void onRowHover(RowHoverEvent event) {
                // TODO - should this emulate base Reactome
                // AnalysisTabPresenter.onPathwayHovered?
                // That would be a challenge to get right.
//                // The row in the current page, subtracting the header row.
//                int pageRow = event.getHoveringRow().getRowIndex() - 1;
//                K key = AnalysisResultPanel.this.getKey(pageRow);
//                // Relay the hover event up to the tab presenter.
//                NursaPathwayHoveredEvent<K> pathwayEvent =
//                        AnalysisResultPanel.this.table.createHoveredEvent(key);
//                eventBus.fireEventFromSource(pathwayEvent, this);
            }
        };
        table.addRowHoverHandler(hoverHandler);
        
        MouseOutHandler mouseOutHandler = new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                // The main Reactome reset event is adequate for our purpose.
                PathwayHoveredResetEvent resetEvent = new PathwayHoveredResetEvent();
                // Relay the reset event to the tab presenter.
                eventBus.fireEventFromSource(resetEvent, this);
            }
        };
        table.addHandler(mouseOutHandler, MouseOutEvent.getType());
        
        SelectionChangeEvent.Handler selectHandler = new SelectionChangeEvent.Handler() {
            
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                int pageRow = table.getKeyboardSelectedRow();
                K key = AnalysisResultPanel.this.getKey(pageRow);
                // Relay the select event up to the tab presenter.
                NursaPathwaySelectedEvent<K> pathwayEvent =
                        AnalysisResultPanel.this.table.createSelectedEvent(key);
                eventBus.fireEventFromSource(pathwayEvent, this);
            }
        };
        table.getSelectionModel().addSelectionChangeHandler(selectHandler);
    }

    @Override
    public void onFilterChanged(double filter) {
        remove(table);
        remove(pager);
        filterResults(filter);
    }
    
    abstract protected double getFdr(R result);

    private void filterResults(double filter) {
        List<R> filtered = unfilteredResults.stream()
                .filter(result -> getFdr(result) <= filter)
                .collect(Collectors.toList());
        initDisplay(filtered, eventBus);
        if (pager != null) {
            pager.setPageSize(table.getPageSize());
        }
    }
    
    private K getKey(int pageRow) {
        // The row in the backing pathway list.
        int absRow = pager.getPageStart() + pageRow;
        // The stable id for the hovered row.
        return table.getKey(absRow);
    }

}