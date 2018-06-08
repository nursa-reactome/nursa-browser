package org.reactome.web.nursa.client.search;

import java.io.IOException;
import java.util.function.Consumer;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.reactome.nursa.model.DataSet;
import org.reactome.web.nursa.client.tools.dataset.NursaClient;

import com.google.gwt.core.shared.GWT;

public class DataSetLoader {

    static public void getDataset(String doi, Consumer<DataSet> consumer) {
        NursaClient client = GWT.create(NursaClient.class);
        client.getDataset(doi, new MethodCallback<DataSet>() {
            
            @Override
            public void onSuccess(Method method, DataSet dataset) {
                consumer.accept(dataset);
            }
            
            @Override
            public void onFailure(Method method, Throwable exception) {
                try {
                    String message = "Dataset " + doi + " was not retrieved";
                    throw new IOException(message, exception);
                } catch (IOException e) {
                    // TODO - how are I/O errors handled in Reactome?
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
