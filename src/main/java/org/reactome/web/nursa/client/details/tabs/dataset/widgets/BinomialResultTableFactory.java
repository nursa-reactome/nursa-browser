package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class BinomialResultTableFactory {
    private static final NumberCell DECIMAL_CELL = new NumberCell(NumberFormat.getDecimalFormat());
    private static final NumberCell SCIENTIFIC_CELL =new NumberCell(NumberFormat.getFormat("0.00E0"));

    public static Widget getTable(AnalysisResult result) {
        List<PathwaySummary> pathways = result.getPathways();
        // The sortable table.
        CellTable<PathwaySummary> table = new CellTable<PathwaySummary>();
        ListDataProvider<PathwaySummary> dataProvider = new ListDataProvider<PathwaySummary>();
        dataProvider.addDataDisplay(table);
        dataProvider.setList(pathways);
        ListHandler<PathwaySummary> sorter = new ListHandler<PathwaySummary>(dataProvider.getList());
        table.addColumnSortHandler(sorter);
        // The exact row count.
        table.setRowCount(pathways.size(), true);

        // The hidden stId column.
        TextColumn<PathwaySummary> stIdColumn = new TextColumn<PathwaySummary>() {
            @Override
            public String getValue(PathwaySummary result) {
              return result.getStId();
            }
        };
        table.addColumn(stIdColumn, "Id");
        table.setColumnWidth(stIdColumn, "0px");

        // Define the columns.
        TextColumn<PathwaySummary> nameColumn = new TextColumn<PathwaySummary>() {
            @Override
            public String getValue(PathwaySummary result) {
              return result.getName();
            }
        };
        nameColumn.setSortable(true);
        sorter.setComparator(nameColumn, new Comparator<PathwaySummary>() {
            @Override
            public int compare(PathwaySummary r1, PathwaySummary r2) {
                return r1.getName().compareTo(r2.getName());
            }
        });
        
        Column<PathwaySummary, Number> pValueColumn = new Column<PathwaySummary, Number>(SCIENTIFIC_CELL) {
            @Override
            public Number getValue(PathwaySummary result) {
                return result.getEntities().getpValue();
            }
        };
        pValueColumn.setSortable(true);
        sorter.setComparator(pValueColumn, new Comparator<PathwaySummary>() {
            @Override
            public int compare(PathwaySummary r1, PathwaySummary r2) {
                return Double.compare(r1.getEntities().getpValue(), r2.getEntities().getpValue());
            }
        });
        
        Column<PathwaySummary, Number> fdrColumn = new Column<PathwaySummary, Number>(DECIMAL_CELL) {
            @Override
            public Number getValue(PathwaySummary result) {
                return result.getEntities().getFdr();
            }
        };
        fdrColumn.setSortable(true);
        sorter.setComparator(fdrColumn, new Comparator<PathwaySummary>() {
            @Override
            public int compare(PathwaySummary r1, PathwaySummary r2) {
                return Double.compare(r1.getEntities().getFdr(), r2.getEntities().getFdr());
            }
        });
        
        // Add the columns.
        table.addColumn(nameColumn, "Name");
        table.addColumn(pValueColumn, "P-Value");
        table.addColumn(fdrColumn, "FDR");
        
        // Enable selection.
        RowHoverEvent.Handler hoverHandler = new RowHoverEvent.Handler() {

            @Override
            public void onRowHover(RowHoverEvent event) {
                event.getHoveringRow();
            }
            
        };

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
