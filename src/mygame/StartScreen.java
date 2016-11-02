package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.IOException;

public class StartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  
  private MainMenu owner;
  
  public Server server = null;
  public Client client = null;
  
 
  public StartScreen(MainMenu owner) {
      this.owner = owner;
  
  }
  public void startServer() throws IOException, InterruptedException  {
    server = Network.createServer(6143);
    server.start();
    server.addMessageListener(new ServerListener(owner), ProtocolMessage.class);
    System.out.println("server");
    nifty.gotoScreen("server");  
    owner.init_player();
    while (!server.hasConnections()) {
        Thread.sleep(1000);
    }
  }
  
  public void startClient() throws IOException  {
    client = Network.connectToServer("localhost", 6143);
    client.start();
    client.addMessageListener(new ClientListener(owner), ProtocolMessage.class);
    System.out.println("client");
    nifty.gotoScreen("client");  
    owner.init_player();
  }

  public void startGame(String nextScreen) {
    nifty.gotoScreen(nextScreen);  
    owner.init_player();
  }

  public void quitGame() {
    app.stop();
  }

  public String getPlayerName() {
    return System.getProperty("user.name");
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

  @Override
  public void update(float tpf) {
      
    if (screen.getScreenId().equals("hud")) {
      Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
      niftyElement.getRenderer(TextRenderer.class).setText((int)(tpf*100000) + ""); 
    }
    
  }
}
