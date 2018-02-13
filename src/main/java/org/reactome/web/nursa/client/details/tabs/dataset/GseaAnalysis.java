package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.AnalysisResult;

public interface GseaAnalysis {

    interface Presenter extends DataSetAnalysis.Presenter {
    }

    interface Display extends DataSetAnalysis.Display {
        void showResult(List<AnalysisResult> result);
    }

}
