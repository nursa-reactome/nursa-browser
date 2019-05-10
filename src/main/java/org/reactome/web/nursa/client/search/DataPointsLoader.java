package org.reactome.web.nursa.client.search;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.nursa.model.DataPoint;
import org.reactome.nursa.model.DisplayableDataPoint;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;

import com.google.gwt.core.shared.GWT;

public class DataPointsLoader {

    static public void getDataPoints(String doi, int expId, Consumer<List<DisplayableDataPoint>> consumer) {
        NursaClient client = GWT.create(NursaClient.class);
        client.getDataPoints(doi, expId, new MethodCallback<List<DisplayableDataPoint>>() {
            
            @Override
            public void onSuccess(Method method, List<DisplayableDataPoint> dataPoints) {
                consumer.accept(dataPoints);
            }
            
            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    String message = "Dataset " + doi + " Experiment " + expId + " was not retrieved";
                    throw new IOException(message, exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }
            
        });
    }

}
