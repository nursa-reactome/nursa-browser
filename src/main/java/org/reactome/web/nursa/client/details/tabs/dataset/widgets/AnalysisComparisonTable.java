package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.reactome.web.nursa.model.Comparison;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

abstract public class AnalysisComparisonTable<R, K>
extends AnalysisResultTable<Map.Entry<String, List<R>>, K> {

    protected AnalysisComparisonTable(Map<String, List<R>> results) {
        super(results.entrySet()
                     .stream()
                     .sorted(Map.Entry.comparingByKey())
                     .collect(Collectors.toList()));
 
        // The pathway name column.
        TextColumn<Map.Entry<String, List<R>>> nameColumn =
                new TextColumn<Map.Entry<String, List<R>>>() {
            @Override
            public String getValue(Map.Entry<String, List<R>> result) {
              return result.getKey();
            }
        };
        nameColumn.setSortable(true);
        sorter.setComparator(nameColumn, new Comparator<Map.Entry<String, List<R>>>() {
            @Override
            public int compare(
                    Map.Entry<String, List<R>> r1,
                    Map.Entry<String, List<R>> r2) {
                return r1.getKey().compareTo(r2.getKey());
            }
        });
        addColumn(nameColumn, "Name");
        
        for (int i=0; i < 2; i++) {
            final int index = i;
            String qualifier = Comparison.LABELS[i];
            Column<Map.Entry<String, List<R>>, Number> pValueColumn =
                    new Column<Map.Entry<String, List<R>>, Number>(SCIENTIFIC_CELL) {
                @Override
                public Number getValue(Map.Entry<String, List<R>> result) {
                    return getPvalue(result.getValue().get(index));
                }

            };
            pValueColumn.setSortable(true);
            sorter.setComparator(pValueColumn, new Comparator<Map.Entry<String, List<R>>>() {
                @Override
                public int compare(
                        Map.Entry<String, List<R>> r1,
                        Map.Entry<String, List<R>> r2) {
                    double pvalue1 = getPvalue(r1.getValue().get(index));
                    double pvalue2 = getPvalue(r2.getValue().get(index));
                    return Double.compare(pvalue1, pvalue2);
                }
            });
            
            Column<Map.Entry<String, List<R>>, Number> fdrColumn =
                    new Column<Map.Entry<String, List<R>>, Number>(DECIMAL_CELL) {
                @Override
                public Number getValue(Map.Entry<String, List<R>> result) {
                    return getFdr(result.getValue().get(index));
                }
            };
            fdrColumn.setSortable(true);
            sorter.setComparator(fdrColumn, new Comparator<Map.Entry<String, List<R>>>() {
                @Override
                public int compare(
                        Map.Entry<String, List<R>> r1,
                        Map.Entry<String, List<R>> r2) {
                    double fdr1 = getFdr(r1.getValue().get(index));
                    double fdr2 = getFdr(r2.getValue().get(index));
                    return Double.compare(fdr1,fdr2);
                }
            });
            
            // Add the columns.
            addColumn(pValueColumn, qualifier + " P-Value");
            addColumn(fdrColumn, qualifier + " FDR");
        }
    }
    
    abstract protected Double getPvalue(R result);
    
    abstract protected Double getFdr(R result);

}
