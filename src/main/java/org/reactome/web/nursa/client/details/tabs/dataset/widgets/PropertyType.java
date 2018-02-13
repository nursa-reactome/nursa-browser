package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public enum PropertyType {

    OVERVIEW("Overview"),
    COMPARE("Compare to Other DataSet"),
    GENE_LIST("Gene List"),
    PATHWAY("Pathway");
    
    private String title;

    PropertyType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
