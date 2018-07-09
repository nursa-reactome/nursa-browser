package org.reactome.web.nursa.analysis.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwayBase;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.nursa.client.details.tabs.dataset.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;

/**
 * This PseudoAnalysisResult class mocks a GSEA or comparison
 * result as a Reactome {@link AnalysisResult}.
 * 
 * @author Fred Loney <loneyf@ohsu.edu> 
 */
public class PseudoAnalysisResult implements AnalysisResult {

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
                if (analysisType != AnalysisType.OVERREPRESENTATION) {
                    throw new UnsupportedOperationException("Analysis type is always " + getType());
                }
            }
            
            @Override
            public String getType() {
                return AnalysisType.OVERREPRESENTATION.name();
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
//        Double min = Stream.of(this.pathways)
//                                  .map(p -> p.getEntities().getpValue())
//                                  .min(Comparator.comparing(Double::valueOf))
//                                  .get();
//        Double max = Stream.of(this.pathways)
//                                  .map(p -> p.getEntities().getpValue())
//                                  .max(Comparator.comparing(Double::valueOf))
//                                  .get();
        return new PseudoExpressionSummary(0.0, 0.05);
    }

    /**
     * Creates a {@link ComparisonPseudoExpressionSummary} with an
     * empty column list.
     */
    private ExpressionSummary createExpressionSummary(Comparison comparison) {
        // DOI doesn't fit in Reactome expression bar.
        // Use First and Second instead.
        // TODO - color code tab experiment name/doi
        // and use same colors in the expression bar.
        List<String> dataSetDescriptors = Arrays.asList("First", "Second");
//        List<String> dataSetDescriptors = Stream.of(comparison.operands)
//                .map(operand -> operand.dataset.getDoi())
//                .collect(Collectors.toList());
        return new ComparisonPseudoExpressionSummary(dataSetDescriptors);
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
