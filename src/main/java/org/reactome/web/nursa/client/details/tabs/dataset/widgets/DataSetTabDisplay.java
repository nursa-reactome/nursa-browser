package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

import org.reactome.web.pwp.client.common.CommonImages;
import org.reactome.web.pwp.client.details.tabs.DetailsTabTitle;
import org.reactome.web.pwp.client.details.tabs.DetailsTabType;
import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetPanel;
import org.reactome.nursa.model.DataSet;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class DataSetTabDisplay extends ResizeComposite implements DataSetTab.Display {

    @SuppressWarnings("unused")
    private DataSetTab.Presenter presenter;

    private DockLayoutPanel container;
    private DetailsTabTitle title;
    private DataSetPanel content;

    public DataSetTabDisplay() {
        this.title = getDetailTabType().getTitle();
        this.container = new DockLayoutPanel(Style.Unit.EM);
        initWidget(this.container);
        setInitialState();
    }

    @Override
    public DetailsTabType getDetailTabType() {
        return DetailsTabType.DATASET;
    }

    @Override
    public Widget getTitleContainer() {
        return this.title;
    }

    @Override
    public void setPresenter(DataSetTab.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetails(DataSet dataset) {
        this.content = new DataSetPanel(dataset);
        this.container.clear();
        this.container.add(this.content);
    }

    @Override
    public void showLoading(String doi) {
        setTitle(doi);
        showLoadingMessage();
    }

    @Override
    public void setInitialState() {
        this.container.clear();
        this.container.add(getDetailTabType().getInitialStatePanel());
    }

    @Override
    public void showLoadingMessage() {
        HorizontalPanel message = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.loader());
        message.add(loader);

        Label label = new Label("Loading the dataset. Please wait...");
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        message.add(label);

        this.container.clear();
        this.container.add(message);
    }

    @Override
    public void showErrorMessage(String message) {
        HorizontalPanel panel = new HorizontalPanel();
        Image loader = new Image(CommonImages.INSTANCE.exclamation());
        panel.add(loader);

        Label label = new Label(message);
        label.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        panel.add(label);

        this.container.clear();
        this.container.add(panel);
    }

    @Override
    public void setTitle(String doi){
        this.title.setTitle(doi);
    }
}
