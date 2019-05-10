package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import org.reactome.web.nursa.client.details.tabs.dataset.NullSafeComparator;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DataPanel is the base class for laying out a data row display.
 *
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class DataPanel<T> implements IsWidget {

    /**
     * The minimum column width in em units, bearing in mind that data
     * is displayed in small text.
     */
    static final int MIN_COL_LEN = 8;

    protected static final int PAGE_SIZE = 20;
    
    public Widget asWidget() {
        // The table content.
        DataGrid<T> table = getDataTable();
        VerticalPanel vp = new VerticalPanel();
        vp.add(table);
        
        // Paginate the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);
        pager.setPageSize(Math.min(PAGE_SIZE, getDataTable().getRowCount()));
        vp.add(pager);
        
        return vp;
    }
    
    abstract protected DataGrid<T> getDataTable();

    protected Column<T, Number> createDataColumn(
            Function<T, Double> accessor, ListHandler<T> sorter, NumberCell cell) {
        // The column with a value function.
        Column<T, Number> column = new Column<T, Number>(cell) {
 
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

    protected void formatTableDimensions(DataGrid<T> table) {
        // The table columns need a minimum of 8 small characters each.
        int[] colWidths = new int[table.getColumnCount()];
        for (int i = 0; i < colWidths.length; i++) {
            Object header = table.getHeader(i).getValue();
            int colWidth = columnWidth(header);
            colWidths[i] = colWidth;
        }
        for (int i = 0; i < colWidths.length; i++) {
            String colUnitsWidth = Integer.toString(colWidths[i]) + "em";
            table.setColumnWidth(i, colUnitsWidth);
        }
        // Unfortunately, we must also set the table width separately.
        int tableWidth = Arrays.stream(colWidths).sum();
        table.setWidth(Integer.toString(tableWidth) + "em");

        // The page size of small tables is the row count.
        int pageSize = Math.min(PAGE_SIZE, table.getRowCount());
        table.setPageSize(pageSize);
        
        // Unfortunately, DataGrid requires an explicit height
        // (cf. https://stackoverflow.com/questions/26137734/gwt-datagrid-not-shown).
        // Allow 1.5em per row for padding. The height doesn't need
        // to be exact, since the list has a side scroll bar.
        int height = new Double(Math.ceil(1.5 * pageSize)).intValue();
        table.setHeight(Integer.toString(height) + "em");
    }

    static int columnWidth(Object header) {
        // Matcher for HTML element delimiters, e.g. <sup> and </sup>.
        RegExp eltDelimRegExp = RegExp.compile("</?\\w+>");
        // Matcher for HTML entities, e.g. &amp;
        RegExp htmlEntityRegExp = RegExp.compile("&\\w+;");
        int hdrLen = 0;
        if (header instanceof String) {
            hdrLen = ((String) header).length();
        } else if (header instanceof SafeHtml) {
            String html = ((SafeHtml) header).asString();
            // A rough heuristic to determine the displayed header
            // text length is to sum the inner text of the HTML
            // elements.
            SplitResult matches = eltDelimRegExp.split(html);
            for (int i = 0; i < matches.length(); i++) {
                String match = matches.get(i);
                // A HTML entity counts as one character.
                SplitResult parts = htmlEntityRegExp.split(match);
                for (int j = 0; j < parts.length(); j++) {
                    String part = parts.get(j);
                    hdrLen += part.length();
                }
                hdrLen += parts.length() - 1;
            }
        }
        int colLen = Math.max(MIN_COL_LEN, hdrLen);
        // The cells are small font, so scale by 0.9.
        int colWidth = (int) Math.ceil(0.8 * colLen);
        
        return colWidth;
    }

}