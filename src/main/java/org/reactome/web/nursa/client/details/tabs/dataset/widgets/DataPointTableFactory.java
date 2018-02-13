package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;

import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.DataSet;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class DataPointTableFactory {
    private static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    private static final NumberCell SCIENTIFIC_CELL =new NumberCell(NumberFormat.getFormat("0.00E0"));

    public static Widget getTable(DataSet dataset) {
        // The table content.
        List<DataPoint> dataPoints = dataset.getDataPoints();
        // The sortable table.
        CellTable<DataPoint> table = new CellTable<DataPoint>();
        ListDataProvider<DataPoint> dataProvider = new ListDataProvider<DataPoint>();
        dataProvider.addDataDisplay(table);
        dataProvider.setList(dataPoints);
        ListHandler<DataPoint> sorter = new ListHandler<DataPoint>(dataProvider.getList());
        table.addColumnSortHandler(sorter);
        // The exact row count.
        table.setRowCount(dataPoints.size(), true);

        // Define the columns.
        TextColumn<DataPoint> symbolColumn = new TextColumn<DataPoint>() {
            @Override
            public String getValue(DataPoint dataPoint) {
              return dataPoint.getSymbol();
            }
        };
        symbolColumn.setSortable(true);
        sorter.setComparator(symbolColumn, new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint p1, DataPoint p2) {
                return p1.getSymbol().compareTo(p2.getSymbol());
            }
        });
        
        Column<DataPoint, Number> pValueColumn = new Column<DataPoint, Number>(SCIENTIFIC_CELL) {
            @Override
            public Number getValue(DataPoint dataPoint) {
                return dataPoint.getPvalue();
            }
        };
        pValueColumn.setSortable(true);
        sorter.setComparator(pValueColumn, new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint p1, DataPoint p2) {
                return Double.compare(p1.getPvalue(), p2.getPvalue());
            }
        });
        
        Column<DataPoint, Number> foldChangeColumn = new Column<DataPoint, Number>(DECIMAL_CELL) {
            @Override
            public Number getValue(DataPoint dataPoint) {
                return dataPoint.getFoldChange();
            }
        };
        foldChangeColumn.setSortable(true);
        sorter.setComparator(foldChangeColumn, new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint p1, DataPoint p2) {
                return Double.compare(p1.getFoldChange(), p2.getFoldChange());
            }
        });
        
        // Add the columns.
        table.addColumn(symbolColumn, "Symbol");
        table.addColumn(pValueColumn, "P-Value");
        table.addColumn(foldChangeColumn, "Fold Change");
        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);
        pager.setPageSize(20);
        // Assemble the widget.
        VerticalPanel vp = new VerticalPanel();
        vp.add(table);
        vp.add(pager);
        // TODO - Center the pager in the panel with a UIBind definition.
        
        return vp;
    }
}
