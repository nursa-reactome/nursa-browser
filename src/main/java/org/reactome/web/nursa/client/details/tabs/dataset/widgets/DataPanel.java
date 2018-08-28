package org.reactome.web.nursa.client.details.tabs.dataset.widgets;


import java.util.Comparator;
import java.util.function.Function;

import org.reactome.web.nursa.client.details.tabs.dataset.NullSafeComparator;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class DataPanel<T> implements IsWidget {

    protected static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    protected static final NumberCell SCIENTIFIC_CELL = new NumberCell(NumberFormat.getFormat("0.00E0"));
    
    public Widget asWidget() {
        // The table content.
        CellTable<T> table = getDataTable();
        VerticalPanel vp = new VerticalPanel();
        vp.add(table);
        
        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);
        pager.setPageSize(20);
        vp.add(pager);
        
        return vp;
    }
    
    abstract protected CellTable<T> getDataTable();

    protected Column<T, Number> createDataColumn(
            Function<T, Double> accessor, ListHandler<T> sorter, NumberCell cell) {
        // The column with a value function.
        Column<T, Number> column =
                new Column<T, Number>(cell) {
            @Override
            public Number getValue(T comparisonDataPoint) {
                return  accessor.apply(comparisonDataPoint);
            }
        };
        column.setSortable(true);
        // The table sort comparator.
        Comparator<T> comparator = new NullSafeComparator<T, Double>() {

            @Override
            protected Double getValue(T t) {
                return accessor.apply(t);
            }
            
        };
        sorter.setComparator(column, comparator);
        return column;
    }

}