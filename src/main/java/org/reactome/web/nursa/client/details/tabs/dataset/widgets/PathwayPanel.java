package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.DataSet;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaClient;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PathwayPanel extends Composite {
    
    /**
     * The UiBinder interface.
     */
    interface Binder extends UiBinder<Widget, PathwayPanel> {
    }
    static final Binder uiBinder = GWT.create(Binder.class);

    final String GENE_NAMES_HEADER = "#Gene names";
    
    /**
     * The panel content.
     */
    @UiField()
    Panel content;
    
    /**
     * The GSEA analysis radio button.
     */
    @UiField()
    RadioButton gseaBtn;
    
    /**
     * The binomial analysis radio button.
     */
    @UiField()
    RadioButton binomialBtn;

    /**
     * The config slider button.
     */
    @UiField()
    Button launchBtn;

    /**
     * The config slider button.
     */
    @UiField()
    Button sliderBtn;

    /**
     * The config slider container.
     */
    @UiField()
    SimplePanel sliderPanel;

    /**
     * The analysis result display.
     */
    @UiField()
    SimplePanel analysisPanel;

    private DataSet dataset;

    private GseaConfigSlider gseaConfigSlider;

    private EventBus eventBus;

    public PathwayPanel(DataSet dataset, EventBus eventBus) {
        this.dataset = dataset;
        this.eventBus = eventBus;
        initWidget(uiBinder.createAndBindUi(this));
        // The configuration control panel.
        buildConfigPanel();
    }

    protected void gseaAnalyse() {
        GseaClient client = GWT.create(GseaClient.class);
        // Transform the data points into the GSEA REST PUT
        // payload using the Java8 list comprehension idiom.
        List<List<String>> rankedList =
                this.dataset.getDataPoints()
                            .stream()
                            .map(PathwayPanel::pullRank)
                            .collect(Collectors.toList());
        // Obtain the dataset size parameters.
        int[] dataSetBounds = gseaConfigSlider.getValues();
        Integer dataSetSizeMinOpt = new Integer(dataSetBounds[0]);
        Integer dataSetSizeMaxOpt = new Integer(dataSetBounds[1]);
        // Call the GSEA REST service.
        client.analyse(rankedList, dataSetSizeMinOpt, dataSetSizeMaxOpt, new MethodCallback<List<GseaAnalysisResult>>() {

            @Override
            public void onSuccess(Method method, List<GseaAnalysisResult> result) {
                showGseaResult(result);
                eventBus.fireEventFromSource(new GseaAnalysisCompletedEvent(), PathwayPanel.this);
            }

            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    throw new IOException("GSEA execution unsuccessful", exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }
         });
    }

    protected void showGseaResult(List<GseaAnalysisResult> result) {
        Widget table = GseaResultTableFactory.createTable(result);
        analysisPanel.setWidget(table);
    }

    protected void binomialAnalyse() {
        // The input is a table of gene symbol lines.
        List<String> rankedList =
                this.dataset.getDataPoints()
                    .stream()
                    .map(dp -> dp.getSymbol())
                    .collect(Collectors.toList());
        rankedList.add(0, GENE_NAMES_HEADER);
        String data = String.join("\n", rankedList);
        AnalysisClient.analyseData(data, true, false, 0, 0, new AnalysisHandler.Result() {
            @Override
            public void onAnalysisServerException(String s) {
                // TODO
            }

            @Override
            public void onAnalysisResult(AnalysisResult result, long time) {
                showBinomialResult(result);
                eventBus.fireEventFromSource(new BinomialAnalysisCompletedEvent(result), PathwayPanel.this);
            }

            @Override
            public void onAnalysisError(AnalysisError analysisError) {
                // TODO
            }

        });
    }

    protected void showBinomialResult(org.reactome.web.analysis.client.model.AnalysisResult result) {
        Widget table = BinomialResultTableFactory.getTable(result);
        analysisPanel.setWidget(table);
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

    private void buildConfigPanel() {
        content.addStyleName(RESOURCES.getCSS().main());
 
        launchBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (gseaBtn.getValue()) {
                    gseaAnalyse();
                } else {
                    binomialAnalyse();
                }
            }
        });

        // For now, the slider panel only applies to GSEA.
        // Per the RadioButton javadoc, the value change event is only
        // triggered when the button is clicked, not when it is cleared,
        // but it is good form to check the value here anyway.
        gseaBtn.setValue(true);
        gseaBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    sliderBtn.setVisible(true);
                }
            }

        });
        
        // Note: it would be preferable to set the style in PathwayPanel.ui.xml:
        //     <ui:style src="PathwayPanel.css" />
        // and then add the styles to the corresponding widgets, e.g:
        //     <g:Button ui:field='sliderBtn' addStyleNames="{style.sliderBtn}">
        // GWT and SO snippets suggest this is feasible and recommended, e.g.
        // https://stackoverflow.com/questions/1899007/add-class-name-to-element-in-uibinder-xml-file
        // However, doing so results in the following browser binding generation
        // code error:
        //   Exception: com.google.gwt.core.client.JavaScriptException: (TypeError) :
        //   this.get_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay_4_g$(...).style_19_g$ is not a function
        // The work-around is to set the style the old-fashioned verbose way with
        // the CSS resource below.
        sliderBtn.addStyleName(RESOURCES.getCSS().sliderBtn());
        //sliderBtn.getElement().getStyle().setPadding(1, Unit.PX);
        //sliderBtn.getElement().getStyle().setPaddingTop(3, Unit.PX);
        sliderBtn.setVisible(gseaBtn.getValue());
        gseaConfigSlider = new GseaConfigSlider();
        sliderPanel.addStyleName("gsea-config");
        sliderPanel.getElement().appendChild(gseaConfigSlider.getElement());
        sliderPanel.setVisible(false);
        // The slider button toggles the slider panel display.
        sliderBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                sliderPanel.setVisible(!sliderPanel.isVisible());
            }

        });

        binomialBtn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    sliderBtn.setVisible(false);
                    sliderPanel.setVisible(false);
                }
            }

        });
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }
 
    /**
     * A ClientBundle of resources used by this widget.
     */
    interface Resources extends ClientBundle {
 
        /**
         * The styles used in this widget.
         */
        @Source(Css.CSS)
        Css getCSS();

    }

    /**
     * Styles used by this widget.
     */
    interface Css extends CssResource {
 
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "PathwayPanel.css";

        String main();

        String sliderBtn();

    }
}
