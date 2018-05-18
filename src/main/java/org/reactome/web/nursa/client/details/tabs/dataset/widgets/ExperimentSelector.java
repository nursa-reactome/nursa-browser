package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.reactome.nursa.model.Experiment;

public class ExperimentSelector extends VerticalPanel {
    
    ListBox listBox;

    public ExperimentSelector(List<Experiment> experiments) {
        addStyleName(RESOURCES.getCSS().main());
        HTML title = new HTML("Experiment");
        title.addStyleName(RESOURCES.getCSS().title());
        add(title);
        listBox = new ListBox();
        for (Experiment exp: experiments) {
            listBox.addItem(exp.getName());
        }
        add(listBox);
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
        String CSS = "ExperimentSelector.css";

        String main();

        String title();

    }
}
