package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.reactome.nursa.model.DataPoint;
import org.reactome.web.nursa.client.details.tabs.dataset.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonDataPoint;
import org.reactome.web.nursa.client.details.tabs.dataset.NullSafeCurry;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.ListDataProvider;

public class ComparisonDataPointsPanel extends DataPanel<ComparisonDataPoint> {

    private Comparison comparison;

    public ComparisonDataPointsPanel(Comparison comparison) {
        this.comparison = comparison;
    }

    @Override
    protected CellTable<ComparisonDataPoint> getDataTable() {
        // The {gene symbol: fdr pair} map.
        Map<String, DataPoint[]> fdrs = new HashMap<String, DataPoint[]>();
        for (int i=0; i < comparison.operands.length; i++) {
            for (DataPoint dp: comparison.operands[i].experiment.getDataPoints()) {
                DataPoint[] values = fdrs.get(dp.getSymbol());
                if (values == null) {
                    values = new DataPoint[2];
                    fdrs.put(dp.getSymbol(), values);
                }
                values[i] = dp;
            }
        }
        
        // The {gene symbol, fdr pair} records.
        ComparisonDataPoint[] rows =
                fdrs.entrySet()
                    .stream()
                    .map(entry -> new ComparisonDataPoint(entry.getKey(), entry.getValue()))
                    .toArray(ComparisonDataPoint[]::new);
        
        // The sortable table.
        CellTable<ComparisonDataPoint> table = new CellTable<ComparisonDataPoint>();
        ListDataProvider<ComparisonDataPoint> dataProvider =
                new ListDataProvider<ComparisonDataPoint>();
        dataProvider.addDataDisplay(table);
        dataProvider.setList(Arrays.asList(rows));
        ListHandler<ComparisonDataPoint> sorter =
                new ListHandler<ComparisonDataPoint>(dataProvider.getList());
        table.addColumnSortHandler(sorter);
        // The exact row count.
        table.setRowCount(rows.length, true);

        // The gene symbol column.
        TextColumn<ComparisonDataPoint> symbolColumn = new TextColumn<ComparisonDataPoint>() {
            @Override
            public String getValue(ComparisonDataPoint dataPoint) {
              return dataPoint.getSymbol();
            }
        };
        symbolColumn.setSortable(true);
        sorter.setComparator(symbolColumn, new Comparator<ComparisonDataPoint>() {
            @Override
            public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                return p1.getSymbol().compareTo(p2.getSymbol());
            }
        });
        table.addColumn(symbolColumn, "Symbol");
        
        // The FDR (p-value) and fold change columns.
        for (int i=0; i < 2; i++) {
            String qualifier = i == 0 ? "First" : "Second";
            Function<ComparisonDataPoint, Double> fdr =
                    curry(i, DataPoint::getPvalue);
            Column<ComparisonDataPoint, Number> fdrCol =
                    createDataColumn(fdr, sorter, SCIENTIFIC_CELL);
            table.addColumn(fdrCol, qualifier + " FDR");
            Function<ComparisonDataPoint, Double> foldChange =
                    curry(i, DataPoint::getFoldChange);
            Column<ComparisonDataPoint, Number> foldChangeCol =
                    createDataColumn(foldChange, sorter, DECIMAL_CELL);
            table.addColumn(foldChangeCol, qualifier + " Fold Change");
        }

        // The log10(FDR1/FDR2) ratio column. 
        Column<ComparisonDataPoint, Number> ratioCol =
                createDataColumn(ComparisonDataPoint::getRatio, sorter, DECIMAL_CELL);
        table.addColumn(ratioCol, "FDR Ratio");

        return table;
    }

    private static Function<ComparisonDataPoint, Double> curry(
            final int index, Function<DataPoint, Double> accessor) {
        
        return new NullSafeCurry<ComparisonDataPoint, DataPoint, Double>(
                cdp -> cdp.getDataPoints()[index],
                accessor);
    }

}
