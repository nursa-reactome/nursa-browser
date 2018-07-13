package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedHandler;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedHandler;
import org.reactome.web.nursa.client.search.ExperimentSelectedEvent;
import org.reactome.web.nursa.client.search.ExperimentSelectedHandler;
import org.reactome.web.nursa.client.search.SearchDialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetPanel extends DockLayoutPanel
        implements BinomialCompletedHandler, GseaCompletedHandler, ExperimentSelectedHandler {

    private static final String SEARCH_DIALOG_TITLE = "Comparison Search";

    private ScrollPanel scrollPanel;

    private Panel contentPanel;
   
    private Integer analysisStartPosition;

    private EventBus eventBus;

    private SimpleEventBus compareEventBus;

    private DataSet dataset;

    private Experiment experiment;

    public DataSetPanel(DataSet dataset, Experiment experiment, EventBus eventBus) {
        super(Style.Unit.EM);
        this.dataset = dataset;
        this.experiment = experiment;
        this.eventBus = eventBus;
        this.compareEventBus = new SimpleEventBus();
        this.compareEventBus.addHandler(ExperimentSelectedEvent.TYPE, this);
        eventBus.addHandler(GseaCompletedEvent.TYPE, this);
        eventBus.addHandler(BinomialCompletedEvent.TYPE, this);
        addStyleName(RESOURCES.getCSS().main());
        topBar = getTopBar(dataset, experiment, eventBus);
        addNorth(topBar, 4.25);
        scrollPanel = new ScrollPanel();
        contentPanel = getExperimentContent(dataset, experiment, eventBus);
        scrollPanel.add(contentPanel);
        add(scrollPanel);
    }

    public void onLoad() {
        super.onLoad();
        analysisStartPosition = scrollPanel.getMaximumVerticalScrollPosition();
    }

    public void onResize() {
        super.onResize();
        // Reset the analysis start position, if necessary.
        // If the analysis start position was cleared on analysis
        // completion, then resize is irrelevant.
        if (analysisStartPosition != null) {
            analysisStartPosition = scrollPanel.getMaximumVerticalScrollPosition();;
        }
    }

    @Override
    public void onAnalysisCompleted(AnalysisResult result) {
        showAnalysisResult();
    }

    @Override
    public void onAnalysisCompleted(List<GseaAnalysisResult> result) {
        showAnalysisResult();
    }

    @Override
    public void onExperimentSelected(ExperimentSelectedEvent expSelectedEvent) {
        // Handle the comparison experiment.
        DataSet dataset = expSelectedEvent.getDataSet();
        Experiment experiment = expSelectedEvent.getExperiment();
        contentPanel = getComparisonContent(dataset, experiment, eventBus);
        scrollPanel.clear();
        scrollPanel.add(contentPanel);
    }

    private void showAnalysisResult() {
        // If this is the first analysis, then the analysis start position
        // was set by load or resize and not cleared by a previous call to
        // this method. In that case, scroll to the start of the analysis
        // section and clear the analysis start position variable. Clearing
        // the variable signals that subsequent analyses do not need to
        // scroll on completion.
        if (analysisStartPosition != null) {
            final int start = analysisStartPosition;
            Scheduler.get().scheduleDeferred(new Command() {
  
                public void execute () {
                    int max = scrollPanel.getMaximumVerticalScrollPosition();
                    int height = scrollPanel.getOffsetHeight();
                    int offset = topBar.getOffsetHeight();
                    int pos = Math.min(max, start + height - offset);
                    scrollPanel.setVerticalScrollPosition(pos);
                }
 
            });
            
            analysisStartPosition = null;
        }
    }

    private HorizontalPanel getTopBar(DataSet dataset, Experiment experiment, EventBus eventBus) {
        HorizontalPanel topBar = new HorizontalPanel();
        topBar.setWidth("95%");
        Widget ds = new HTMLPanel(dataset.getName());
        // Borrow the Details tab style.
        ds.setStyleName("elv-Details-Title-Wrap");
        topBar.add(ds);
        Widget doi = new HTMLPanel("DOI: " + dataset.getDoi());
        doi.setStyleName(RESOURCES.getCSS().doi());
        topBar.add(doi);
        Widget exp = new HTMLPanel("Experiment: " + experiment.getName());
        exp.setStyleName(RESOURCES.getCSS().experiment());
        topBar.add(exp);
        // The alignment directive below only applies to widgets added
        // after the property is set.
        topBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Button compare = new Button("Compare...");
        compare.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                // Open the comparand experiment search dialog.
                // Position the dialog box in the middle of the
                // application window.
                int offset = Window.getClientHeight() / 2;
                DialogBox dialog = new SearchDialog(compareEventBus, SEARCH_DIALOG_TITLE, offset);
                dialog.show();
            }
 
        });
        topBar.add(compare);
        topBar.setCellVerticalAlignment(ds, HasVerticalAlignment.ALIGN_MIDDLE);
        return topBar;
    }

    private Panel getExperimentContent(DataSet dataset, Experiment experiment, EventBus eventBus) {
        DataSetSections sections = new ExperimentSections(dataset, experiment, eventBus);
        VerticalPanel vp = new VerticalPanel();
        for (Widget section: sections) {
            vp.add(section);
        }
        return vp;
    }

    private Panel getComparisonContent(DataSet dataset, Experiment experiment, EventBus eventBus) {
        Comparison comparison = new Comparison(this.dataset, this.experiment, dataset, experiment);
        DataSetSections sections = new ComparisonSections(comparison, eventBus);
        VerticalPanel vp = new VerticalPanel();
        for (Widget section: sections) {
            vp.add(section);
        }
        return vp;
    }

    public static Resources RESOURCES;

    private HorizontalPanel topBar;

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
        String CSS = "DataSetPanel.css";

        String main();

        String doi();

        String experiment();

        String overview();

    }
}
