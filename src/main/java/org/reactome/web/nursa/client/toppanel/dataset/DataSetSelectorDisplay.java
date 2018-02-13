package org.reactome.web.nursa.client.toppanel.dataset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;

import org.reactome.web.pwp.client.toppanel.layout.LayoutButton;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSelectorDisplay extends Composite implements DataSetSelector.Display, ClickHandler {

    private DataSetSelector.Presenter presenter;

    public DataSetSelectorDisplay() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName(RESOURCES.getCSS().layoutPanel());
        flowPanel.add(new SimplePanel(new InlineLabel("DataSet:")));
        flowPanel.add(new LayoutButton("Selects the dataset...", RESOURCES.getCSS().search(), this));
        initWidget(flowPanel);
    }

    @Override
    public void setPresenter(DataSetSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(ClickEvent event) {
        this.presenter.search();
    }

    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("images/search_clicked.png")
        ImageResource searchClicked();

        @Source("images/search_hovered.png")
        ImageResource searchHovered();

        @Source("images/search_normal.png")
        ImageResource searchNormal();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("pwp-DataSetSelectorDisplay")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "DataSetSelectorDisplay.css";

        String layoutPanel();

        String search();
    }
}
