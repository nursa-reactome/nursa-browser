package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface DataSetAnalysis {

    interface Presenter extends EventHandler {
    }

    interface Display extends IsWidget {

        void setPresenter(Presenter presenter);

        void setInitialState();

        void showInProgressMessage();

        void showErrorMessage(String message);
   }

}
