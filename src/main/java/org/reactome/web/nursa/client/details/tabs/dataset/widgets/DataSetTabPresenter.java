package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.client.common.events.DataSetSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaAnalysisCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectHoveredEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;
import org.reactome.web.pwp.client.common.events.ErrorMessageEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.DataSet;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetTabPresenter extends AbstractPresenter
        implements DataSetTab.Presenter {

    private DataSetTab.Display display;
    private DataSet dataset;
    protected Object selected;
    
    public DataSetTabPresenter(EventBus detailsEventBus, DataSetTab.Display display,
            EventBus dataSetEventBus) {
        super(detailsEventBus);
        dataSetEventBus.addHandler(DataSetSelectedEvent.TYPE, this);
        dataSetEventBus.addHandler(BinomialAnalysisCompletedEvent.TYPE, this);
        dataSetEventBus.addHandler(GseaAnalysisCompletedEvent.TYPE, this);
        dataSetEventBus.addHandler(NursaPathwayHoveredEvent.TYPE, this);
        dataSetEventBus.addHandler(PathwayHoveredResetEvent.TYPE, this);
        dataSetEventBus.addHandler(NursaPathwaySelectedEvent.TYPE, this);
        this.display = display;
        this.display.setPresenter(this);
    }
    
    private EventBus getDetailsEventBus() {
        return eventBus;
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        // This body is borrowed from AnalysisTabPresenter.
        State state = event.getState();
        DatabaseObject selected = state.getSelected();
        this.selected = (selected instanceof Pathway) ? (Pathway) selected : state.getPathway();
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
        getDetailsEventBus().fireEventFromSource(tabChangedEvent, this);
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
        // Relay the analysis result to the StateManager,
        // which will in turn apply the analysis overlay.
        getDetailsEventBus().fireEventFromSource(new AnalysisCompletedEvent(result), this);
    }

    @Override
    public void onAnalysisCompleted(List<GseaAnalysisResult> result) {
        // Transform the result to a Reactome data structure.
        BinomialAnalysisResult binomial = new BinomialAnalysisResult(result);
        // Borrow the binomial handler to propagate a completed event on
        // the details event bus.
        onAnalysisCompleted(binomial);
    }
    
    @Override
    public void onPathwayHovered(final String stId) {
        final Function<Pathway, GwtEvent<?>> eventFactory =
                pathway -> new DatabaseObjectHoveredEvent(pathway);
        load(stId, eventFactory);
    }

    @Override
    public void onPathwaySelected(String stId) {
        final Function<Pathway, GwtEvent<?>> eventFactory =
                pathway -> new DatabaseObjectSelectedEvent(new Selection(pathway));
        load(stId, eventFactory);
    }

    private void load(final String stId, final Function<Pathway, GwtEvent<?>> eventFactory) {
        // The query below is adapted from AnalysisTabPresenter. Unlike
        // AnalysisTabPresenter, we use the stable id rather than the
        // database id, since both the binomial and the GSEA analysis
        // results have a stable id, but only the binomial result has a
        // database id. Note that the global LRU object cache stores an
        // object value for both id keys.
       ContentClient.query(stId, new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if (!Objects.equals(pathway, DataSetTabPresenter.this.selected)) {
                    // TODO - address the following Reactome bug:
                    // * hovering over a top-level pathway,
                    //   then hovering over a pathway in a different
                    //   hierarchy does not clear the top-level
                    //   pathway highight. Similarly, hovering over
                    //   a different top-level pathway does not clear
                    //   the non-top-level highlighted pathway.
                    //   The code commented out below does not fix this
                    //   bug and has no effect.
                    //if (DataSetTabPresenter.this.selected != null) {
                    //    // Clear the previous hover, if any.
                    //    DatabaseObjectHoveredEvent event = new DatabaseObjectHoveredEvent();
                    //    getDetailsEventBus().fireEventFromSource(event, DataSetTabPresenter.this);
                    //}
                    // Highlight the hovered pathway.
                    GwtEvent<?> event = eventFactory.apply(pathway);
                    getDetailsEventBus().fireEventFromSource(event, DataSetTabPresenter.this);
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                onFetchError(stId);
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                onFetchError(stId);
            }

            private void onFetchError(String stId) {
                ErrorMessageEvent msgEvent = new ErrorMessageEvent("Error retrieving data for " + stId);
                getDetailsEventBus().fireEventFromSource(msgEvent, DataSetTabPresenter.this);
            }
        });
    }

    @Override
    public void onPathwayHoveredReset(PathwayHoveredResetEvent event) {
        // An empty hover event clears any current highlighting.
        DatabaseObjectHoveredEvent hoverEvent = new DatabaseObjectHoveredEvent();
        getDetailsEventBus().fireEventFromSource(hoverEvent, this);
    }
}
