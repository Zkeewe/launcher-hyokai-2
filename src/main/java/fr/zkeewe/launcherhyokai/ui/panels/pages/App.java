package fr.zkeewe.launcherhyokai.ui.panels.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.zkeewe.launcherhyokai.Launcher;
import fr.zkeewe.launcherhyokai.ui.PanelManager;
import fr.zkeewe.launcherhyokai.ui.panel.Panel;
import fr.zkeewe.launcherhyokai.ui.panels.pages.content.ContentPanel;
import fr.zkeewe.launcherhyokai.ui.panels.pages.content.Home;
import fr.zkeewe.launcherhyokai.ui.panels.pages.content.Settings;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class App extends Panel {
    GridPane sideMenu = new GridPane();
    GridPane navContent = new GridPane();

    Node activeLink = null;
    ContentPanel currentPage = null;

    Button homeBtn, settingsBtn;

    Saver saver = Launcher.getInstance().getSaver();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStylesheetPath() {
        return "css/app.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background
        this.layout.getStyleClass().add("app-layout");
        setCanTakeAllSize(this.layout);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMinWidth(470);
        columnConstraints.setMaxWidth(470);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());

        // Side menu
        this.layout.add(sideMenu, 0, 0);
        sideMenu.getStyleClass().add("sidemenu");
        setLeft(sideMenu);
        setCenterH(sideMenu);
        setCenterV(sideMenu);

        // Background Image
        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        //Nav content
        this.layout.add(navContent, 1, 0);
        navContent.getStyleClass().add("nav-content");
        setLeft(navContent);
        setCenterH(navContent);
        setCenterV(navContent);

        /*
         * Side menu
         */
        // Titre
        Label title = new Label("Hyokai         \n" + "   Launcher");
        title.setFont(Font.font("Osaka-Sans Serif", 75f));
        title.getStyleClass().add("home-title");
        setCenterH(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30d);
        sideMenu.getChildren().add(title);

        // Navigation
        homeBtn = new Button("Home");
        homeBtn.getStyleClass().add("sidemenu-nav-btn");
        homeBtn.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.HOME));
        setCanTakeAllSize(homeBtn);
        setTop(homeBtn);
        homeBtn.setTranslateY(200d);
        homeBtn.setOnMouseClicked(e -> setPage(new Home(), homeBtn));

        settingsBtn = new Button("Settings");
        settingsBtn.getStyleClass().add("sidemenu-nav-btn");
        settingsBtn.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.GEARS));
        setCanTakeAllSize(settingsBtn);
        setTop(settingsBtn);
        settingsBtn.setTranslateY(260d);
        settingsBtn.setOnMouseClicked(e -> setPage(new Settings(), settingsBtn));

        sideMenu.getChildren().addAll(homeBtn, settingsBtn);

        // Pseudo + avatar
        GridPane userPane = new GridPane();
        setCanTakeAllWidth(userPane);
        userPane.setMaxHeight(80);
        userPane.setMinWidth(80);
        userPane.getStyleClass().add("user-pane");
        setBottom(userPane);

        String avatarUrl = "https://minotar.net/avatar/" + (
                Launcher.getInstance().getAuthInfos().getUuid() + ".png"
        );
        ImageView avatarView = new ImageView();
        Image avatarImg = new Image(avatarUrl);
        avatarView.setImage(avatarImg);
        avatarView.setPreserveRatio(true);
        avatarView.setFitHeight(50d);
        setCenterV(avatarView);
        setCanTakeAllSize(avatarView);
        setLeft(avatarView);
        avatarView.setTranslateX(15d);
        userPane.getChildren().add(avatarView);

        Label usernameLabel = new Label(Launcher.getInstance().getAuthInfos().getUsername());
        usernameLabel.setFont(Font.font("REM Medium", 25f));
        setCanTakeAllSize(usernameLabel);
        setCenterV(usernameLabel);
        setLeft(usernameLabel);
        usernameLabel.getStyleClass().add("username-label");
        usernameLabel.setTranslateX(75d);
        setCanTakeAllWidth(usernameLabel);
        userPane.getChildren().add(usernameLabel);

        Button logoutBtn = new Button();
        FontAwesomeIconView logoutIcon = new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT);
        logoutIcon.getStyleClass().add("logout-icon");
        setCanTakeAllSize(logoutBtn);
        setCenterV(logoutBtn);
        setRight(logoutBtn);
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setGraphic(logoutIcon);
        logoutBtn.setOnMouseClicked(e -> {
            if (currentPage instanceof Home && ((Home) currentPage).isDownloading()) {
                return;
            }
            saver.remove("accessToken");
            saver.remove("clientToken");
            saver.remove("msAccessToken");
            saver.remove("msRefreshToken");
            saver.save();
            Launcher.getInstance().setAuthInfos(null);
            this.panelManager.showPanel(new Login());
        });
        userPane.getChildren().add(logoutBtn);

        sideMenu.getChildren().add(userPane);
    }

    @Override
    public void onShow() {
        super.onShow();
        setPage(new Home(), homeBtn);
    }

    public void setPage(ContentPanel panel, Node navButton) {
        if (currentPage instanceof Home && ((Home) currentPage).isDownloading()) {
            return;
        }
        if (activeLink != null)
            activeLink.getStyleClass().remove("active");
        activeLink = navButton;
        activeLink.getStyleClass().add("active");

        this.navContent.getChildren().clear();
        if (panel != null) {
            this.navContent.getChildren().add(panel.getLayout());
            currentPage = panel;
            if (panel.getStylesheetPath() != null) {
                this.panelManager.getStage().getScene().getStylesheets().clear();
                this.panelManager.getStage().getScene().getStylesheets().addAll(
                        this.getStylesheetPath(),
                        panel.getStylesheetPath()
                );
            }
            panel.init(this.panelManager);
            panel.onShow();
        }
    }
}
