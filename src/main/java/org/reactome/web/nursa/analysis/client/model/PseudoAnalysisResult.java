package org.reactome.web.nursa.analysis.client.model;

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

/**
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class BinomialResult implements AnalysisResult {

    public static final String GSEA_RESOURCE = "GSEA";
     
    private AnalysisSummary summary;
    private List<PathwaySummary> pathways;
    private List<ResourceSummary> resources;

    private BinomialResult(String token) {
        summary = new BinomialSummary(token);
    }

    /**
     * @param result the GSEA analysis output
     * @param token a short digest string that is (almost) unique for
     *        each dataset analysis input
     */
    public BinomialResult(List<GseaAnalysisResult> result, String token) {
        this(token);
        resources = getGseaResourceSummary();
        pathways = result.stream()
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
        return resources;
    }

    /**
     * @return a minimal {@link ExpressionSummary} with zero
     *         min and max and empty column list
     */
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

    /**
     * Reactome requires a non-empty resource summary list.
     * This method returns a resource summary singleton list whose
     * sole member has a null resource and zero pathway count.
     * 
     * @return the minimal {@link ResourceSummary} singleton list
     */
    private List<ResourceSummary> getGseaResourceSummary() {
        ResourceSummary emptyItem = new ResourceSummary() {
            
            @Override
            public String getResource() {
                return GSEA_RESOURCE;
            }
            
            @Override
            public Integer getPathways() {
                return 0;
            }
        };

        return Arrays.asList(emptyItem);
    }

}
