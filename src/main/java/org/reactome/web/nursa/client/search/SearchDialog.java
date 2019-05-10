package org.reactome.web.nursa.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.function.Consumer;

import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.diagram.common.PwpButton;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.nursa.client.search.DataSetSearcher.DataSetAdapter;
import org.reactome.web.widgets.search.Searcher;
import org.reactome.web.widgets.search.SuggestionComboBox;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SearchDialog extends DialogBox implements ClickHandler {

    private static final int PAGE_SIZE = 10;

    private static final String HELP_TEXT =
            "Enter three or more search characters. The matching datasets" +
            " are listed with name, description and DOI. If the name or" +
            " description is elided, hover over the field to see the full text." +
            " Select a dataset and click Load to display it in the DataSet tab below.";

    protected DataSet dataset;

    protected EventBus eventBus;

    private Button loadBtn;

    private SuggestionComboBox<DataSetAdapter> suggestionBox;

    public SearchDialog(EventBus eventBus, String title, int verticalPosition) {
        this.eventBus = eventBus;
        setAutoHideEnabled(false);
        setModal(true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setAutoHideOnHistoryEventsEnabled(false);
        setStyleName(RESOURCES.getCSS().main());
        setTitlePanel(title);
        
        // The search dialog content container panel.
        FlowPanel container = new FlowPanel();
        initDisplay(container);
        setWidget(container);

        // Unfortunately, the dialog position must be set programatically.
        // GWT sets the element position to top left in the DOM, so a CSS
        // position is ignored. Set the offset width to 2/3 of the
        // display. The caller specifies the offset height.
        setPopupPositionAndShow(new PositionCallback(){

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                // The old values are ignored. Place the pop-up in the
                // right third of the display area just below the top panel.
                int horizontalPosition = 2 * Window.getClientWidth() / 3;
                setPopupPosition(horizontalPosition, verticalPosition);
            }

        });      
    }

    @Override
    public void show() {
        super.show();
        // For some reason, GWT compile can't find the following method,
        // perhaps because the suggestion box is not itself a widget.
        // The only impact of this bug is that the user has to click
        // in the suggestion input widget to get focus, which is a
        // reasonable expectation.
        //suggestionBox.setFocus(true);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        hide();
    }

    private void initDisplay(ComplexPanel container) {
        // The searcher provides suggestions.
        Searcher searcher = new DataSetSearcher();
        // The suggestion selection consumer.
        Consumer<DataSetAdapter> consumer = new Consumer<DataSetAdapter>() {

            @Override
            public void accept(DataSetAdapter suggestion) {
                
                loadBtn.setEnabled(suggestion != null);
                if (suggestion != null) {
                    dataset = suggestion.getDataset();
                }
            }
  
        };

        // The dialog close button.
        PwpButton close = new PwpButton("Close", RESOURCES.getCSS().close(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SearchDialog.this.hide();
            }
            
        });
        container.add(close);

        // The dataset search child.
        ComplexPanel searchPanel = new FlowPanel();
        suggestionBox = new SuggestionComboBox<DataSetAdapter>(searcher, consumer, PAGE_SIZE);
        Widget combo = suggestionBox.asWidget();
        combo.addStyleName(RESOURCES.getCSS().combo());
        searchPanel.add(combo);

        // Double click loads the suggested dataset.
        suggestionBox.addDoubleClickConsumer(new Consumer<DataSetAdapter>() {
 
            @Override
            public void accept(DataSetAdapter suggestion) {
                loadDataSet(container, searchPanel);
            }

        });

        // The dataset load button.
        loadBtn = new Button("Load");
        loadBtn.setStyleName(RESOURCES.getCSS().load());
        loadBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                loadDataSet(container, searchPanel);
            }

        });
        loadBtn.setEnabled(false);
        searchPanel.add(loadBtn);
        
        // The help tooltip.
        Widget info = getInfo();
        searchPanel.add(info);
        
        // Add the search panel to the container.
        // This will be swapped out for the experiment selection panel
        // when the dataset is selected and loaded.
        container.add(searchPanel);
    }
    
    protected void loadDataSet(ComplexPanel container, ComplexPanel searchPanel) {
        container.remove(searchPanel);
        HTML loadingMsg = new HTML("Loading the dataset...");
        container.add(loadingMsg);
        // Get the experiments.
        DataSetLoader.getDataset(dataset.getDoi(), new Consumer<DataSet>() {

            @Override
            public void accept(DataSet dataset) {
                // Select an experiment, if necessary.
                List<Experiment> experiments = dataset.getExperiments();
                container.remove(loadingMsg);
                if (experiments.size() == 1) {
                    loadDataPoints(experiments.get(0), container);
                } else {
                    container.add(new ExperimentSelector(experiments, new Consumer<Integer>() {

                        @Override
                        public void accept(Integer expNdx) {
                            Experiment experiment = experiments.get(expNdx);
                            loadDataPoints(experiment, container);
                        }
                        
                    }));
                 }
            }
            
        });
    }
    
    protected void loadDataPoints(Experiment experiment, ComplexPanel container) {
        HTML loadingMsg = new HTML("Loading the experiment data points...");
        container.add(loadingMsg);
        // Fire the experiment loaded event after the data points are fetched.
        Consumer<List<DisplayableDataPoint>> consumer = new Consumer<List<DisplayableDataPoint>>() {

            @Override
            public void accept(List<DisplayableDataPoint> dataPoints) {
                DataPointsLoadedEvent event = new DataPointsLoadedEvent(dataset, experiment, dataPoints);
                eventBus.fireEventFromSource(event, SearchDialog.this);
                SearchDialog.this.hide();
            }
            
        };
        // Fetch the experiment data points.
        DataPointsLoader.getDataPoints(dataset.getDoi(), experiment.getId(), consumer);
    }
    
    private void setTitlePanel(String title) {
        Label label = new Label(title);
        label.setStyleName(RESOURCES.getCSS().headerText());
        SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant(label.toString());
        getCaption().setHTML(safeHtml);
        getCaption().asWidget().setStyleName(RESOURCES.getCSS().header());
    }
    
    /**
     * @return Widget a short instruction on how to use the search dialog
     */
    private Widget getInfo() {
        // This builder is adapted from MoleculesPanel.
        Panel infoPanel = new HorizontalPanel();
        infoPanel.setStyleName(RESOURCES.getCSS().info());
        infoPanel.setTitle(HELP_TEXT);
        Image image = new Image(CommonImages.INSTANCE.information());
        infoPanel.add(image);
        HTMLPanel label = new HTMLPanel("Info");
        infoPanel.add(label);
        return infoPanel;
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
 
        @Source(Css.CSS)
        Css getCSS();

        @Source("images/minihelp_normal.png")
        ImageResource info();

        @Source("images/close_normal.png")
        ImageResource closeNormal();

        @Source("images/close_hovered.png")
        ImageResource closeHovered();

        @Source("images/close_clicked.png")
        ImageResource closeClicked();

    }

    public interface Css extends CssResource {

        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "SearchDialog.css";

        String main();

        String info();

        String load();

        String header();

        String headerText();

        String combo();

        String close();

    }

}
