package org.reactome.web.nursa.client.search;

import java.util.List;
import java.util.function.Consumer;

import org.reactome.nursa.model.Experiment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExperimentSelector extends VerticalPanel {

    private static final int MAX_EXP_ITEMS = 8;
    
    public ExperimentSelector(List<Experiment> experiments, Consumer<Integer> consumer) {
        addStyleName(RESOURCES.getCSS().main());
        
        // The list box title.
        Widget title = new HTML("Select experiment:");
        title.addStyleName(RESOURCES.getCSS().title());
        add(title);
       
        // The experiment list.
        ListBox lb = new ListBox();
        for (Experiment exp: experiments) {
            lb.addItem(exp.getName());
        }
        lb.setVisibleItemCount(Math.min(experiments.size(), MAX_EXP_ITEMS));
        // The default selection is the first experiment.
        lb.setSelectedIndex(0);
        // Double click accepts the experiment.
        lb.addDoubleClickHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                int expNbr = lb.getSelectedIndex() + 1;
                consumer.accept(expNbr);
            }
        
        });
        
        add(lb);
        
        // The Accept button.
        Button accept = new Button("Accept");
        accept.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                consumer.accept(lb.getSelectedIndex());
            }

        });
        accept.addStyleName(RESOURCES.getCSS().accept());
        // A bottom panel for button right alignment.
        ComplexPanel bottom = new FlowPanel();
        bottom.addStyleName(RESOURCES.getCSS().bottom());
        bottom.add(accept);
        add(bottom);
    }

    public static Resources RESOURCES;

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
        String CSS = "ExperimentSelector.css";

        String main();

        String title();

        String bottom();

        String accept();

    }

}
