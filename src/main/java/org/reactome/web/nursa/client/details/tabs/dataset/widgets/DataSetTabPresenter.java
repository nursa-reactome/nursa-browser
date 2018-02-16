package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.io.IOException;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.common.events.DataSetSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialAnalysisCompletedEvent;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.nursa.model.DataSet;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetTabPresenter extends AbstractPresenter
        implements DataSetTab.Presenter {

    private DataSetTab.Display display;
    private DataSet dataset;
    
    public DataSetTabPresenter(EventBus detailsEventBus, DataSetTab.Display display,
            EventBus dataSetEventBus) {
        super(detailsEventBus);
        dataSetEventBus.addHandler(DataSetSelectedEvent.TYPE, this);
        dataSetEventBus.addHandler(BinomialAnalysisCompletedEvent.TYPE, this);
        this.display = display;
        this.display.setPresenter(this);
    }
    
    private EventBus getDetailsEventBus() {
        return eventBus;
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        // Nothing special to do here.
    }

    @Override
    public void onDataSetSelected(DataSetSelectedEvent datasetSelectedEvent) {
        // A dataset was selected by the search dialog.
        // Note: we do not check whether the selected dataset
        // DOI is the same as the current dataset DOI. A loaded
        // dataset could have been reselected. In that case,
        // reload the dataset anyway since that is presumably
        // the user's intent.
        this.dataset = datasetSelectedEvent.getDataSet();
        loadDataset(this.dataset.getDoi());
        // Notify the state manager that this tab wants the focus.
        DetailsTabChangedEvent tabChangedEvent =
                new DetailsTabChangedEvent(display.getDetailTabType());
        eventBus.fireEventFromSource(tabChangedEvent, this);
    }

    public void loadDataset(String doi) {
        this.display.showLoading(doi);
        getDataset(doi);
    }

    private void getDataset(String doi) {
        NursaClient client = GWT.create(NursaClient.class);
        client.getDataset(doi, new MethodCallback<DataSet>() {
            
            @Override
            public void onSuccess(Method method, DataSet dataset) {
                // Guard against a loaded dataset which differs from
                // the loading dataset. This could occur in the rare
                // case of a second dataset being selected in the
                // search dialog while the first dataset is being
                // loaded. In that case, ignore the loaded dataset,
                // throwing away the result and continuing to wait
                // for the second selected dataset load to complete.
                if (DataSetTabPresenter.this.dataset.getDoi() == dataset.getDoi()) {
                    DataSetTabPresenter.this.dataset = dataset;
                    display.showDetails(dataset);
                }
            }
            
            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    throw new IOException("Dataset " + doi + " was not retrieved", exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onAnalysisCompleted(AnalysisResult result) {
        // Forward the analysis result to the StateManager,
        // which will in turn apply the analysis overlay.
        getDetailsEventBus().fireEventFromSource(new AnalysisCompletedEvent(result), this);
    }
}
