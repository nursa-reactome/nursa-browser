package org.reactome.web.nursa.client.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.DataSetSearchResult;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;
import org.reactome.web.widgets.search.SearchParameters;
import org.reactome.web.widgets.search.SearchResult;
import org.reactome.web.widgets.search.Searcher;
import org.reactome.web.widgets.search.Suggestion;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetSearcher implements Searcher {
    // Proxy a dataset as a suggestion.
    static class DataSetAdapter implements Suggestion {
        private DataSet dataset;

        public DataSetAdapter(DataSet dataset) {
            this.dataset = dataset;
        }
        
        public DataSet getDataset() {
            return dataset;
        }

        @Override
        public String getKey() {
            return dataset.getDoi();
        }
        
        @Override
        public String getTitle() {
            return dataset.getName();
        }
        
        @Override
        public SafeUri getImage() {
            return RESOURCES.dataset().getSafeUri();
        }
        
        @Override
        public List<String> getSecondary() {
            String description = dataset.getDescription();
            if (description == null) {
                return Collections.emptyList();
            } else {
                return Arrays.asList(description, getKey());
            }
        }

    }

    // Proxy a DataSetSearchResult as a SearchResult.
    static class SearchResultAdapter implements SearchResult {
        private DataSetSearchResult dsResult;

        public SearchResultAdapter(DataSetSearchResult dsResult) {
            this.dsResult = dsResult;
        }

        @Override
        public Integer getNumFound() {
            return dsResult.getNumFound();
        }

        @Override
        public List<Suggestion> getEntries() {
            return dsResult.getDatasets().stream()
                    .map(ds -> new DataSetAdapter(ds))
                    .collect(Collectors.toList());
        }

    }
 
    @Override
    public void search(SearchParameters parameters, Consumer<SearchResult> consumer) {
        NursaClient client = GWT.create(NursaClient.class);
        String term = parameters.getTerm();
        // The search result handler.
        MethodCallback<DataSetSearchResult> callback = new MethodCallback<DataSetSearchResult>() {

            @Override
            public void onSuccess(Method method, DataSetSearchResult dsResult) {
                // Proxy the bean to the interface with an adapter.
                SearchResult searchResult = new SearchResultAdapter(dsResult);
                consumer.accept(searchResult);
            }

            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    String message = "Dataset search for " + term + " was not retrieved";
                    throw new IOException(message, exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }

        };
      
      client.search(term, parameters.getStart(), parameters.getSize(), callback);
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
    }

    interface Resources extends ClientBundle {
        @Source("images/dataset_icon.png")
        ImageResource dataset();
    }

}
