package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.nursa.model.ComparisonDataPoint;
import org.reactome.web.nursa.client.details.tabs.dataset.NullSafeCurry;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaWidgetHelper;
import org.reactome.web.nursa.model.Comparison;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;

public class ComparisonDataPointsPanel extends DataPanel<ComparisonDataPoint> {

    private Comparison comparison;

    public ComparisonDataPointsPanel(Comparison comparison) {
        this.comparison = comparison;
    }

    @Override
    protected DataGrid<ComparisonDataPoint> getDataTable() {
        // The {gene symbol: fdr pair} map.
        Map<String, DisplayableDataPoint[]> fdrs = new HashMap<String, DisplayableDataPoint[]>();
        for (int i=0; i < comparison.operands.length; i++) {
            for (DisplayableDataPoint dp: comparison.operands[i].dataPoints) {
                DisplayableDataPoint[] values = fdrs.get(dp.getSymbol());
                if (values == null) {
                    values = new DisplayableDataPoint[2];
                    fdrs.put(dp.getSymbol(), values);
                }
                values[i] = dp;
            }
        }
        
        // The {gene symbol, fdr pair} records.
        ComparisonDataPoint[] rows = fdrs.entrySet().stream()
                .map(entry -> new ComparisonDataPoint(entry.getKey(), entry.getValue()))
                .toArray(ComparisonDataPoint[]::new);
        
        // The sortable table.
        DataGrid<ComparisonDataPoint> table = new DataGrid<ComparisonDataPoint>(PAGE_SIZE);
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

        // The reactome flag column.
        TextColumn<ComparisonDataPoint> isReactomeColumn = new TextColumn<ComparisonDataPoint>() {
            @Override
            public String getValue(ComparisonDataPoint dataPoint) {
              return Boolean.toString(dataPoint.isReactome());
            }
        };
        isReactomeColumn.setSortable(true);
        sorter.setComparator(isReactomeColumn, new Comparator<ComparisonDataPoint>() {
            @Override
            public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                return Boolean.compare(p1.isReactome(), p2.isReactome());
            }
        });
        table.addColumn(isReactomeColumn, "Reactome Gene");
        
        // The p-value and fold change columns.
        for (int i=0; i < 2; i++) {
            Function<ComparisonDataPoint, Double> pValue =
                    curry(i, DataPoint::getPvalue);
            Column<ComparisonDataPoint, Number> pValueCol =
                    createDataColumn(pValue, sorter, CellTypes.SCIENTIFIC_CELL);
            SafeHtml pValueHdr = NursaWidgetHelper.superscriptHeader(i, "p-value");
            pValueCol.setSortable(true);
            sorter.setComparator(pValueCol, new Comparator<ComparisonDataPoint>() {
                @Override
                public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                    Double pvalue1 = pValue.apply(p1);
                    Double pvalue2 = pValue.apply(p2);
                    return nullSafeCompare(pvalue1, pvalue2);
                }
            });
            table.addColumn(pValueCol, pValueHdr);
 
            Function<ComparisonDataPoint, Double> foldChange =
                    curry(i, DataPoint::getFoldChange);
            Column<ComparisonDataPoint, Number> fcCol =
                    createDataColumn(foldChange, sorter, CellTypes.SCIENTIFIC_CELL);
            SafeHtml fcHdr = NursaWidgetHelper.superscriptHeader(i, "Fold Change");
            fcCol.setSortable(true);
            sorter.setComparator(fcCol, new Comparator<ComparisonDataPoint>() {
                @Override
                public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                    return nullSafeCompare(foldChange.apply(p1), foldChange.apply(p2));
                }
            });
           table.addColumn(fcCol, fcHdr);
        }

        // The log10(pvalue2/pvalue1) ratio column. 
        Column<ComparisonDataPoint, Number> ratioCol =
                createDataColumn(ComparisonDataPoint::getPValueRatio, sorter, CellTypes.DECIMAL_CELL);
        ratioCol.setSortable(true);
        sorter.setComparator(ratioCol, new Comparator<ComparisonDataPoint>() {
            @Override
            public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                return nullSafeCompare(p1.getPValueRatio(), p2.getPValueRatio());
            }
        });
        table.addColumn(ratioCol, "Log p-value Ratio");

        // The fold change difference column. 
        Column<ComparisonDataPoint, Number> fcDiffCol =
                createDataColumn(ComparisonDataPoint::getFCDifference, sorter, CellTypes.DECIMAL_CELL);
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        shb.appendHtmlConstant("&Delta;");
        shb.appendEscaped( " Fold Change");
        SafeHtml fcHdr = shb.toSafeHtml();
        sorter.setComparator(fcDiffCol, new Comparator<ComparisonDataPoint>() {
            @Override
            public int compare(ComparisonDataPoint p1, ComparisonDataPoint p2) {
                return nullSafeCompare(p1.getFCDifference(), p2.getFCDifference());
            }
        });
        table.addColumn(fcDiffCol, fcHdr);

        formatTableDimensions(table);
 
        return table;
    }

    /**
     * The usual object comparison idiom.
     * 
     * @param value1
     * @param value2
     * @return
     */
    private static int nullSafeCompare(Double value1, Double value2) {
        return value1 == null ?
                (value2 == null ? 0 : -1) :
                (value2 == null ? 1 : value1.compareTo(value2));
    }

    /**
     * Curries the index accessor composed with the given data point accessor
     * into a single function.
     * 
     * @param index the index to dereference
     * @param accessor the data point accessor function
     * @return the composed function
     */
    private static Function<ComparisonDataPoint, Double> curry(
            final int index, Function<DataPoint, Double> accessor) {
        // First dereference the data point, then apply the accessor.
        return new NullSafeCurry<ComparisonDataPoint, DataPoint, Double>(
                cdp -> cdp.getDataPoints()[index],
                accessor);
    }

}
