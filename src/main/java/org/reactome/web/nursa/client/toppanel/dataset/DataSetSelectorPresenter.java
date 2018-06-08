package org.reactome.web.nursa.client.toppanel.dataset;

import org.reactome.web.nursa.client.search.SearchDialog;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSelectorPresenter implements DataSetSelector.Presenter {

    private static final String SEARCH_DIALOG_TITLE = "DataSet Search";

    // Place the dialog just below the top panel, which is roughly
    // 40 pixels in height.
    private static final int TOP_PANEL_HEIGHT = 42;

    private EventBus eventBus;

    public DataSetSelectorPresenter(EventBus eventBus, DataSetSelector.Display display) {
        this.eventBus = eventBus;
        display.setPresenter(this);
    }

    @Override
    public void search() {
        DialogBox dialog = new SearchDialog(eventBus, SEARCH_DIALOG_TITLE, TOP_PANEL_HEIGHT);
        dialog.show();
    }

}
