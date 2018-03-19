package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialSummary;

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class BinomialResult implements AnalysisResult {
     
    private AnalysisSummary summary;
    private List<PathwaySummary> pathways;

    public BinomialResult() {
        this.summary = new BinomialSummary();
    }

    public BinomialResult(List<GseaAnalysisResult> gseaResult) {
        this();
        pathways = gseaResult.stream()
                             .map(BinomialPathwaySummary::transform)
                             .collect(Collectors.toList());
    }

    @Override
    public AnalysisSummary getSummary() {
        return summary;
    }

    @Override
    public Integer getPathwaysFound() {
        return 0;
    }

    @Override
    public Integer getIdentifiersNotFound() {
        return 0;
    }

    @Override
    public List<PathwaySummary> getPathways() {
        return pathways;
    }

    @Override
    public List<ResourceSummary> getResourceSummary() {
        return Arrays.asList(new ResourceSummary() {
            
            @Override
            public String getResource() {
                return "";
            }
            
            @Override
            public Integer getPathways() {
                return 0;
            }
        });
    }

    @Override
    public ExpressionSummary getExpression() {
        return new ExpressionSummary() {
            
            @Override
            public Double getMin() {
                return 0.0;
            }
            
            @Override
            public Double getMax() {
                return 0.0;
            }
            
            @Override
            public List<String> getColumnNames() {
                return new ArrayList<>();
            }
        };
    }

    @Override
    public List<String> getWarnings() {
        return new ArrayList<>();
    }

}
