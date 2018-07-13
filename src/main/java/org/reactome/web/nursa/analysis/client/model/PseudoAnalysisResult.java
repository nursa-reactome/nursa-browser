package org.reactome.web.nursa.analysis.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwayBase;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;
import org.reactome.web.nursa.model.Comparison;

/**
 * This PseudoAnalysisResult class mocks a GSEA or comparison
 * result as a Reactome {@link AnalysisResult}.
 * 
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class PseudoAnalysisResult implements AnalysisResult {

    private static final String TYPE_ERROR_MSG = "A comparison analysis type cannot be set";
    
    private AnalysisSummary summary;
    private PseudoPathwaySummary[] pathways;
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
        resources = createResourceSummary();
        pathways = results.stream()
                          .map(result -> new PseudoPathwaySummary(result))
                          .collect(Collectors.toList())
                          .toArray(new PseudoPathwaySummary[results.size()]);
        expression = createExpressionSummary(); 
    }

    /**
     * Converts the given comparison results to an AnalysisResult.
     * 
     * @param comparison the enrichment input experiments
     * @param partition the shared/unshared pathway data point lists
     * @param token a short digest string that is (almost) unique for
     *        each dataset analysis input
     */
    public PseudoAnalysisResult(Comparison comparison, ComparisonPartition<?> partition, String token) {
        this(token);
        resources = createResourceSummary();
        pathways = partition.createPseudoPathwaySummary();
        expression = createExpressionSummary(comparison); 
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
        return Arrays.asList(pathways);
    }

    public List<PathwayBase> getPseudoFilteredPathways() {
        return Arrays.asList(pathways);
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
    
    public SpeciesFilteredResult asSpeciesFilteredResult() {
        return new SpeciesFilteredResult() {
            
            @Override
            public void setAnalysisType(AnalysisType analysisType) {
                if (analysisType != getAnalysisType()) {
                    throw new UnsupportedOperationException(TYPE_ERROR_MSG);
                }
            }
            
            @Override
            public String getType() {
                // The FireworksViewer sets the analysis type to the result
                // of this method. It is unknown why the analysis type is
                // handled this way, but this implementation works so far.
                return getAnalysisType().toString();
            }
            
            @Override
            public List<PathwayBase> getPathways() {
                return Arrays.asList(pathways);
            }
            
            @Override
            public ExpressionSummary getExpressionSummary() {
                return getExpression();
            }
            
            @Override
            public AnalysisType getAnalysisType() {
                return AnalysisType.OVERREPRESENTATION;
            }
        };
    }
    
    /**
     * Creates a minimal {@link ExpressionSummary} with the min and max
     * pathway p-values and an empty column list.
     */
    private ExpressionSummary createExpressionSummary() {
        // Base Reactome hard-codes min to 0 and max to 0.05 in various
        // places, so we must go along with that here.
        return new PseudoExpressionSummary(0.0, 0.05);
    }

    /**
     * Creates a {@link ComparisonPseudoExpressionSummary} with an
     * empty column list.
     */
    private ExpressionSummary createExpressionSummary(Comparison comparison) {
        return new ComparisonPseudoExpressionSummary();
    }

    /**
     * Reactome requires a non-empty resource summary list.
     * This method returns a resource summary singleton list whose
     * sole member has a null resource and zero pathway count.
     * 
     * @return the minimal {@link ResourceSummary} singleton list
     */
    private List<ResourceSummary> createResourceSummary() {
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
