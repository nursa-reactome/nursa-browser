package org.reactome.web.nursa.client.tools.dataset;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.reactome.nursa.model.DataSet;
import org.reactome.nursa.model.DataSetSearchResult;
import org.reactome.nursa.model.DisplayableDataPoint;


@Path("/NursaService")
/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface NursaClient extends RestService {
    @GET
    @Path("/dataset")
    public void getDataset(@QueryParam("doi") String doi,
                           MethodCallback<DataSet> dataset);

    @GET
    @Path("/datapoints")
    public void getDataPoints(@QueryParam("doi") String doi,
                           @QueryParam("experimentId") int experimentId,
                           MethodCallback<List<DisplayableDataPoint>> dataset);

    @GET
    @Path("/search")
    public void search(@QueryParam("term") String term,
                       @QueryParam("start") Integer start,
                       @QueryParam("size") Integer size,
                       MethodCallback<DataSetSearchResult> searchResult);
}
