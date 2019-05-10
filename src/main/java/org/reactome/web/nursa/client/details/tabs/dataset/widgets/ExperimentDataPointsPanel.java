package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;
import org.reactome.nursa.model.DisplayableDataPoint;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

/**
 * ExperimentDataPointsPanel lays out the experiment data points.
 *
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ExperimentDataPointsPanel extends DataPanel<DisplayableDataPoint> {

    private List<DisplayableDataPoint> dataPoints;

    public ExperimentDataPointsPanel(List<DisplayableDataPoint>dataPoints) {
        this.dataPoints = dataPoints;
    }

    @Override
    protected DataGrid<DisplayableDataPoint> getDataTable() {
        // The sortable table.
        DataGrid<DisplayableDataPoint> table = new DataGrid<DisplayableDataPoint>(PAGE_SIZE);
        ListDataProvider<DisplayableDataPoint> dataProvider =
                new ListDataProvider<DisplayableDataPoint>();
        dataProvider.addDataDisplay(table);
        dataProvider.setList(dataPoints);
        ListHandler<DisplayableDataPoint> sorter =
                new ListHandler<DisplayableDataPoint>(dataProvider.getList());
        table.addColumnSortHandler(sorter);
        // The exact row count.
        table.setRowCount(dataPoints.size(), true);
        
        // Define the columns.
        TextColumn<DisplayableDataPoint> symbolColumn = new TextColumn<DisplayableDataPoint>() {
            @Override
            public String getValue(DisplayableDataPoint dataPoint) {
              return dataPoint.getSymbol();
            }
        };
        symbolColumn.setSortable(true);
        sorter.setComparator(symbolColumn, new Comparator<DisplayableDataPoint>() {
            @Override
            public int compare(DisplayableDataPoint p1, DisplayableDataPoint p2) {
                return p1.getSymbol().compareTo(p2.getSymbol());
            }
        });
        table.addColumn(symbolColumn, "Symbol");

        Column<DisplayableDataPoint, Number> pValueColumn = new Column<DisplayableDataPoint, Number>(CellTypes.SCIENTIFIC_CELL) {
            @Override
            public Number getValue(DisplayableDataPoint dataPoint) {
                return dataPoint.getPvalue();
            }
        };
        pValueColumn.setSortable(true);
        sorter.setComparator(pValueColumn, new Comparator<DisplayableDataPoint>() {
            @Override
            public int compare(DisplayableDataPoint p1, DisplayableDataPoint p2) {
                return Double.compare(p1.getPvalue(), p2.getPvalue());
            }
        });
        table.addColumn(pValueColumn, "p-value");

        Column<DisplayableDataPoint, Number> foldChangeColumn = new Column<DisplayableDataPoint, Number>(CellTypes.SCIENTIFIC_CELL) {
            @Override
            public Number getValue(DisplayableDataPoint dataPoint) {
                return dataPoint.getFoldChange();
            }
        };
        foldChangeColumn.setSortable(true);
        sorter.setComparator(foldChangeColumn, new Comparator<DisplayableDataPoint>() {
            @Override
            public int compare(DisplayableDataPoint p1, DisplayableDataPoint p2) {
                return Double.compare(p1.getFoldChange(), p2.getFoldChange());
            }
        });
        table.addColumn(foldChangeColumn, "Fold Change");

        TextColumn<DisplayableDataPoint> isReactomeColumn = new TextColumn<DisplayableDataPoint>() {
            @Override
            public String getValue(DisplayableDataPoint dataPoint) {
                return Boolean.toString(dataPoint.isReactome());
            }
        };
        isReactomeColumn.setSortable(true);
        sorter.setComparator(isReactomeColumn, new Comparator<DisplayableDataPoint>() {
            @Override
            public int compare(DisplayableDataPoint p1, DisplayableDataPoint p2) {
                return Boolean.compare(p1.isReactome(), p2.isReactome());
            }
        });
        table.addColumn(isReactomeColumn, "Reactome Gene");
        
        formatTableDimensions(table);
        
        return table;
    }

}
