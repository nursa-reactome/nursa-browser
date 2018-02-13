package org.reactome.web.nursa.client.toppanel.dataset;

import com.google.gwt.event.shared.EventBus;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSelectorPresenter implements DataSetSelector.Presenter {

    private DataSetSelector.Display display;
    private EventBus eventBus;

    public DataSetSelectorPresenter(EventBus eventBus, DataSetSelector.Display display) {
        this.eventBus = eventBus;
        this.display = display;
        this.display.setPresenter(this);
    }

    @Override
    public void search() {
        SearchDialog dialog = new SearchDialog(eventBus);
        dialog.show();
    }
}
