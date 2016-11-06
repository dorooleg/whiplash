package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.Callable;

public class StartScreen extends AbstractAppState implements ScreenController {

    Nifty nifty;
    private Application app;
    public MainMenu owner;
    private String ip;
    public mygame.Server server = null;
    public mygame.Client client = null;
    private Element progressBarElement;
    private Element progressBarWhipStatus;
    private Element progressBarElement_empty;
    private Element progressBarElement_blue;
    private Element progressBarWhipStatus_blue;
    private Element progressBarElement_empty_blue;

    public StartScreen(MainMenu owner) {
        this.owner = owner;
    }

    public void clearPlayers() {
        owner.clearPlayers();
    }

    public void startServer() {
        nifty.gotoScreen("server");
        try {
            server = new mygame.Server(owner, this);
            setTextOpenServerBody("Loading... ");
        } catch (Exception ex) {
            setTextOpenServerHead("Error creating server");
            setTextOpenServerBody("");
            server = null;
        }
    }

    public void setTextOpenServerBody(final String text) {
        Element element = nifty.getScreen("server").findElementByName("text_open_server_body");
        element.getRenderer(TextRenderer.class).setText(text);
    }

    public void setTextOpenServerHead(final String text) {
        Element element = nifty.getScreen("server").findElementByName("text_open_server_head");
        element.getRenderer(TextRenderer.class).setText(text);
    }

    public void setTextClient(final String text) {
        Element element = nifty.getScreen("ip").findElementByName("text_client");
        element.getRenderer(TextRenderer.class).setText(text);
    }

    public void cancelServer() {
        if (server != null) {
            server.close();
            server = null;
        }
        gotoScreen("start");
    }
    
    public void gotoScreen(String screen) {
        nifty.gotoScreen(screen);
    }

    public void cleanNetwork() {
        if (server != null) {
            server.close();
            server = null;
        }

        if (client != null) {
            client.close();
            client = null;
        }
    }

    public void startClient() {
        try {
            client = new mygame.Client(ip, owner, this);
        } catch (Exception ex) {
            setTextClient("Error connection to server");
        }
    }

    public void setProgress(final float progress) {
        if (progress != 0) {
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);

            progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarElement.getParent().layoutElements();
            progressBarElement.setVisible(true);
        } else {
            progressBarElement.setVisible(false);
        }

    }

    public void setProgressWhip(final float progress) {
        if (progress != 0) {
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH
                    + (progressBarWhipStatus.getParent().getWidth()
                    - MIN_WIDTH) * progress);

            progressBarWhipStatus.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarWhipStatus.getParent().layoutElements();
            progressBarWhipStatus.setVisible(true);
        } else {
            progressBarWhipStatus.setVisible(false);

        }

    }

    public void setProgress_Blue(final float progress) {
        if (progress != 0) {
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH
                    + (progressBarElement_blue.getParent().getWidth()
                    - MIN_WIDTH) * progress);

            progressBarElement_blue.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarElement_blue.getParent().layoutElements();
            progressBarElement_blue.setVisible(true);
        } else {
            progressBarElement_blue.setVisible(false);

        }

    }

    public void setProgressWhip_blue(final float progress) {
        if (progress != 0) {
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH
                    + (progressBarWhipStatus_blue.getParent().getWidth()
                    - MIN_WIDTH) * progress);

            progressBarWhipStatus_blue.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarWhipStatus_blue.getParent().layoutElements();
            progressBarWhipStatus_blue.setVisible(true);
        } else {
            progressBarWhipStatus_blue.setVisible(false);

        }

    }

    public void start(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }

    public void startGame(final String nextScreen) {

        synchronized (this) {
            owner.shortListEvents.add(new Callable() {
                public Object call() throws Exception {
                    nifty.gotoScreen(nextScreen);
                    owner.initPlayer("player");
                    return null;
                }
            });
        }

    }

    public void quitGame() {
        app.stop();
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        progressBarElement = nifty.getScreen("game").findElementByName("progressbar1");
        progressBarElement.setWidth(100);

        progressBarWhipStatus = nifty.getScreen("game").findElementByName("progressbar2");
        progressBarWhipStatus.setWidth(100);

        progressBarElement_empty = nifty.getScreen("game").findElementByName("progressbar_empty");
        progressBarElement_empty.setWidth(100);
        progressBarElement_empty.setVisible(false);

        progressBarElement_blue = nifty.getScreen("game").findElementByName("progressbar1_blue");
        progressBarElement_blue.setWidth(100);

        progressBarWhipStatus_blue = nifty.getScreen("game").findElementByName("progressbar2_blue");
        progressBarWhipStatus_blue.setWidth(100);

        progressBarElement_empty_blue = nifty.getScreen("game").findElementByName("progressbar_empty_blue");
        progressBarElement_empty_blue.setWidth(100);
        progressBarElement_empty_blue.setVisible(false);


        this.nifty = nifty;

    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
    }

    @NiftyEventSubscriber(id = "ip_txt")
    public void onIp(final String id, final TextFieldChangedEvent event) {
        ip = event.getText();
    }

    @Override
    public void update(float tpf) {
    }
}
