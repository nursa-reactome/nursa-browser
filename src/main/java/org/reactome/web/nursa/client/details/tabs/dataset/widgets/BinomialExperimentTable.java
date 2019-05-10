package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class BinomialExperimentTable extends AnalysisResultTable<PathwaySummary, Long> {

    public BinomialExperimentTable(List<PathwaySummary> results) {
        super(results);
        initDisplay();
    }

    @Override
    protected Long getKey(PathwaySummary result) {
        return result.getDbId();
    }

    @Override
    protected NursaPathwayHoveredEvent<Long> createHoveredEvent(Long dbId) {
        return new BinomialHoveredEvent(dbId);
    }

    @Override
    protected NursaPathwaySelectedEvent<Long> createSelectedEvent(Long dbId) {
        return new BinomialSelectedEvent(dbId);
    }

    private void initDisplay() {
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
        addColumn(nameColumn, "Pathway");
        
        Column<PathwaySummary, Number> pValueColumn = new Column<PathwaySummary, Number>(CellTypes.SCIENTIFIC_CELL) {
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
        addColumn(pValueColumn, "p-value");
       
        Column<PathwaySummary, Number> fdrColumn = new Column<PathwaySummary, Number>(CellTypes.DECIMAL_CELL) {
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
        addColumn(fdrColumn, "FDR");
        
        formatTableDimensions();
    }

}
