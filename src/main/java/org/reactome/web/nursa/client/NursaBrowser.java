package org.reactome.web.nursa.client;

import org.fusesource.restygwt.client.Defaults;
import org.reactome.web.pwp.client.AppController;
import org.reactome.web.pwp.client.Browser;

public class NursaBrowser extends Browser {

    // Initialize the resty context root. This ensures that requests
    // go to /<module>/... rather than /Browser/<module>/...
    static {
        Defaults.setServiceRoot("/");
    }

    @Override
    protected AppController createController() {
        return new NursaAppController();
    }

}
