package org.reactome.web.nursa.fireworks.legends;

import org.reactome.web.fireworks.events.AnalysisPerformedEvent;
import org.reactome.web.fireworks.legends.EnrichmentLegend;
import org.reactome.web.nursa.analysis.client.model.ComparisonExpressionSummary;
import org.reactome.web.nursa.model.Comparison;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NursaEnrichmentLegend extends EnrichmentLegend {

    /** The comparison analysis labels. */
    private static final String[] DISCRETE_LABELS = {
            Comparison.LABELS[0],
            ComparisonExpressionSummary.SHARED_LABEL,
            Comparison.LABELS[1],
    };

    /** The comparison analysis colors. */
    private static final String[] DISCRETE_COLOURS = {
            ComparisonExpressionSummary.UNSHARED_COLOURS[0],
            ComparisonExpressionSummary.SHARED_COLOUR,
            ComparisonExpressionSummary.UNSHARED_COLOURS[1],
    };
    
    /**
     * The discrete panel replaces the gradient widget for
     * comparison analysis.
     */
    private Panel discrete;

    public NursaEnrichmentLegend(EventBus eventBus) {
        super(eventBus);
        // The parent widget size and position is hard-coded
        // in fireworks with absolute formatting.
        // For an unknown reason, fireworks erases right and
        // bottom margins of the enrichment legend by an
        // absolute location that offsets the margin. This
        // might be a GWT artifact or in the fireworks code.
        // In any case, the legend widget is placed in an
        // otherwise superfluous body panel whose sole
        // purpose is to circumvent the heavy-handed parent
        // formatting. However, this body margin is inexplicably
        // modified to set a right margin of -5 pixels.
        // The discrete panel height is set to 100%
        // in the CSS without margins. The legend widget
        // proper is then added with appropriate margins set
        // in the CSS.
        discrete = new SimplePanel();
        discrete.addStyleName(RESOURCES.getCSS().main());
        Panel body = new VerticalPanel();
        body.addStyleName(RESOURCES.getCSS().body());
        fillDiscrete(body);
        discrete.add(body);
        add(discrete, 0, 0);
    }
    
    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        if (e.getExpressionSummary() instanceof ComparisonExpressionSummary) {
            this.discrete.setVisible(true);
            this.gradient.setVisible(false);
            for (Label label: gradientLabels) {
                label.setVisible(false);
            }
        } else {
            this.discrete.setVisible(false);
            this.gradient.setVisible(true);
            for (Label label: gradientLabels) {
                label.setVisible(true);
            }
        }
        super.onAnalysisPerformed(e);
    }

    private void fillDiscrete(Panel container) {
        for (int i=0; i < DISCRETE_LABELS.length; i++) {
            Panel panel = new SimplePanel();
            panel.addStyleName(RESOURCES.getCSS().discretePanel());
            panel.getElement().getStyle().setBackgroundColor(DISCRETE_COLOURS[i]);
            panel.getElement().getStyle().setColor(DISCRETE_COLOURS[i]);
            Label label = new Label(DISCRETE_LABELS[i]);
            label.addStyleName(RESOURCES.getCSS().discreteLabel());
            panel.add(label);
            container.add(panel);
        }
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
        String CSS = "NursaEnrichmentLegend.css";

        String main();

        String body();

        String discretePanel();

        String discreteLabel();

    }
}
