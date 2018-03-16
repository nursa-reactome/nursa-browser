package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

public abstract class AnalysisResultTable<T> extends CellTable<T> {

    protected static final NumberCell INTEGER_CELL = new NumberCell();
    protected static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    protected static final NumberCell SCIENTIFIC_CELL = new NumberCell(NumberFormat.getFormat("0.00E0"));

    protected ListHandler<T> sorter;

    public AnalysisResultTable(List<T> result) {
        // Hook in the data source.
        ListDataProvider<T> dataProvider = new ListDataProvider<T>();
        dataProvider.addDataDisplay(this);
        dataProvider.setList(result);
        // Apparently, there is no default GWT selection model.
        // If we don't set the selection model, we can't get the selection.
        SingleSelectionModel<Object> selectModel = new SingleSelectionModel<>();
        setSelectionModel(selectModel);
        // Enable sorting.
        sorter = new ListHandler<T>(dataProvider.getList());
        addColumnSortHandler(sorter);
        // The exact row count.
        setRowCount(result.size(), true);
    }
    
    public String getStId(int row) {
        T result = sorter.getList().get(row);
        return getStId(result);
    }
    
    abstract protected String getStId(T result);
}