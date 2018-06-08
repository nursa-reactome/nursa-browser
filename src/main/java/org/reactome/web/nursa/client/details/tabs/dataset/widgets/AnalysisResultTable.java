package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class AnalysisResultTable<R, K> extends CellTable<R> {

    protected static final NumberCell INTEGER_CELL = new NumberCell();
    protected static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    protected static final NumberCell SCIENTIFIC_CELL = new NumberCell(NumberFormat.getFormat("0.00E0"));

    protected ListHandler<R> sorter;

    protected AnalysisResultTable(List<R> results) {
        // Hook in the data source.
        ListDataProvider<R> dataProvider = new ListDataProvider<R>();
        dataProvider.addDataDisplay(this);
        dataProvider.setList(results);
        // Apparently, there is no default GWT selection model.
        // If we don't set the selection model, we can't get the selection.
        SingleSelectionModel<Object> selectModel = new SingleSelectionModel<>();
        setSelectionModel(selectModel);
        // Enable sorting.
        sorter = new ListHandler<R>(dataProvider.getList());
        addColumnSortHandler(sorter);
        // The exact row count.
        setRowCount(results.size(), true);
    }
    
    public K getKey(int row) {
        R result = sorter.getList().get(row);
        return getKey(result);
    }
    
    abstract protected NursaPathwayHoveredEvent<K> createHoveredEvent(K key);
    
    abstract protected NursaPathwaySelectedEvent<K> createSelectedEvent(K key);
    
    abstract protected K getKey(R result);
}