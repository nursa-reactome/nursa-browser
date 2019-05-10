package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaExperimentTable extends AnalysisResultTable<GseaAnalysisResult, String> {

    public GseaExperimentTable(List<GseaAnalysisResult> results) {
        super(results);
        initDisplay();
    }

    @Override
    protected String getKey(GseaAnalysisResult result) {
        return result.getPathway().getStId();
    }

    @Override
    protected NursaPathwayHoveredEvent<String> createHoveredEvent(String stId) {
        return new GseaHoveredEvent(stId);
    }

    @Override
    protected NursaPathwaySelectedEvent<String> createSelectedEvent(String stId) {
        return new GseaSelectedEvent(stId);
    }

    private void initDisplay() {
        // The display columns.
        TextColumn<GseaAnalysisResult> nameColumn = new TextColumn<GseaAnalysisResult>() {
            @Override
            public String getValue(GseaAnalysisResult result) {
              return result.getPathway().getName();
            }
        };
        nameColumn.setSortable(true);
        sorter.setComparator(nameColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return r1.getPathway().getName().compareTo(r2.getPathway().getName());
            }
        });
        addColumn(nameColumn, "Pathway");
       
        Column<GseaAnalysisResult, Number> hitsColumn = new Column<GseaAnalysisResult, Number>(INTEGER_CELL) {
            @Override
            public Integer getValue(GseaAnalysisResult result) {
                return result.getHitCount();
            }
        };
        hitsColumn.setSortable(true);
        sorter.setComparator(hitsColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return Integer.compare(r1.getHitCount(), r2.getHitCount());
            }
        });
        addColumn(hitsColumn, "Hits");
        
        Column<GseaAnalysisResult, Number> scoreColumn = new Column<GseaAnalysisResult, Number>(CellTypes.DECIMAL_CELL) {
            @Override
            public Number getValue(GseaAnalysisResult result) {
                return result.getScore();
            }
        };
        scoreColumn.setSortable(true);
        sorter.setComparator(scoreColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return Double.compare(r1.getScore(), r2.getScore());
            }
        });
        addColumn(scoreColumn, "Score");
        
        Column<GseaAnalysisResult, Number> nesColumn = new Column<GseaAnalysisResult, Number>(CellTypes.DECIMAL_CELL) {
            @Override
            public Number getValue(GseaAnalysisResult result) {
                return result.getNormalizedScore();
            }
        };
        nesColumn.setSortable(true);
        sorter.setComparator(nesColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return Double.compare(r1.getNormalizedScore(), r2.getNormalizedScore());
            }
        });
        addColumn(nesColumn, "NES");
        
        Column<GseaAnalysisResult, Number> pValueColumn = new Column<GseaAnalysisResult, Number>(CellTypes.SCIENTIFIC_CELL) {
            @Override
            public Number getValue(GseaAnalysisResult result) {
                return result.getPvalue();
            }
        };
        pValueColumn.setSortable(true);
        sorter.setComparator(pValueColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return Double.compare(r1.getPvalue(), r2.getPvalue());
            }
        });
        addColumn(pValueColumn, "p-value");
        
        Column<GseaAnalysisResult, Number> fdrColumn = new Column<GseaAnalysisResult, Number>(CellTypes.DECIMAL_CELL) {
            @Override
            public Number getValue(GseaAnalysisResult result) {
                return result.getFdr();
            }
        };
        fdrColumn.setSortable(true);
        sorter.setComparator(fdrColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return Double.compare(r1.getFdr(), r2.getFdr());
            }
        });
        addColumn(fdrColumn, "FDR");

        Column<GseaAnalysisResult, String> regTypeColumn = new TextColumn<GseaAnalysisResult>() {
            @Override
            public String getValue(GseaAnalysisResult result) {
                return result.getRegulationType().toString();
            }
        };
        regTypeColumn.setSortable(true);
        sorter.setComparator(regTypeColumn, new Comparator<GseaAnalysisResult>() {
            @Override
            public int compare(GseaAnalysisResult r1, GseaAnalysisResult r2) {
                return r1.getRegulationType().compareTo(r2.getRegulationType());
            }
        });
        addColumn(regTypeColumn, "Regulation Type");
        
        formatTableDimensions();
    }

}
