package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public abstract class NursaPathwaySelectedEvent<K>
extends GwtEvent<NursaPathwaySelectedHandler> {

    private final K key;

    public NursaPathwaySelectedEvent(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }
}