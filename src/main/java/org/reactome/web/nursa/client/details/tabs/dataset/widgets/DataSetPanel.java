package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.nursa.model.DataSet;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialAnalysisCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaAnalysisCompletedHandler;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
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
        implements BinomialAnalysisCompletedHandler, GseaAnalysisCompletedHandler {
    /**
     * The UiBinder interface.
     */
//    interface Binder extends UiBinder<Widget, DataSetPanel> {
//    }
//    static final Binder uiBinder = GWT.create(Binder.class);

    private ScrollPanel scrollPanel;

    private Panel contentPanel;
   
    private Integer analysisStartPosition;

    public DataSetPanel(DataSet dataset, EventBus eventBus) {
        super(Style.Unit.EM);
        eventBus.addHandler(GseaAnalysisCompletedEvent.TYPE, this);
        eventBus.addHandler(BinomialAnalysisCompletedEvent.TYPE, this);
        addStyleName(RESOURCES.getCSS().main());
        topBar = getTopBar(dataset);
        addNorth(topBar, 4);
        scrollPanel = new ScrollPanel();
        contentPanel = getMainContent(dataset, eventBus);
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
    public void onAnalysisCompleted() {
        showAnalysisResult();
    }

    private void showAnalysisResult() {
        // If this is the first analysis, then the analysis start position
        // was set by load or resize and not cleared by a previous call to
        // this method. In the case, scroll to the start of the analysis
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

    private HorizontalPanel getTopBar(DataSet dataset) {
        HorizontalPanel topBar = new HorizontalPanel();
        topBar.setWidth("95%");
        Widget name = new HTMLPanel(dataset.getName());
        // Borrow the Details tab style.
        name.setStyleName("elv-Details-Title-Wrap");
        topBar.add(name);
        Widget doi = new HTMLPanel("DOI: " + dataset.getDoi());
        doi.setStyleName(RESOURCES.getCSS().doi());
        topBar.add(doi);
        // The alignment directive below only applies to widgets added
        // after the property is set.
        topBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Button compare = new Button("Compare...");
        topBar.add(compare);
        topBar.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
        return topBar;
    }

    private Panel getMainContent(DataSet dataset, EventBus eventBus) {
        DataSetSections sections = new DataSetSections(dataset, eventBus);
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

        String overview();

    }
}
