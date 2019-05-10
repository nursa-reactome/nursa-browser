package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.nursa.analysis.client.model.PseudoAnalysisResult;
import org.reactome.web.nursa.analysis.client.model.TokenGenerator;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonAnalysisCompletedEvent;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaCompletedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaSelectedEvent;
import org.reactome.web.nursa.client.search.DataPointsLoadedEvent;
import org.reactome.web.pwp.client.common.Selection;
import org.reactome.web.pwp.client.common.events.AnalysisCompletedEvent;
import org.reactome.web.pwp.client.common.events.AnalysisResetEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectHoveredEvent;
import org.reactome.web.pwp.client.common.events.DatabaseObjectSelectedEvent;
import org.reactome.web.pwp.client.common.events.DetailsTabChangedEvent;
import org.reactome.web.pwp.client.common.events.StateChangedEvent;
import org.reactome.web.pwp.client.common.module.AbstractPresenter;
import org.reactome.web.pwp.client.details.tabs.analysis.widgets.results.events.PathwayHoveredResetEvent;
import org.reactome.web.pwp.client.manager.state.State;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.common.ContentClientHandler.ObjectLoaded;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;
import org.reactome.web.pwp.model.client.util.Path;
import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.Experiment;

import com.google.gwt.event.shared.EventBus;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetTabPresenter extends AbstractPresenter
        implements DataSetTab.Presenter {

    private DataSetTab.Display display;
    protected Object selected;
    private DataSet dataset;
    private Experiment experiment;
    
    public DataSetTabPresenter(EventBus detailsEventBus, DataSetTab.Display display,
            EventBus dataSetEventBus) {
        super(detailsEventBus);
        dataSetEventBus.addHandler(DataPointsLoadedEvent.TYPE, this);
        dataSetEventBus.addHandler(BinomialCompletedEvent.TYPE, this);
        dataSetEventBus.addHandler(GseaCompletedEvent.TYPE, this);
        dataSetEventBus.addHandler(ComparisonAnalysisCompletedEvent.TYPE, this);
//        dataSetEventBus.addHandler(BinomialHoveredEvent.TYPE, this);
//        dataSetEventBus.addHandler(GseaHoveredEvent.TYPE, this);
        dataSetEventBus.addHandler(PathwayHoveredResetEvent.TYPE, this);
        dataSetEventBus.addHandler(BinomialSelectedEvent.TYPE, this);
        dataSetEventBus.addHandler(GseaSelectedEvent.TYPE, this);
        dataSetEventBus.addHandler(AnalysisResetEvent.TYPE, this);
        this.display = display;
        this.display.setPresenter(this);
    }
    
    private EventBus getDetailsEventBus() {
        return eventBus;
    }

    @Override
    public void onStateChanged(StateChangedEvent event) {
        // This method body is adapted from AnalysisTabPresenter.
        // The motivation and effect of this snippet are unclear,
        // but it makes the Reactome state/tab framework happy.
        State state = event.getState();
        
        DatabaseObject selected = state.getSelected();
        if (selected instanceof Pathway) {
            this.selected = selected;
        } else {
            this.selected = state.getPathway();
        }
    }

    @Override
    public void onExperimentLoaded(DataPointsLoadedEvent event) {
        this.dataset = event.getDataSet();
        this.experiment = event.getExperiment();
        // If there is a current analysis, then clear it.
        eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
        // Display the experiment.
        display.showDetails(event);
        // Notify the state manager that this tab wants the focus.
        DetailsTabChangedEvent tabChangedEvent =
                new DetailsTabChangedEvent(display.getDetailTabType());
        getDetailsEventBus().fireEventFromSource(tabChangedEvent, this);
    }

    @Override
    public void onAnalysisCompleted(AnalysisResult result) {
        // Work around the bug described in onAnalysisReset()
        // by firing the reset event here to clear the previous
        // overlay, if any.
        AnalysisResetEvent resetEvent = new AnalysisResetEvent();
        getDetailsEventBus().fireEventFromSource(resetEvent, this);
        // Relay the analysis result to the StateManager,
        // which will in turn apply the analysis overlay.
        AnalysisCompletedEvent event = new AnalysisCompletedEvent(result);
        getDetailsEventBus().fireEventFromSource(event, this);
    }

    @Override
    public void onAnalysisCompleted(List<GseaAnalysisResult> result) {
        String token = generateGseaToken();
        // Transform the result to a Reactome data structure.
        PseudoAnalysisResult pseudo = new PseudoAnalysisResult(result, token);
        // Borrow the binomial handler to propagate a completed event on
        // the details event bus.
        onAnalysisCompleted(pseudo);
    }

    @Override
    public void onAnalysisCompleted(Comparison comparison,
            ComparisonPartition<?> partition) {
        String token = generateComparisonToken(comparison);
        // Transform the result to a Reactome data structure.
        PseudoAnalysisResult pseudo =
                new PseudoAnalysisResult(comparison, partition, token);
        // Borrow the basic handler to propagate a completed event on
        // the details event bus.
        onAnalysisCompleted(pseudo);
    }

    @Override
    public void onPathwaySelected(Long dbId) {
        ObjectLoaded<DatabaseObject> handler = createLoadHandler();
        ContentClient.query(dbId, handler);
    }

    @Override
    public void onPathwaySelected(String stId) {
        ObjectLoaded<DatabaseObject> handler = createLoadHandler();
        ContentClient.query(stId, handler);
    }

    private ObjectLoaded<DatabaseObject> createLoadHandler() {
        return new ContentClientHandler.ObjectLoaded<DatabaseObject>() {

            @Override
            public void onObjectLoaded(DatabaseObject databaseObject) {
                Pathway pathway = (Pathway) databaseObject;
                if (!Objects.equals(pathway, selected)) {
                    DatabaseObjectSelectedEvent event =
                            new DatabaseObjectSelectedEvent(new Selection(pathway, new Path()));
                    getDetailsEventBus().fireEventFromSource(event, DataSetTabPresenter.this);
                }
            }

            @Override
            public void onContentClientException(Type type, String message) {
                // TODO
            }

            @Override
            public void onContentClientError(ContentClientError error) {
                // TODO
            }
       };
    }

//    private ObjectLoaded<DatabaseObject> createLoadHandler(final Object key) {
//        return new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
//                @Override
//                public void onObjectLoaded(DatabaseObject databaseObject) {
//                    Pathway pathway = (Pathway) databaseObject;
//                    if (!Objects.equals(pathway, DataSetTabPresenter.this.selected)) {
//                        // TODO - address the following Reactome bug:
//                        // * Hovering over a top-level pathway, then hovering
//                        //   over a pathway in a different hierarchy does not
//                        //   clear the top-level pathway highight. Similarly,
//                        //   hovering over a different top-level pathway does
//                        //   not clear the non-top-level highlighted pathway.
//                        //   The following code fragment does not fix this bug
//                        //   and has no discernable effect:
//                        //
//                        //      if (DataSetTabPresenter.this.selected != null) {
//                        //          // Clear the previous hover, if any.
//                        //          DatabaseObjectHoveredEvent event =
//                        //                  new DatabaseObjectHoveredEvent();
//                        //          getDetailsEventBus().fireEventFromSource(
//                        //                  event, DataSetTabPresenter.this
//                        //          );
//                        //      }
//                        
//                        DatabaseObjectSelectedEvent event =
//                                new DatabaseObjectSelectedEvent(new Selection(pathway, new Path()));
//                        getDetailsEventBus().fireEventFromSource(event, DataSetTabPresenter.this);
//                    }
//                }
//
//                @Override
//                public void onContentClientException(Type type, String message) {
//                    onFetchError(key);
//                }
//
//                @Override
//                public void onContentClientError(ContentClientError error) {
//                    onFetchError(key);
//                }
//
//                private void onFetchError(Object key) {
//                    ErrorMessageEvent msgEvent = new ErrorMessageEvent("Error retrieving data for " + key);
//                    getDetailsEventBus().fireEventFromSource(msgEvent, DataSetTabPresenter.this);
//                }
//            };
//    }

    @Override
    public void onPathwayHoveredReset(PathwayHoveredResetEvent event) {
        // An empty hover event clears any current highlighting.
        DatabaseObjectHoveredEvent hoverEvent = new DatabaseObjectHoveredEvent();
        getDetailsEventBus().fireEventFromSource(hoverEvent, this);
    }

    private String generateGseaToken() {
        return TokenGenerator.create(dataset.getName() + experiment.getName() + "GSEA");
    }

    private String generateComparisonToken(Comparison comparison) {
        String compStr = generateComparisonString(comparison);
        return TokenGenerator.create(dataset.getName() + compStr + "Comparison");
    }

    private String generateComparisonString(Comparison comparison) {
        List<Experiment> exps = comparison.experiments();
        String expNames = exps.stream()
                .map(Experiment::getName)
                .collect(Collectors.joining());
        return dataset.getName() + expNames;
    }

    @Override
    public void onAnalysisReset() {
        // NOTE: Even though it is verified that the reset event fires
        // on the dataset event bus, this handler is never called.
        // The work-around is to fire the event when analysis completes.
        // Forward to the rest of the Reactome GUI.
        AnalysisResetEvent resetEvent = new AnalysisResetEvent();
        getDetailsEventBus().fireEventFromSource(resetEvent, this);
    }

}
