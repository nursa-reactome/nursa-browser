package org.reactome.web.nursa.client.search;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.nursa.model.DataPoint;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;

import com.google.gwt.core.shared.GWT;

public class DataPointsLoader {

    static public void getDataPoints(String doi, int expNbr, Consumer<List<DataPoint>> consumer) {
        NursaClient client = GWT.create(NursaClient.class);
        client.getDataPoints(doi, expNbr, new MethodCallback<List<DataPoint>>() {
            
            @Override
            public void onSuccess(Method method, List<DataPoint> dataPoints) {
                consumer.accept(dataPoints);
            }
            
            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    String message = "Dataset " + doi + " Experiment " + expNbr + " was not retrieved";
                    throw new IOException(message, exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }
            
        });
    }

}
