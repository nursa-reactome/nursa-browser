package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.AnalysisSummary;
import org.reactome.web.analysis.client.model.ExpressionSummary;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.analysis.client.model.ResourceSummary;
import org.reactome.web.pwp.client.common.events.AnalysisResetEvent;
import org.reactome.web.pwp.client.common.utils.Console;
import org.reactome.web.nursa.client.details.tabs.dataset.AnalysisResultFilterChangedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaClient;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class AnalysisDisplay extends Composite {

    private static final double DEF_RESULT_FILTER = 0.05;

    private static final String BINOMIAL_CONFIG = "Binomial Analysis Configuration";
    
    private static final String GSEA_CONFIG = "GSEA Configuration";

    /**
     * The UiBinder interface.
     */
    interface Binder extends UiBinder<Widget, AnalysisDisplay> {
    }
    
    static final Binder uiBinder = GWT.create(Binder.class);

    private static final String GENE_NAMES_HEADER = "#Gene names";
    
    /** The main panel. */
    @UiField()
    Panel content;
    
    /** The binomial analysis radio button. */
    @UiField()
    RadioButton binomialBtn;
    
    /** The GSEA analysis radio button. */
    @UiField()
    RadioButton gseaBtn;

    /** The launch display area. */
    @UiField()
    Widget launchPad;

    /** The launch button. */
    @UiField()
    Button launchBtn;

    /** The analysis running label. */
    @UiField()
    Label runningLbl;

    /** The configuration button. */
    @UiField()
    Button configBtn;

    /** The configuration settings text display. */
    @UiField()
    Label settingsLbl;

    /** The results filter panel. */
    @UiField()
    Widget filterPanel;

    /** The results filter entry box. */
    @UiField()
    DoubleBox filterBox;

    /** The analysis result display. */
    @UiField()
    protected SimplePanel resultsPanel;
    
    private BinomialConfigDisplay binomialConfig;
    
    private GseaConfigDisplay gseaConfig;

    protected EventBus eventBus;

    protected Double resultFilter = DEF_RESULT_FILTER;

    public AnalysisDisplay(EventBus eventBus) {
        this.eventBus = eventBus;
        binomialConfig = new BinomialConfigDisplay();
        gseaConfig = new GseaConfigDisplay();
        initWidget(uiBinder.createAndBindUi(this));
        buildDisplay();
    }

    public double getResultFilter() {
        return resultFilter == null ? DEF_RESULT_FILTER : resultFilter;
    }

    abstract protected void gseaAnalyse();

    abstract protected void binomialAnalyse();

    protected void showBinomialResult(List<PathwaySummary> results) {
        Widget panel = createBinomialPanel(results);
        resultsPanel.setWidget(panel);
    }

    protected void showGseaResult(List<GseaAnalysisResult> results) {
        Widget panel = createGseaPanel(results);
        resultsPanel.setWidget(panel);
    }

    protected void gseaAnalyse(List<DisplayableDataPoint> dataPoints, Consumer<List<GseaAnalysisResult>> consumer) {
        GseaClient client = GWT.create(GseaClient.class);
        // Transform the data points into the GSEA REST PUT
        // payload using the Java8 list comprehension idiom.
        // Only Reactome genes are included.
        List<List<String>> rankedList = filterDataPoints(dataPoints).stream()
                .map(AnalysisDisplay::pullRank)
                .collect(Collectors.toList());
        // The permutations parameter.
        Integer nperms = gseaConfig.getNperms();
        // The dataset size parameters.
        int[] dataSetBounds = gseaConfig.getDataSetSizeBounds();
        Integer dataSetSizeMinOpt = new Integer(dataSetBounds[0]);
        Integer dataSetSizeMaxOpt = new Integer(dataSetBounds[1]);
        
        // The result handler.
        MethodCallback<List<GseaAnalysisResult>> callback = new MethodCallback<List<GseaAnalysisResult>>() {

            @Override
            public void onSuccess(Method method, List<GseaAnalysisResult> result) {
                runningLbl.setVisible(false);
                launchBtn.setVisible(true);
                filterPanel.setVisible(true);
                consumer.accept(result);
            }

            @Override
            public void onFailure(Method method, Throwable exception) {
                runningLbl.setVisible(false);
                launchBtn.setVisible(true);
                filterPanel.setVisible(false);
                Console.error("GSEA execution unsuccessful: " + exception);
            }

        };
        
        // Call the GSEA REST service.
        client.analyse(rankedList, nperms, dataSetSizeMinOpt, dataSetSizeMaxOpt, callback);
    }

    private Collection<DisplayableDataPoint> filterDataPoints(List<DisplayableDataPoint> dataPoints) {
        HashMap<String, List<DisplayableDataPoint>> geneDataPointsMap =
                new HashMap<String, List<DisplayableDataPoint>>();
        for (DisplayableDataPoint dataPoint: dataPoints) {
            if (!dataPoint.isReactome()) {
                continue;
            }
            String symbol = dataPoint.getSymbol();
            List<DisplayableDataPoint> geneDataPoints = geneDataPointsMap.get(symbol);
            if (geneDataPoints == null) {
                geneDataPoints = new ArrayList<DisplayableDataPoint>();
                geneDataPointsMap.put(symbol, geneDataPoints);
            }
            geneDataPoints.add(dataPoint);
        }
        
        return geneDataPointsMap.values().stream()
                .map(AnalysisDisplay::summarize)
                .collect(Collectors.toList());
    }
    
    private static DisplayableDataPoint summarize(List<DisplayableDataPoint> dataPoints) {
        DisplayableDataPoint first = dataPoints.get(0);
        if (dataPoints.size() == 1) {
            return first;
        }
        
        DisplayableDataPoint summary = new DisplayableDataPoint();
        summary.setSymbol(first.getSymbol());
        summary.setReactome(first.isReactome());
        // The p-value is the geometric mean of the data point p-values.
        double[] pValues = dataPoints.stream()
                .mapToDouble(DisplayableDataPoint::getPvalue)
                .toArray();
        double pValue = geometricMean(pValues);
        summary.setPvalue(pValue);
        // The FC is that of the most significant data point.
        DisplayableDataPoint bestDataPoint = null;
        for (DisplayableDataPoint dataPoint: dataPoints) {
            if (bestDataPoint == null || dataPoint.getPvalue() < bestDataPoint.getPvalue()) {
                bestDataPoint = dataPoint;
            }
        }
        summary.setFoldChange(bestDataPoint.getFoldChange());
        
        return summary;
    }
    
    private static double geometricMean(double[] values) {
        double product = Arrays.stream(values)
                .reduce((accum, value) -> accum * value).getAsDouble();
        return Math.pow(product, 1.0 / values.length);
    }
    
    protected void binomialAnalyse(List<DisplayableDataPoint> dataPoints, Consumer<AnalysisResult> consumer) {
        // The input is a table of gene symbol lines.
        List<String> geneList = getBinomialGeneList(dataPoints);
        if (geneList.isEmpty()) {
            runningLbl.setVisible(false);
            launchBtn.setVisible(true);
            filterPanel.setVisible(true);
            AnalysisResult result = new AnalysisResult() {
                
                @Override
                public List<String> getWarnings() {
                    return null;
                }
                
                @Override
                public AnalysisSummary getSummary() {
                    return null;
                }
                
                @Override
                public List<ResourceSummary> getResourceSummary() {
                    return null;
                }
                
                @Override
                public Integer getPathwaysFound() {
                    return 0;
                }
                
                @Override
                public List<PathwaySummary> getPathways() {
                    return Collections.emptyList();
                }
                
                @Override
                public Integer getIdentifiersNotFound() {
                    return 0;
                }
                
                @Override
                public ExpressionSummary getExpression() {
                    return null;
                }
            };
            consumer.accept(result);
        }
        // Add the header required by the REST API call.
        geneList.add(0, GENE_NAMES_HEADER);
        // Make the REST payload text value.
        String data = String.join("\n", geneList);
        AnalysisClient.analyseData(data, true, false, 0, 0, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisServerException(String message) {
                runningLbl.setVisible(false);
                launchBtn.setVisible(true);
                filterPanel.setVisible(false);
                Console.error(message);
            }

            @Override
            public void onAnalysisResult(AnalysisResult result, long time) {
                runningLbl.setVisible(false);
                launchBtn.setVisible(true);
                filterPanel.setVisible(true);
                consumer.accept(result);
            }

            @Override
            public void onAnalysisError(AnalysisError error) {
                runningLbl.setVisible(false);
                launchBtn.setVisible(true);
                filterPanel.setVisible(false);
                Console.error(error.getReason());
            }

        });
    }

    /**
     * Filters the experiment data points based on the config.
     * 
     * @param dataPoints the Nursa dataset experiment data points
     * @return the ranked gene symbols list
     */
    private List<String> getBinomialGeneList(List<DisplayableDataPoint> dataPoints) {
        return filterDataPoints(dataPoints).stream()
                .filter(binomialConfig.getFilter())
                .map(dp -> dp.getSymbol())
                .collect(Collectors.toList());
    }

    /**
     * Pulls the gene symbol and FC from the given data point.
     * The FC is formatted as a string for the REST interface.
     * 
     * @param dataPoint the input data point.
     * @return the [symbol, FC] list.
     */
    private static List<String> pullRank(DataPoint dataPoint) {
        return Arrays.asList(
                dataPoint.getSymbol(),
                Double.toString(dataPoint.getFoldChange())
        );
    }

    private void buildDisplay() {
        content.addStyleName(RESOURCES.getCSS().main());
        // Note: it would be preferable to set the style for the
        // widgets below in AnalysisDisplay.ui.xml, e.g.:
        //     <ui:style src="AnalysisDisplay.css" />
        //     ...
        //     <g:Button ui:field='sliderBtn' addStyleNames="{style.sliderBtn}">
        // GWT and SO snippets suggest this is feasible and recommended, e.g.
        // https://stackoverflow.com/questions/1899007/add-class-name-to-element-in-uibinder-xml-file
        // However, doing so results in the following browser binding generation
        // code error:
        //   Exception: com.google.gwt.core.client.JavaScriptException: (TypeError) :
        //   this.get_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay_4_g$(...).style_19_g$ is not a function
        // The work-around is to set the style the old-fashioned verbose way with
        // the CSS resource below.
        launchPad.addStyleName(RESOURCES.getCSS().launchPad());
        launchBtn.addStyleName(RESOURCES.getCSS().launchBtn());
        gseaBtn.addStyleName(RESOURCES.getCSS().gseaBtn());
        runningLbl.addStyleName(RESOURCES.getCSS().running());
        configBtn.addStyleName(RESOURCES.getCSS().configBtn());
        settingsLbl.addStyleName(RESOURCES.getCSS().settingsLbl());
        filterPanel.addStyleName(RESOURCES.getCSS().filterPanel());
        filterBox.addStyleName(RESOURCES.getCSS().filterBox());
        filterBox.setValue(getResultFilter());
        filterPanel.setVisible(false);
        
        // The config button toggles the config panel display.
        configBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Open the configuration dialog.
                showConfig();
            }

        });
        
        // Per the RadioButton javadoc, the value change event is only
        // triggered when the button is clicked, not when it is cleared,
        // but it is good form to check the value here anyway.
        binomialBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    settingsLbl.setText(binomialConfig.formatFilter());
                }
            }

        });
        
        // The default analysis is binomial.
        binomialBtn.setValue(true);
        settingsLbl.setText(binomialConfig.formatFilter());
        
        gseaBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    settingsLbl.setText(formatGseaSettings());
                }
            }

        });
        
        runningLbl.setVisible(false);
        launchBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                launchBtn.setVisible(false);
                runningLbl.setVisible(true);
                resultsPanel.clear();
                AnalysisResetEvent resetEvent = new AnalysisResetEvent();
                eventBus.fireEventFromSource(event, resetEvent);
                if (gseaBtn.getValue()) {
                    gseaAnalyse();
                } else {
                    binomialAnalyse();
                }
            }
        });
        
        filterBox.addValueChangeHandler(new ValueChangeHandler<Double>() {

            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                resultFilter = event.getValue();
                // Redraw the tables.
                AnalysisResultFilterChangedEvent changedEvent =
                        new AnalysisResultFilterChangedEvent(resultFilter);
                eventBus.fireEventFromSource(changedEvent, this);
            }
            
        });
    }

    private String formatGseaSettings() {
        int permutations = gseaConfig.getNperms();
        int[] bounds = gseaConfig.getDataSetSizeBounds();
        // Alas, GWT does not support String.format().
        return Integer.toString(permutations) + " permutations, " + 
                Integer.toString(bounds[0]) +
                " <= dataset size < " +
                Integer.toString(bounds[1]);
    }

    private Widget createBinomialPanel(List<PathwaySummary> results) {
        return new BinomialAnalysisResultPanel(results, getResultFilter(), eventBus);
    }

    private Widget createGseaPanel(List<GseaAnalysisResult> results) {
        return new GseaAnalysisResultPanel(results, resultFilter, eventBus);
    }

    private void showConfig() {
        DialogBox dialog = createConfigDialog();
        dialog.show();
    }

    private DialogBox createConfigDialog() {
        if (binomialBtn.getValue()) {
            return createBinomialConfigDialog();
        } else {
            return createGseaConfigDialog();
        }
    }

    private DialogBox createBinomialConfigDialog() {
        AnalysisConfigDialog dialog =
                new AnalysisConfigDialog(binomialConfig, BINOMIAL_CONFIG);
        dialog.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                String settings = binomialConfig.formatFilter();
                settingsLbl.setText(settings);
            }
            
        });
        
        return dialog;
    }

    private DialogBox createGseaConfigDialog() {
        AnalysisConfigDialog dialog =
                new AnalysisConfigDialog(gseaConfig, GSEA_CONFIG);
        dialog.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                settingsLbl.setText(formatGseaSettings());
            }
            
        });
        
        return dialog;
    }
    
    private static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }
 
    /** A ClientBundle of resources used by this widget. */
    interface Resources extends ClientBundle {
 
        /** The styles used in this widget. */
        @Source(Css.CSS)
        Css getCSS();

    }

    /** Styles used by this widget. */
    interface Css extends CssResource {
 
        /** The path to the default CSS styles used by this resource. */
        String CSS = "AnalysisDisplay.css";

        String main();

        String gseaBtn();

        String launchPad();

        String launchBtn();

        String running();

        String configBtn();

        String settingsLbl();
        
        String filterPanel();
        
        String filterBox();

    }
}
