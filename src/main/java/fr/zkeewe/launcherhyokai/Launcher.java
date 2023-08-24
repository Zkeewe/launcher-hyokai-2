package fr.zkeewe.launcherhyokai;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.zkeewe.launcherhyokai.ui.PanelManager;
import fr.zkeewe.launcherhyokai.ui.panels.pages.App;
import fr.zkeewe.launcherhyokai.ui.panels.pages.Login;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.Path;

public class Launcher extends Application {
    private PanelManager panelManager;
    private static Launcher instance;
    private final ILogger logger;
    private final Path launcherDir = GameDirGenerator.createGameDir("hyokai-launcher", true);
    private final Saver saver;
    private AuthInfos authInfos = null;

    public Launcher() {
        instance = this;
        this.logger = new Logger("[HyokaiLauncher]", this.launcherDir.resolve("launcher-hyokai.log"));
        if (!this.launcherDir.toFile().exists()) {
            if (!this.launcherDir.toFile().mkdir()) {
                this.logger.err("Unable to create launcher folder");
            }
        }

        saver = new Saver(this.launcherDir.resolve("config.properties"));
        saver.load();

    }

    @Override
    public void start(Stage stage) throws Exception {
        this.logger.info("Starting launcher");
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();

        Font.loadFont(
                this.getClass().getResource("/fonts/NotoSerifJP-ExtraLight.otf").toExternalForm(),
                10
        );
        Font.loadFont(
                this.getClass().getResource("/fonts/osaka-re.ttf").toExternalForm(),
                10
        );
        Font.loadFont(
                this.getClass().getResource("/fonts/REM-VariableFont_wght.ttf").toExternalForm(),
                10
        );

        if (this.isUserAlreadyLoggedIn()) {
            logger.info("Hello " + authInfos.getUsername());

            this.panelManager.showPanel(new App());
        } else {
            this.panelManager.showPanel(new Login()); //LOGIN
        }
    }

    public boolean isUserAlreadyLoggedIn() {
        if (saver.get("msAccessToken") != null && saver.get("msRefreshToken") != null) {
            try {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult response = authenticator.loginWithRefreshToken(saver.get("msRefreshToken"));

                saver.set("msAccessToken", response.getAccessToken());
                saver.set("msRefreshToken", response.getRefreshToken());
                saver.save();
                this.setAuthInfos(new AuthInfos(
                        response.getProfile().getName(),
                        response.getAccessToken(),
                        response.getProfile().getId()
                ));
                return true;
            } catch (MicrosoftAuthenticationException e) {
                saver.remove("msAccessToken");
                saver.remove("msRefreshToken");
                saver.save();
            }
        }
        return false;
    }

    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    public void setAuthInfos(AuthInfos authInfos) {
        this.authInfos = authInfos;
    }

    public ILogger getLogger() {
        return logger;
    }

    public static Launcher getInstance() {
        return instance;
    }

    public Saver getSaver() {
        return saver;
    }

    public Path getLauncherDir() {
        return launcherDir;
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public void hideWindow() {
        this.panelManager.getStage().hide();
    }
}
