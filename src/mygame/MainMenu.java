/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializer;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.sun.istack.internal.NotNull;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class MainMenu extends SimpleApplication implements ActionListener, AnalogListener {

    private int health;
    private StartScreen startScreen;
  
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        setPauseOnLostFocus(false);

//        Box b = new Box( 1, 1, 1);
//        Geometry geom = new Geometry("Box", b);
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        geom.setMaterial(mat);
//        rootNode.attachChild(geom);

        startScreen = new StartScreen(this);
        stateManager.attach(startScreen);
        
        
        inputManager.addListener(this, "MouseMoved");
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/tutorial/screen3.xml", "start", startScreen);
        //nifty.setDebugOptionPanelColors(true);

        flyCam.setDragToRotate(true);  
        Serializer.registerClass(ProtocolMessage.class);
        Serializer.registerClass(ProtocolMessage.Entry.class);
    }
    
    public int a = 0  ;
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
        if (name.equals("Rotate")) {
            player.getControl(Player.class).rotate = isPressed;
        }
        if (name.equals("MouseMoved")) {
            player.getControl(Player.class).mouse_position =
                    inputManager.getCursorPosition();
            player.getControl(Player.class).move_mouse = isPressed;
        }
        if (name.equals("Up")) {
            player.getControl(Player.class).up_key = isPressed;
        }
        if (name.equals("Down")) {
            player.getControl(Player.class).down_key = isPressed;
        }
        if (name.equals("Left")) {
            player.getControl(Player.class).left_key = isPressed;
        }
        if (name.equals("Right")) {
            player.getControl(Player.class).right_key = isPressed;
        }
        
        if (name.equals("Mouse_Click_Left")){
            player.getControl(Player.class).click_mouse = isPressed;
        }
        

    }
    

    
    public Spatial player;
    public Spatial player2;
    
    public void init_player(){
       initCamera();
       
       inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A),
                new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W),
                new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S),
                new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D),
                new KeyTrigger(KeyInput.KEY_RIGHT));


        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping("MouseMoved", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new MouseAxisTrigger(MouseInput.AXIS_X, false));
        
        inputManager.addMapping("Mouse_Click_Left", 
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));


       inputManager.addListener(this, "Left", "Right", "Rotate", "Up", "Down", 
               "MouseMoved","Mouse_Click_Left");
       
       player = getSpatial("Player");
       player.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0f);
       player.addControl(new Player());
       player2 = getSpatial("Player");
       player2.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0f);
//
//
       guiNode.attachChild(player);
       guiNode.attachChild(player2);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        if (!list.isEmpty()) {
            try {
                list.listIterator().next().call();
            } catch (Exception ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            list.clear();
        }
    }
    
     private void initCamera() {
        cam.setLocation(new Vector3f(0.0f, 0.0f, 0.5f));
        getFlyByCamera().setEnabled(false);
        setDisplayStatView(false);
        setDisplayFps(false);
        inputManager.setCursorVisible(true);
        cam.setParallelProjection(true);
    }
     

     ArrayList<Callable> list = new ArrayList<Callable>();
     public void updateState(@NotNull final ProtocolMessage message) {

         synchronized(this) {
            list.add(new Callable() {

             public Object call() throws Exception {
                    player2.setLocalTranslation(message.entries.get(0).x, message.entries.get(0).y, 0);
                    player2.setLocalRotation(message.entries.get(0).rotation);
               //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                 return null;
             }   
            });
         }
     }
     
     
     private Spatial getSpatial(String name) {
        if (name  == "Player"){
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);

            Node node = new Node(name);
            node.setMaterial(mat);

            Picture pic;

            pic = new Picture(name);
            pic.setMaterial(mat);
            float width = 50;
            float height = 50;
            pic.setWidth(width);
            pic.setHeight(height);
            pic.move(-width / 2f, -height / 2f, 0);


            Box box1 = new Box( 5,5,0);
            Geometry blue = new Geometry(name, box1);
            Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat1.setColor("Color", ColorRGBA.Red);
            blue.setMaterial(mat1);
            blue.move(-width / 2f , 0, 0);

         
            Node pictire = new Node("whip");
            Picture pic1;
            pic1 = new Picture(name);
            Texture2D tex = (Texture2D)assetManager.loadTexture(
                    "/Textures/spiral.png");
            pic1.setTexture(assetManager, tex, true);

            pic1.setWidth(width);
            pic1.setHeight(height);
            pic1.move(-width/2f - 50, -height/2f, 0);
            
            pictire.attachChild(pic1);
         
            node.attachChild(pictire);
            node.attachChild(pic);

            node.attachChild(blue);

            return node;
        }
        return null;
    }

     @Override
    public void onAnalog(String name, float value, float tpf) {
         
        if (startScreen.client != null) {
            ProtocolMessage protocolMessage = new ProtocolMessage();
            ProtocolMessage.Entry entry = new ProtocolMessage.Entry();
            entry.rotation = player.getLocalRotation();
            entry.x = player.getLocalTranslation().x;
            entry.y = player.getLocalTranslation().y;
            protocolMessage.addEntry(entry);
            startScreen.client.send(protocolMessage);
        }
        
        if (startScreen.server != null) {
            ProtocolMessage protocolMessage = new ProtocolMessage();
            ProtocolMessage.Entry entry = new ProtocolMessage.Entry();
            entry.rotation = player.getLocalRotation();
            entry.x = player.getLocalTranslation().x;
            entry.y = player.getLocalTranslation().y;
            protocolMessage.addEntry(entry);
            startScreen.server.broadcast(protocolMessage);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
