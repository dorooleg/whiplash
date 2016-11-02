package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


public class StartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private Element textField;
  //private InputManager inputManager;
  private MainMenu owner;
  private String ip;
  
 
  public StartScreen(MainMenu owner) {
      this.owner = owner;
      //this.inputManager = app.getInputManager();
  
  }
  public void startServer()  {
    System.out.println("server");
  }
  
  public void startClient()  {
    textField = nifty.getCurrentScreen().findElementByName("input");
  
    System.out.println("client");
  }

  public void start(String nextScreen) {
    nifty.gotoScreen(nextScreen);  
  }
  public void startGame(String nextScreen) {
    nifty.gotoScreen(nextScreen);  
    owner.init_player();
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
 
  @NiftyEventSubscriber (id = "ip_txt")
  public void onIp(final String id, final TextFieldChangedEvent event)
  {

   ip = event.getText();
   System.out.print("text:"+ip );

  }
  @Override
  public void update(float tpf) {
      
    
    if (screen.getScreenId().equals("hud")) {
      Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
      //textField = nifty.getCurrentScreen().findElementByName("input");
//      niftyElement.getRenderer(TextRenderer.class).setText((int)(tpf*100000) + ""); 
    }
    
  }
}
