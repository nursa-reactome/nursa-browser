package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import org.reactome.gsea.config.PreRanked;
import org.reactome.web.widgets.sliders.MinMaxSlider;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class GseaConfigSlider extends MinMaxSlider {
    private static int[] BOUNDS = {0, 500};
    private static int[] START = {PreRanked.DEF_MIN_DATASET_SIZE, PreRanked.DEF_MAX_DATASET_SIZE};

    public GseaConfigSlider() {
        super(BOUNDS, START);
    }

}
