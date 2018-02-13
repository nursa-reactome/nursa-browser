package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;

import org.reactome.gsea.model.AnalysisResult;
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

public class GseaResultTableFactory {
    private static final NumberCell INTEGER_CELL = new NumberCell();
    private static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    private static final NumberCell SCIENTIFIC_CELL =new NumberCell(NumberFormat.getFormat("0.00E0"));

    public static Widget createTable(List<AnalysisResult> result) {
        // The sortable table.
        CellTable<AnalysisResult> table = new CellTable<AnalysisResult>();
        ListDataProvider<AnalysisResult> dataProvider = new ListDataProvider<AnalysisResult>();
        dataProvider.addDataDisplay(table);
        dataProvider.setList(result);
        ListHandler<AnalysisResult> sorter = new ListHandler<AnalysisResult>(dataProvider.getList());
        table.addColumnSortHandler(sorter);
        // The exact row count.
        table.setRowCount(result.size(), true);

        // Define the columns.
        TextColumn<AnalysisResult> nameColumn = new TextColumn<AnalysisResult>() {
            @Override
            public String getValue(AnalysisResult result) {
              return result.getPathway().getName();
            }
        };
        nameColumn.setSortable(true);
        sorter.setComparator(nameColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return r1.getPathway().getName().compareTo(r2.getPathway().getName());
            }
        });
        
        Column<AnalysisResult, Number> hitsColumn = new Column<AnalysisResult, Number>(INTEGER_CELL) {
            @Override
            public Integer getValue(AnalysisResult result) {
                return result.getHitCount();
            }
        };
        hitsColumn.setSortable(true);
        sorter.setComparator(hitsColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return Integer.compare(r1.getHitCount(), r2.getHitCount());
            }
        });
        
        Column<AnalysisResult, Number> scoreColumn = new Column<AnalysisResult, Number>(DECIMAL_CELL) {
            @Override
            public Number getValue(AnalysisResult result) {
                return result.getScore();
            }
        };
        scoreColumn.setSortable(true);
        sorter.setComparator(scoreColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return Double.compare(r1.getScore(), r2.getScore());
            }
        });
        
        Column<AnalysisResult, Number> nesColumn = new Column<AnalysisResult, Number>(DECIMAL_CELL) {
            @Override
            public Number getValue(AnalysisResult result) {
                return result.getNormalizedScore();
            }
        };
        scoreColumn.setSortable(true);
        sorter.setComparator(nesColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return Double.compare(r1.getNormalizedScore(), r2.getNormalizedScore());
            }
        });
        
        Column<AnalysisResult, Number> pValueColumn = new Column<AnalysisResult, Number>(SCIENTIFIC_CELL) {
            @Override
            public Number getValue(AnalysisResult result) {
                return result.getPvalue();
            }
        };
        pValueColumn.setSortable(true);
        sorter.setComparator(pValueColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return Double.compare(r1.getPvalue(), r2.getPvalue());
            }
        });
        
        Column<AnalysisResult, Number> fdrColumn = new Column<AnalysisResult, Number>(DECIMAL_CELL) {
            @Override
            public Number getValue(AnalysisResult result) {
                return result.getFdr();
            }
        };
        fdrColumn.setSortable(true);
        sorter.setComparator(fdrColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return Double.compare(r1.getFdr(), r2.getFdr());
            }
        });

        Column<AnalysisResult, String> regTypeColumn = new TextColumn<AnalysisResult>() {
            @Override
            public String getValue(AnalysisResult result) {
                return result.getRegulationType().toString();
            }
        };
        regTypeColumn.setSortable(true);
        sorter.setComparator(regTypeColumn, new Comparator<AnalysisResult>() {
            @Override
            public int compare(AnalysisResult r1, AnalysisResult r2) {
                return r1.getRegulationType().compareTo(r2.getRegulationType());
            }
        });
        
        // Add the columns.
        table.addColumn(nameColumn, "Name");
        table.addColumn(hitsColumn, "Hits");
        table.addColumn(scoreColumn, "Score");
        table.addColumn(nesColumn, "NES");
        table.addColumn(pValueColumn, "P-Value");
        table.addColumn(fdrColumn, "FDR");
        table.addColumn(regTypeColumn, "Regulation Type");

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
