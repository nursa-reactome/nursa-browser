package org.reactome.web.nursa.client.tools.dataset;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.DataSetSearchResult;


@Path("/Nursa")
/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface NursaClient extends RestService {
    @GET
    @Path("/dataset")
    public void getDataset(@QueryParam("doi") String doi, MethodCallback<DataSet> dataset);

    @GET
    @Path("/search")
    public void search(@QueryParam("term") String term,
                       @QueryParam("start") Integer start,
                       @QueryParam("size") Integer size,
                       MethodCallback<DataSetSearchResult> searchResult);
}
