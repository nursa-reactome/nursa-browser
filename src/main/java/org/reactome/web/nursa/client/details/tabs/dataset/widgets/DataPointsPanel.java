package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;
import org.reactome.web.nursa.client.common.events.ExperimentSelectedEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataPointsPanel extends StackPanel {

    private EventBus eventBus;

    public DataPointsPanel(DataSet dataset, EventBus eventBus) {
        this.eventBus = eventBus;
        for (Experiment exp: dataset.getExperiments()) {
            Widget table = new DataPointTable(exp);
            add(table, exp.getName());
        }
    }
    
    @Override
    public void insert(Widget w, int beforeIndex) {
        if (!(w instanceof DataPointTable)) {
            // Constrains children to be data points tables.
            // This is arguably better than maintaining redundant
            // state to support the showStack event propagation
            // in the highly unlikely case that a different child
            // is added to this panel.
            throw new UnsupportedOperationException(
                    "Only data point tables can be added to the data points panel"
            );
        }
        super.insert(w, beforeIndex);
    }

    public void showStack(int index) {
        boolean changed = index != getSelectedIndex();
        super.showStack(index);
        if (changed) {
            // The insert override guarantees that children are data
            // point tables.
            DataPointTable table = (DataPointTable) getChildren().get(index);
            ExperimentSelectedEvent event =
                    new ExperimentSelectedEvent(table.experiment);
            eventBus.fireEventFromSource(event, this);
        }
    }

}
