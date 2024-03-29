package fr.zkeewe.launcherhyokai.ui.panel;

import fr.zkeewe.launcherhyokai.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init(PanelManager panelManager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStylesheetPath();
}
