package org.reactome.web.nursa.client.toppanel.dataset;

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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.function.Consumer;

import org.reactome.nursa.model.DataSet;
import org.reactome.web.diagram.common.PwpButton;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSearcher.DataSetAdapter;
import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.nursa.client.common.events.DataSetSelectedEvent;
import org.reactome.web.widgets.search.Searcher;
import org.reactome.web.widgets.search.SuggestionComboBox;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SearchDialog extends DialogBox implements ClickHandler {

    private static final String HELP_TEXT =
            "Enter three or more search characters. The matching datasets" +
            " are listed with name, description and DOI. If the name or" +
            " description is elided, hover over the field to see the full text." +
            " Select a dataset and click Load to display it in the DataSet tab below.";

    private static final int TOP_PANEL_HEIGHT = 42;

    protected DataSet dataset;

    private Button loadBtn;

    public SearchDialog(final EventBus eventBus) {
        setAutoHideEnabled(false);
        setModal(true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setAutoHideOnHistoryEventsEnabled(false);
        setStyleName(RESOURCES.getCSS().main());
        setTitlePanel("DataSet Search");

        // Unfortunately, the dialog position must be set programatically.
        // GWT sets the element position to top left in the DOM, so a CSS
        // position is ignored. We want the dialog in the top left just
        // below the top panel, so set the offset width to 2/3 of the
        // display and the offset height to 40 pixels, which is roughly
        // the height of the top panel. Kind of a kludge, but the alternative
        // requires overriding the dialog box position using the top panel
        // offsets. This probably would need to be done by the app controller,
        // which is even more of a kludge.
        setPopupPositionAndShow(new PositionCallback(){

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                // The old values are ignored. Place the pop-up in the
                // right third of the display area just below the top panel.
                offsetWidth = 2 * Window.getClientWidth() / 3;
                setPopupPosition(offsetWidth, TOP_PANEL_HEIGHT);
            }

        });      
        
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
        // The search dialog content container panel.
        FlowPanel container = new FlowPanel();
        // The dialog close button.
        PwpButton close = new PwpButton("Close", RESOURCES.getCSS().close(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SearchDialog.this.hide();
            }
            
        });
        container.add(close);
        // The suggestion combo box shows suggestions.
        SuggestionComboBox<DataSetAdapter> comboBox =
                new SuggestionComboBox<DataSetAdapter>(searcher, consumer);
        Widget combo = comboBox.asWidget();
        combo.addStyleName(RESOURCES.getCSS().combo());
        container.add(combo);
        // The dataset load button.
        loadBtn = new Button("Load");
        loadBtn.setStyleName(RESOURCES.getCSS().load());
        loadBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new DataSetSelectedEvent(dataset), this);
                SearchDialog.this.hide();
            }
            
        });
        loadBtn.setEnabled(false);
        container.add(loadBtn);
        // The help tooltip.
        Widget info = getInfo();
        container.add(info);
        setWidget(container);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        hide();
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
