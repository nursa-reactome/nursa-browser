package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import org.reactome.web.diagram.common.PwpButton;
import org.reactome.web.nursa.client.search.SearchDialog.Css;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class AnalysisConfigDialog extends DialogBox implements ClickHandler {

    public AnalysisConfigDialog(Widget config, String title) {
        setAutoHideEnabled(false);
        setModal(true);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setAutoHideOnHistoryEventsEnabled(false);
        setStyleName(RESOURCES.getCSS().main());
        setTitlePanel(title);
        
        // The dialog content container panel.
        FlowPanel container = new FlowPanel();
        addCloseButton(container);
        container.add(config);
        setWidget(container);

        // The dialog position must be set programatically.
        // See the SearchDialog constructor setPopupPositionAndShow
        // call comment.
        setPopupPositionAndShow(new PositionCallback(){

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                // The old values are ignored. Place the pop-up in the
                // middle right third of the display area.
                int left = 2 * Window.getClientWidth() / 3;
                int top = Window.getClientHeight() / 2;
                setPopupPosition(left, top);
            }

        });      
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        hide();
    }
    
    private void addCloseButton(ComplexPanel container) {
        // The dialog accept button.
        PwpButton close = new PwpButton("Accept", RESOURCES.getCSS().close(), new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                AnalysisConfigDialog.this.hide();
            }
            
        });
        container.add(close);
    }
    
    private void setTitlePanel(String title) {
        Label label = new Label(title);
        label.setStyleName(RESOURCES.getCSS().headerText());
        SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant(label.toString());
        getCaption().setHTML(safeHtml);
        getCaption().asWidget().setStyleName(RESOURCES.getCSS().header());
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {

        @Source(Css.CSS)
        Css getCSS();

        @Source("images/minihelp_normal.png")
        ImageResource info();

        @Source("images/close_normal.png")
        ImageResource closeNormal();

        @Source("images/close_hovered.png")
        ImageResource closeHovered();

        @Source("images/close_clicked.png")
        ImageResource closeClicked();

    }

    // TODO - refactor the common CSS and images for
    // AnalysisConfigDialog and the SearchDialog into
    // a new widgets DialogResources interface.
    public interface Css extends CssResource {

        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "AnalysisConfigDialog.css";

        String main();

        String info();

        String header();

        String headerText();

        String close();

    }

}
