package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.reactome.gsea.model.GseaAnalysisResult;

@Path("/GseaService")
/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface GseaClient extends RestService {

    @POST
    @Path("/analyse")
    public void analyse(
            List<List<String>> rankedList,
            @QueryParam("dataSetSizeMin") Integer dataSetSizeMin,
            @QueryParam("dataSetSizeMax") Integer dataSetSizeMax,
            MethodCallback<List<GseaAnalysisResult>> result
    );

}
