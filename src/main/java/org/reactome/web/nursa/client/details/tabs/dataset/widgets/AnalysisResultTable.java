package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Arrays;
import java.util.List;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class AnalysisResultTable<R, K> extends DataGrid<R> {

    private static final int PAGE_SIZE = 20;

    protected static final NumberCell INTEGER_CELL = new NumberCell();
    protected static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());

    protected ListHandler<R> sorter;
    
    private ListDataProvider<R> dataProvider;

    protected AnalysisResultTable(List<R> results) {
        // Hook in the data source.
        dataProvider = new ListDataProvider<R>(results);
        dataProvider.addDataDisplay(this);
        // Apparently, there is no default GWT selection model.
        // If we don't set the selection model, we can't get the selection.
        SingleSelectionModel<Object> selectModel = new SingleSelectionModel<>();
        setSelectionModel(selectModel);
        // Enable sorting.
        sorter = new ListHandler<R>(dataProvider.getList());
        addColumnSortHandler(sorter);
        // The exact row count.
        setRowCount(results.size(), true);
        // The page size of small tables is the row count.
        setPageSize(Math.min(PAGE_SIZE, getRowCount()));
        
        // Allow 1.2em per row for padding.
        int height = new Double(Math.ceil(1.2 * getPageSize())).intValue();
        setHeight(Integer.toString(height) + "em");
    }
    
    public K getKey(int row) {
        R result = sorter.getList().get(row);
        return getKey(result);
    }
    
    protected void formatTableDimensions() {
        // The first name column needs room for app. 32 small characters.
        int[] colWidths = new int[getColumnCount()];
        colWidths[0] = (int)(0.8 * 32);
        for (int i = 1; i < colWidths.length; i++) {
            Object header = getHeader(i).getValue();
            colWidths[i] = DataPanel.columnWidth(header);
        }
        for (int i = 0; i < colWidths.length; i++) {
            String colUnitsWidth = Integer.toString(colWidths[i]) + "em";
            setColumnWidth(i, colUnitsWidth);
        }
        // Unfortunately, we must also set the table width separately.
        int tableWidth = Arrays.stream(colWidths).sum();
        setWidth(Integer.toString(tableWidth) + "em");
    }
    
    // Hover events are place-holders; no action yet taken.
    // TDOO - what to do on hover?
    
    abstract protected NursaPathwayHoveredEvent<K> createHoveredEvent(K key);
    
    abstract protected NursaPathwaySelectedEvent<K> createSelectedEvent(K key);
    
    abstract protected K getKey(R result);
}