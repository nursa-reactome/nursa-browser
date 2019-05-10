package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.user.client.ui.SimplePanel;

public class GseaConfigPanel extends SimplePanel {
 
    private GseaConfigSlider slider;

    public GseaConfigPanel() {
        // The config style must be defined in the global
        // style.css file since it uses the third-party slider
        // widget styles.
        addStyleName("gsea-config");
        // Make the slider.
        slider = new GseaConfigSlider();
        // Dip into the DOM to add the third-party slider.
        getElement().appendChild(slider.getElement());
    }

    public int[] getDataSetSizeBounds() {
        return slider.getValues();
    }
 
}
