package fr.zkeewe.launcherhyokai.ui.panels.pages;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.zkeewe.launcherhyokai.Launcher;
import fr.zkeewe.launcherhyokai.ui.PanelManager;
import fr.zkeewe.launcherhyokai.ui.panel.Panel;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.*;

public class Login extends Panel {
    GridPane loginCard = new GridPane();

    Saver saver = Launcher.getInstance().getSaver();
    Button btnLogin = new Button("Login with\n" +  " Microsoft");

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStylesheetPath() {

        return "css/login.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background
        this.layout.getStyleClass().add("login-layout");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMinWidth(470);
        columnConstraints.setMaxWidth(470);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
        this.layout.add(loginCard, 0, 0);

        // Background image
        GridPane backgroundImage = new GridPane();
        setCanTakeAllSize(backgroundImage);
        backgroundImage.getStyleClass().add("background-image");
        this.layout.add(backgroundImage, 1, 0);

        // Login card
        setCanTakeAllSize(this.layout);
        loginCard.getStyleClass().add("login-card");
        setLeft(loginCard);
        setCenterH(loginCard);
        setCenterV(loginCard);

        /*
         * Login Sidebar
         */
        Label title = new Label("Hyokai         \n" + "   Launcher");
        title.setFont(Font.font("Osaka-Sans Serif", 75f));
        title.getStyleClass().add("login-title");
        setCenterH(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30d);
        loginCard.getChildren().add(title);

        //Login button
        setCanTakeAllSize(btnLogin);
        setCenterV(btnLogin);
        setCenterH(btnLogin);
        btnLogin.setDisable(false);
        btnLogin.setMaxWidth(400);
        btnLogin.setMaxHeight(120);
        btnLogin.setTranslateY(220d);
        btnLogin.getStyleClass().add("login-log-btn");
        btnLogin.setOnMouseClicked(e -> this.authenticateMS());


        loginCard.getChildren().addAll(btnLogin);

    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
            if (error != null) {
                Platform.runLater(() -> {
                    Launcher.getInstance().getLogger().err(error.toString());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(error.getMessage());
                alert.show();
                });
                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();

            Launcher.getInstance().setAuthInfos(new AuthInfos(
                    response.getProfile().getName(),
                    response.getAccessToken(),
                    response.getProfile().getId()
            ));
            this.logger.info("Hello " + response.getProfile().getName());

            Platform.runLater(() -> panelManager.showPanel(new App()));
        });
    }
}
