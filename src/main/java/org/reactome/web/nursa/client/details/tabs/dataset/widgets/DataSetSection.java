package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSection extends Composite {

    private VerticalPanel content;

    public DataSetSection() {
        this.content = new VerticalPanel();
        initWidget(this.content);
        // The Reactome OverviewRow style applies to any section header,
        // not just the Overview section header.
        addStyleName("elv-Details-OverviewRow");
        addStyleName(RESOURCES.getCSS().main());
    }

    public void add(String title, Widget widget){
        addTitle(title);
        addWidget(widget);
    }

    private void addTitle(String title){
        Label label = new Label(title);
        label.addStyleName(RESOURCES.getCSS().title());
        content.add(label);
    }

    private void addWidget(Widget widget){
        SimplePanel aux = new SimplePanel();
        aux.addStyleName(RESOURCES.getCSS().widget());
        aux.add(widget);
        content.add(aux);
    }

    static Resources RESOURCES;

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
        String CSS = "DataSetSection.css";

        String main();

        String widget();

        String title();

        String subtitle();

    }
}
