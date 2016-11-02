package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Server;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;

public class StartScreen extends AbstractAppState implements ScreenController {
    private Nifty nifty;
    private Application app;
    private Screen screen;
    private Element textField;
    private MainMenu owner;
    private String ip;
    public Server server = null;
    public Client client = null;

    public StartScreen(MainMenu owner) {
        this.owner = owner;
        server = null;
        client = null;
    }

    public void startServer() throws IOException, InterruptedException {
        server = Network.createServer(6143);
        server.start();
        server.addMessageListener(new ServerListener(owner), ProtocolMessage.class);
        System.out.println("server");
        nifty.gotoScreen("server");
        owner.initPlayer();
        while (!server.hasConnections()) {
            Thread.sleep(1000);
        }
    }
    
    public void startClient() throws IOException {
        client = Network.connectToServer("localhost", 6143);
        client.start();
        client.addMessageListener(new ClientListener(owner), ProtocolMessage.class);
        textField = nifty.getCurrentScreen().findElementByName("input");
        System.out.println("client");
        nifty.gotoScreen("client");
        owner.initPlayer();
    }

    public void start(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }

    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        owner.initPlayer();
    }

    public void quitGame() {
        app.stop();
    }

    public String getPlayerName() {
        return new String("Oleg");
    }

    public void bind(Nifty nifty, Screen screen) {
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
        System.out.print("text:" + ip);
    }

    @Override
    public void update(float tpf) {
    }
}
