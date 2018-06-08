package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaComparisonCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.PathwayLoader;
import org.reactome.web.nursa.client.search.ExperimentSelectedHandler;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredResetHandler;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface DataSetTab {

    interface Presenter extends DetailsTab.Presenter, ExperimentSelectedHandler,
    GseaCompletedHandler, GseaComparisonCompletedHandler, BinomialCompletedHandler,
    PathwayLoader, PathwayHoveredResetHandler {
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showDetails(DataSet dataset, Experiment experiment);
    }
}
