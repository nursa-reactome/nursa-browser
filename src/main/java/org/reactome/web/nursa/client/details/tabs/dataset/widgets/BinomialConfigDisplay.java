package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.reactome.nursa.model.DataPoint;
import org.reactome.web.nursa.client.details.tabs.dataset.ConfigFilter;
import org.reactome.web.nursa.client.details.tabs.dataset.ConfigFilter.Conjunction;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

public class BinomialConfigDisplay extends Composite {

    private static final double DEF_BINOMIAL_CUTOFF = .05;
    
    static final Binder uiBinder = GWT.create(Binder.class);

    /** The p-value cut-off. */
    @UiField()
    DoubleBox pValueCutoff;
    
    /** The p-value/FDR conjunction. */
    @UiField()
    ListBox pValueFcMinConjunction;
    
    /** The Fold Change minimum. */
    @UiField()
    DoubleBox fcMin;
    
    /** The Fold Change min/max conjunction. */
    @UiField()
    ListBox fcMinMaxConjunction;
    
    /** The Fold Change maximum. */
    @UiField()
    DoubleBox fcMax;

    public BinomialConfigDisplay() {
        // TODO - add and/or FDR <= and/or FDR >=.
        initWidget(uiBinder.createAndBindUi(this));
        setStyleName(RESOURCES.getCSS().main());

        // Initialize the binomial cut-off.
        pValueCutoff.setValue(DEF_BINOMIAL_CUTOFF);
    }

    /**
     * @return the pvalue and fold change range predicate to apply
     *      to data points
     */
    Predicate<DataPoint> getFilter() {
        Double[] cutoffs = {
                pValueCutoff.getValue(),
                fcMin.getValue(),
                fcMax.getValue()
        };
        
        // This folderol is necessary because Java doesn't support
        // generic predicate arrays.
        BiPredicate<Double, Double> le = (value, cutoff) -> value <= cutoff;
        BiPredicate<Double, Double> ge = (value, cutoff) -> value >= cutoff;
        List<BiPredicate<Double, Double>> comparators =
                new ArrayList<BiPredicate<Double,Double>>();
        // pvalue comparator
        comparators.add(le);
        // FC min comparator
        comparators.add(le);
        // FC max comparator
        comparators.add(ge);

        Conjunction[] conjunctions = {
                getConjunction(pValueFcMinConjunction),
                getConjunction(fcMinMaxConjunction)
        };
        ConfigFilter filter = new ConfigFilter(cutoffs, comparators, conjunctions);
        
        // The predicate calls the filter.
        return dp -> {
            double fc = dp.getFoldChange();
            return filter.test(dp.getPvalue(), fc, fc);
        };
    }
    
    public Double getPValueCutoff() {
        return pValueCutoff.getValue();
    }
    
    public Double getMinFoldChange() {
        return fcMin.getValue();
    }
    
    public Double getMaxFoldChange() {
        return fcMax.getValue();
    }
    
    public Conjunction getPValueFcMinConjunction() {
        return getConjunction(pValueFcMinConjunction);
    }

    public Conjunction getFcMinMaxConjunction() {
        return getConjunction(fcMinMaxConjunction);
    }
    
    public String formatFilter() {
        StringBuilder sb = new StringBuilder();
        Double pValue = getPValueCutoff();
        if (pValue != null) {
            sb.append("p-value <= ");
            sb.append(pValue);
        }
        Double fcMin = getMinFoldChange();
        if (fcMin != null) {
            if (sb.length() > 0) {
                sb.append(" ");
                sb.append(formatConjunction(getPValueFcMinConjunction()));
            }
            sb.append(" FC <= ");
            sb.append(fcMin);
        }
        Double fcMax = getMaxFoldChange();
        if (fcMax != null) {
            if (sb.length() > 0) {
                sb.append(" ");
                sb.append(formatConjunction(getFcMinMaxConjunction()));
            }
            sb.append(" FC >= ");
            sb.append(fcMax);
        }
        
        return sb.toString(); 
    }
    
    private Conjunction getConjunction(ListBox lb) {
        return lb.getSelectedValue() == "and" ? Conjunction.AND : Conjunction.OR;
    }
    
    private String formatConjunction(Conjunction conjunction) {
        switch (conjunction) {
        case AND:
            return "and";
        case OR:
            return "or";
        default:
            return null;
        }
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
        String CSS = "BinomialConfigDisplay.css";

        String main();

    }

}
