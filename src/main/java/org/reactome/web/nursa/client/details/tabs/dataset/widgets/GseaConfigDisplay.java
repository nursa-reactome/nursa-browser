package org.reactome.web.nursa.client.details.tabs.dataset.widgets;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GseaConfigDisplay extends Composite {
    
    private static final int DEF_NPERMS = 100;

    static final Binder uiBinder = GWT.create(Binder.class);

    /** The number of permutations. */
    @UiField()
    VerticalPanel container;

    /** The number of permutations. */
    @UiField()
    IntegerBox nperms;
 
    private GseaConfigSlider slider;

    public GseaConfigDisplay() {
        initWidget(uiBinder.createAndBindUi(this));
        setStyleName(RESOURCES.getCSS().main());
        // The config style must be defined in the global style.css
        // file since it uses the third-party slider widget styles.
        addStyleName("gsea-config");
        nperms.setValue(DEF_NPERMS);
        // Make the slider.
        slider = new GseaConfigSlider();
        // Dip into the DOM to add the third-party slider.
        container.getElement().appendChild(slider.getElement());
    }

    public Integer getNperms() {
        return nperms.getValue();
    }

    public int[] getDataSetSizeBounds() {
        return slider.getValues();
    }

    /**
     * The UiBinder interface.
     */
    interface Binder extends UiBinder<Widget, GseaConfigDisplay> {
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
        String CSS = "GseaConfigDisplay.css";

        String main();

    }
 
}
