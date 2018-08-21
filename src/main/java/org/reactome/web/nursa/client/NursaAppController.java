package org.reactome.web.nursa.client;

import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTabPresenter;
import org.reactome.web.nursa.client.hierarchy.NursaHierarchyDisplay;
import org.reactome.web.nursa.client.manager.state.NursaStateManager;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelector;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelectorDisplay;
import org.reactome.web.nursa.client.toppanel.dataset.DataSetSelectorPresenter;
import org.reactome.web.nursa.client.viewport.fireworks.NursaFireworksDisplay;
import org.reactome.web.nursa.client.viewport.fireworks.NursaFireworksPresenter;
import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTab;
import org.reactome.web.nursa.client.details.tabs.dataset.widgets.DataSetTabDisplay;
import org.reactome.web.pwp.client.AppController;
import org.reactome.web.pwp.client.details.tabs.description.DescriptionTab;
import org.reactome.web.pwp.client.details.tabs.description.DescriptionTabDisplay;
import org.reactome.web.pwp.client.details.tabs.description.DescriptionTabPresenter;
import org.reactome.web.pwp.client.details.tabs.downloads.DownloadsTab;
import org.reactome.web.pwp.client.details.tabs.downloads.DownloadsTabDisplay;
import org.reactome.web.pwp.client.details.tabs.downloads.DownloadsTabPresenter;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTab;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTabDisplay;
import org.reactome.web.pwp.client.details.tabs.molecules.MoleculesTabPresenter;
import org.reactome.web.pwp.client.details.tabs.structures.StructuresTab;
import org.reactome.web.pwp.client.details.tabs.structures.StructuresTabDisplay;
import org.reactome.web.pwp.client.details.tabs.structures.StructuresTabPresenter;
import org.reactome.web.pwp.client.hierarchy.HierarchyDisplay;
import org.reactome.web.pwp.client.tools.launcher.ToolLauncher;
import org.reactome.web.pwp.client.viewport.fireworks.Fireworks.Display;
import org.reactome.web.pwp.client.viewport.fireworks.FireworksDisplay;
import org.reactome.web.pwp.client.viewport.fireworks.FireworksPresenter;

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
    protected void initStateManager(){
        new NursaStateManager(this.eventBus);
    }

    @Override
    protected HierarchyDisplay createHierarchyDisplay() {
        return new NursaHierarchyDisplay();
    }

    @Override
    protected FireworksPresenter createFireworksPresenter(Display fireworks) {
        return new NursaFireworksPresenter(this.eventBus, fireworks);
    }

    @Override
    protected FireworksDisplay getFireworksDisplay() {
        return new NursaFireworksDisplay();
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
        // Cull the superclass tabs and add the dataset tab
        // before the downloads tab.
        DescriptionTab.Display description = new DescriptionTabDisplay();
        new DescriptionTabPresenter(this.eventBus, description);
        DETAILS_TABS.add(description);

        MoleculesTab.Display molecules = new MoleculesTabDisplay();
        new MoleculesTabPresenter(this.eventBus, molecules);
        DETAILS_TABS.add(molecules);

        StructuresTab.Display structures = new StructuresTabDisplay();
        new StructuresTabPresenter(this.eventBus, structures);
        DETAILS_TABS.add(structures);
 
        DataSetTab.Display dataset = new DataSetTabDisplay(getDataSetEventBus());
        new DataSetTabPresenter(this.eventBus, dataset, getDataSetEventBus());
        DETAILS_TABS.add(dataset);

        DownloadsTab.Display downloads = new DownloadsTabDisplay();
        new DownloadsTabPresenter(this.eventBus, downloads);
        DETAILS_TABS.add(downloads);
   }

}
