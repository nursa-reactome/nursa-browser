package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class AnalysisResultPanel<T, K> extends VerticalPanel {

    private SimplePager pager;
    private AnalysisResultTable<T, K> table;

    public AnalysisResultPanel(AnalysisResultTable<T, K> table, EventBus eventBus) {
        this.table = table;
        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);
        pager.setPageSize(20);

        // Assemble the widget.
        this.add(table);
        this.add(pager);
        
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
    
    private K getKey(int pageRow) {
        // The row in the backing pathway list.
        int absRow = pager.getPageStart() + pageRow;
        // The stable id for the hovered row.
        return table.getKey(absRow);
    }

}