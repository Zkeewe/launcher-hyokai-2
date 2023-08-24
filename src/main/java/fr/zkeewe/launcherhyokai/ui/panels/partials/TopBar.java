package fr.zkeewe.launcherhyokai.ui.panels.partials;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.zkeewe.launcherhyokai.ui.PanelManager;
import fr.zkeewe.launcherhyokai.ui.panel.Panel;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TopBar extends Panel {
    private GridPane topBar;

    @Override
    public String getName() {
        return "TopBar";
    }

    @Override
    public String getStylesheetPath() {
        return null;
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        this.topBar = this.layout;
        this.layout.setStyle("-fx-background-color: rgb(18,5,35);");

        /**
         * TopBar separation
         */
        // TopBar: left side
        /**ImageView imageView = new ImageView();
        imageView.setImage(new Image("images/icon.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(25);
        setLeft(imageView);
        this.layout.getChildren().add(imageView);*/

        // TopBar: center
        Label title = new Label("ヒ  ョ  ウ  カ  イ");
        title.setFont(Font.font("Noto Serif JP ExtraLight",26f));
        title.setStyle("-fx-text-fill: white;");
        setCenterH(title);
        this.layout.getChildren().add(title);

        // TopBar: right side
        GridPane topBarButton = new GridPane();
        topBarButton.setMinWidth(100d);
        topBarButton.setMaxWidth(100d);
        setCanTakeAllSize(topBarButton);
        setRight(topBarButton);
        this.layout.getChildren().add(topBarButton);

        /**
         * TopBar buttons configurations
         */
        MaterialDesignIconView closeBtn = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_CLOSE);
        //MaterialDesignIconView fullscreenBtn = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MAXIMIZE);
        MaterialDesignIconView minimizeBtn = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MINIMIZE);
        setCanTakeAllWidth(closeBtn, minimizeBtn);

        closeBtn.setFill(Color.WHITE);
        closeBtn.setOpacity(.7f);
        closeBtn.setSize("35px");
        closeBtn.setOnMouseEntered(e -> closeBtn.setOpacity(1.f));
        closeBtn.setOnMouseExited(e -> closeBtn.setOpacity(.7f));
        closeBtn.setOnMouseClicked(e ->
                System.exit(0));
        closeBtn.setTranslateX(60f);

        /*
        fullscreenBtn.setFill(Color.WHITE);
        fullscreenBtn.setOpacity(0.70f);
        fullscreenBtn.setSize("31px");
        fullscreenBtn.setOnMouseEntered(e -> fullscreenBtn.setOpacity(1.0f));
        fullscreenBtn.setOnMouseExited(e -> fullscreenBtn.setOpacity(0.7f));
        fullscreenBtn.setOnMouseClicked(e ->
                this.panelManager.getStage().setMaximized(!this.panelManager.getStage().isMaximized()));
        fullscreenBtn.setTranslateX(40.0d);
        */

        minimizeBtn.setFill(Color.WHITE);
        minimizeBtn.setOpacity(0.70f);
        minimizeBtn.setSize("35px");
        minimizeBtn.setOnMouseEntered(e -> minimizeBtn.setOpacity(1.0f));
        minimizeBtn.setOnMouseExited(e -> minimizeBtn.setOpacity(0.7f));
        minimizeBtn.setOnMouseClicked(e ->
                this.panelManager.getStage().setIconified(true));
        minimizeBtn.setTranslateX(20.0d);

        topBarButton.getChildren().addAll(closeBtn, minimizeBtn);
    }
}
