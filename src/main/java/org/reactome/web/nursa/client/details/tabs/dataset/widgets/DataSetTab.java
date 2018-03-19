package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.web.nursa.client.common.handlers.DataSetSelectedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedHandler;
import org.reactome.web.nursa.client.details.tabs.dataset.PathwayLoader;
import org.reactome.web.pwp.client.details.tabs.DetailsTab;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.handlers.PathwayHoveredResetHandler;
import org.reactome.nursa.model.DataSet;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface DataSetTab {

    interface Presenter extends DetailsTab.Presenter, DataSetSelectedHandler,
            GseaCompletedHandler, BinomialCompletedHandler, PathwayLoader,
            PathwayHoveredResetHandler {
    }

    interface Display extends DetailsTab.Display<Presenter> {
        void showLoading(String doi);
        void showDetails(DataSet dataset);
    }
}
