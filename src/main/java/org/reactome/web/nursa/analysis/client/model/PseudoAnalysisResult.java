package org.reactome.web.nursa.analysis.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;

/**
 * This PseudoAnalysisResult class mocks a GSEA or comparison
 * result as a Reactome {@link AnalysisResult}.
 * 
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class PseudoAnalysisResult implements AnalysisResult {

    private AnalysisSummary summary;
    private List<PathwaySummary> pathways;
    private List<ResourceSummary> resources;

    private ExpressionSummary expression;

    private PseudoAnalysisResult(String token) {
        summary = new PseudoAnalysisSummary(token);
    }

    /**
     * @param result the GSEA analysis output
     * @param token a short digest string that is (almost) unique for
     *        each dataset analysis input
     */
    public PseudoAnalysisResult(List<GseaAnalysisResult> results, String token) {
        this(token);
        resources = createPseudoResourceSummary();
        pathways = results.stream()
                          .map(result -> new PseudoPathwaySummary(result))
                          .collect(Collectors.toList());
        expression = createExpression(); 
    }

    /**
     * Converts the given comparison results to an AnalysisResult.
     * 
     * @param result the comparison analysis output
     * @param token a short digest string that is (almost) unique for
     *        each dataset analysis input
     */
    public PseudoAnalysisResult(ComparisonPartition<?> partition, String token) {
        this(token);
        resources = createPseudoResourceSummary();
        pathways = partition.createPseudoPathwaySummary();
        expression = createExpression(); 
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

    @Override
    public ExpressionSummary getExpression() {
        return expression;
    }

    @Override
    public List<String> getWarnings() {
        return new ArrayList<>();
    }
    
    /**
     * Creates a minimal {@link ExpressionSummary} with
     * the min and max pathway p-values and an empty column
     * list.
     */
    private ExpressionSummary createExpression() {
        Double min = this.pathways.stream()
                                  .map(p -> p.getEntities().getpValue())
                                  .min(Comparator.comparing(Double::valueOf))
                                  .get();
        Double max = this.pathways.stream()
                                  .map(p -> p.getEntities().getpValue())
                                  .max(Comparator.comparing(Double::valueOf))
                                  .get();
        return new PseudoExpressionSummary(min, max);
    }

    /**
     * Reactome requires a non-empty resource summary list.
     * This method returns a resource summary singleton list whose
     * sole member has a null resource and zero pathway count.
     * 
     * @return the minimal {@link ResourceSummary} singleton list
     */
    private List<ResourceSummary> createPseudoResourceSummary() {
        // TODO - is there any adverse side-effect to this?
        ResourceSummary emptyItem = new ResourceSummary() {
            
            @Override
            public String getResource() {
                return PseudoResourceSummary.RESOURCE;
            }
            
            @Override
            public Integer getPathways() {
                return 0;
            }
        };

        return Arrays.asList(emptyItem);
    }

}
