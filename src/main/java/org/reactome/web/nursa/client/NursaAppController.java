package org.reactome.web.nursa.client;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTabPresenter;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelector;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelectorDisplay;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelectorPresenter;
import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTab;
import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTabDisplay;
import org.reactome.web.pwp.client.AppController;
import org.reactome.web.pwp.client.details.tabs.description.DescriptionTab;
import org.reactome.web.pwp.client.details.tabs.downloads.DownloadsTab;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTab;
import org.reactome.web.pwp.client.tools.launcher.ToolLauncher;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class NursaAppController extends AppController {

    private EventBus dataSetEventBus;

    private EventBus getDataSetEventBus() {
        // Create the event bus on demand. Since the protected 
        // methods below are called from the superclass constructor,
        // the event bus can't be created in a constructor for
        // this class, which would otherwise be preferable.
        if (dataSetEventBus == null) {
            dataSetEventBus = new SimpleEventBus();
        }
        return dataSetEventBus;
    }

    @Override
    protected ComplexPanel getTopPanel() {
        ComplexPanel panel = super.getTopPanel();
        // Remove the analysis button.
        for (Widget child: panel) {
            if (child instanceof ToolLauncher.Display) {
                panel.remove(child);
                break;
            }
        }
        
        // Add the dataset button.
        DataSetSelector.Display datasetDisplay = new DataSetSelectorDisplay();
        new DataSetSelectorPresenter(getDataSetEventBus(), datasetDisplay);
        panel.add(datasetDisplay);
        
        return panel;
    }

    @Override
    protected void initialiseDetailsTabsList() {
        super.initialiseDetailsTabsList();

        // Make the dataset tab.
        DataSetTab.Display datasetTab = new DataSetTabDisplay();
        new DataSetTabPresenter(this.eventBus, datasetTab, getDataSetEventBus());
        // Remove extraneous tabs.
        DETAILS_TABS.removeIf(tab -> !(tab instanceof DescriptionTab.Display ||
                                       tab instanceof MoleculesTab.Display ||
                                       tab instanceof DownloadsTab.Display));
        // Insert the dataset tab before the downloads tab.
        OptionalInt downloadsNdx =
                IntStream.range(0, DETAILS_TABS.size())
                         .filter(i -> DETAILS_TABS.get(i) instanceof DownloadsTab.Display)
                         .findAny();
        if (downloadsNdx == null) {
            throw new NoSuchElementException("Downloads tab not found");
        }
        DETAILS_TABS.add(downloadsNdx.getAsInt(), datasetTab);
    }

}
