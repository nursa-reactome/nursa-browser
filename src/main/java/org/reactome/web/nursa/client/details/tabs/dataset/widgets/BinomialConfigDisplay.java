package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

public class BinomialConfigDisplay extends Composite {

    private static final double DEF_BINOMIAL_CUTOFF = .05;
    
    static final Binder uiBinder = GWT.create(Binder.class);
    
    /** The main panel. */
    @UiField()
    Panel content;

    /** The binomial p-value cut-off. */
    @UiField()
    DoubleBox pValueCutoff;

    public BinomialConfigDisplay() {
       // TODO - add and/or FDR <= and/or FDR >=.
        initWidget(uiBinder.createAndBindUi(this));
        content.addStyleName(RESOURCES.getCSS().main());
        pValueCutoff.addStyleName(RESOURCES.getCSS().pValueCutoff());

        // Initialize the binomial cut-off.
        pValueCutoff.setValue(DEF_BINOMIAL_CUTOFF);
    }
    
    public Double getPValueCutoff() {
        return pValueCutoff.getValue();
    }

    /**
     * The UiBinder interface.
     */
    interface Binder extends UiBinder<Widget, BinomialConfigDisplay> {
    }
    
    /** A ClientBundle of resources used by this widget. */
    interface Resources extends ClientBundle {
 
        /** The styles used in this widget. */
        @Source(Css.CSS)
        Css getCSS();

    }

    private static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /** Styles used by this widget. */
    interface Css extends CssResource {
 
        /** The path to the default CSS styles used by this resource. */
        String CSS = "BinomialConfigPanel.css";

        String main();

        String pValueCutoff();

    }

}
