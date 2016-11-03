package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.sun.istack.internal.NotNull;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartScreen extends AbstractAppState implements ScreenController {


    private final static int PORT = 6143;

    private Nifty nifty;
    private Application app;
    private Screen screen;
    //private Element textField;
    private MainMenu owner;
    private String ip;
    public Server server = null;
    public Client client = null;
    private Element progressBarElement;
    private Element progressBarWhipStatus;
    private Element progressBarElement_empty;
    private TextRenderer textRenderer;

    public StartScreen(MainMenu owner) {
        this.owner = owner;
        server = null;
        client = null;
    }

    public void clearPlayers() {
        owner.clearPlayers();
    }

    public void startServer() {
        nifty.gotoScreen("server");
        try {
            server = Network.createServer(PORT);
            server.start();
            server.addMessageListener(new ServerListener(owner), ProtocolMessage.class);
            server.addConnectionListener(new ServerConnectionListener(this));
            setTextOpenServerBody("Loading... " + Inet4Address.getLocalHost().getHostAddress() + ":" + PORT);
        } catch (Exception ex) {
            Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
            setTextOpenServerHead("Error creating server");
            setTextOpenServerBody("");
            server = null;
        }
    }

    public void setTextOpenServerBody(@NotNull final String text) {
        Element element = nifty.getScreen("server").findElementByName("text_open_server_body");
        element.getRenderer(TextRenderer.class).setText(text);
    }

    public void setTextOpenServerHead(@NotNull final String text) {
        Element element = nifty.getScreen("server").findElementByName("text_open_server_head");
        element.getRenderer(TextRenderer.class).setText(text);
    }
    
    public void setTextClient(@NotNull final String text) {
        Element element = nifty.getScreen("ip").findElementByName("text_client");
        element.getRenderer(TextRenderer.class).setText(text);
    }

    public void cancelServer() {
        System.out.println("Cancel Server");
        if (server != null) {
            server.close();
            server = null;
        }
        nifty.gotoScreen("start");
    }

    public void clean() {
        if (server != null) {
            server.close();
            server = null;
        }

        if (client != null) {
            client.close();
            client = null;
        }

        nifty.gotoScreen("start");
    }

    public void startClient() {
        try {
            client = Network.connectToServer(ip, 6143);
            client.start();
            client.addMessageListener(new ClientListener(owner), ProtocolMessage.class);
            client.addErrorListener(new ClientErrorListener(this));
            owner.initPlayer("player2");
            nifty.gotoScreen("game");
            //textField = nifty.getCurrentScreen().findElementByName("input");
        } catch (IOException ex) {
            Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
            setTextClient("Error connection to server");
        }

    }


    public void setProgress(final float progress) {
        if (progress != 0) {
//            System.err.println("setProgress");
//            System.err.println(progressBarElement.getParent().getWidth());
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);

            progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarElement.getParent().layoutElements();
        } else {
//            System.out.println("prigress = " + progress);
            progressBarElement.setVisible(false);

        }

    }
    
     public void setProgressWhip(final float progress) {
        if (progress != 0) {
//            System.err.println("whip .....");
//            System.err.println(progressBarWhipStatus.getParent().getWidth());
            final int MIN_WIDTH = 32;
            int pixelWidth = (int) (MIN_WIDTH + (progressBarWhipStatus.getParent().getWidth() - MIN_WIDTH) * progress);

            progressBarWhipStatus.setConstraintWidth(new SizeValue(pixelWidth + "px"));
            progressBarWhipStatus.getParent().layoutElements();
            progressBarWhipStatus.setVisible(true);
        } else {
//            System.out.println("whip = " + progress);
            progressBarWhipStatus.setVisible(false);

        }

    }
    
    public void start(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }

    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        owner.initPlayer("player");
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

        this.nifty = nifty;
        this.screen = screen;

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
