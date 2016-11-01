package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

public class Main extends SimpleApplication implements ActionListener,
                AnalogListener{

  public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(640, 640);
        MainMenu app = new MainMenu();
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
        app.start();
  }
  protected Geometry geom;

   private Spatial player;
  
  @Override
  public void simpleInitApp() {
    Box b = new Box(0.1f, 0.1f, 0.1f);
    
    geom = new Geometry("Player", b);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    geom.setMaterial(mat);
    
//    rootNode.attachChild(geom);
            
    initCamera();
    
    
//    inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
//    inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
//    inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
//    inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
//    inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_RETURN));
    
    
//    inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_MEMORY );

    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_A),
                                      new KeyTrigger(KeyInput.KEY_LEFT));

    inputManager.addMapping("Up",   new KeyTrigger(KeyInput.KEY_W),
                                    new KeyTrigger(KeyInput.KEY_UP));

    inputManager.addMapping("Down",   new KeyTrigger(KeyInput.KEY_S),
                                      new KeyTrigger(KeyInput.KEY_DOWN));

    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_D),
                                      new KeyTrigger(KeyInput.KEY_RIGHT));


    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE));

    inputManager.addMapping("Mouse_Up",   new MouseAxisTrigger(MouseInput.AXIS_Y,true));
    inputManager.addMapping("Mouse_Down", new MouseAxisTrigger(MouseInput.AXIS_Y,false));

    inputManager.addMapping("Mouse_Right", new MouseAxisTrigger(MouseInput.AXIS_X,true));
    inputManager.addMapping("Mouse_Left",  new MouseAxisTrigger(MouseInput.AXIS_X,false));

    inputManager.addListener(this,"Left", "Right", "Rotate", "Up","Down","Mouse_Up",
            "Mouse_Down","Mouse_Right","Mouse_Left");
    
    
    
    Node node = new Node("Player");
    node.setMaterial(mat);
    
    Picture pic;
    pic = new Picture("Player");
    pic.setMaterial(mat);
    float width = 50;
    float height =50;
    pic.setWidth(width);
    pic.setHeight(height);
    node.attachChild(pic);
    
    
    player = node;
    player.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2, 0f);
    player.addControl(new Player());
        
    
    guiNode.attachChild(player);
  }
  
  @Override
    public void simpleUpdate(float tpf) {

    }
  
  
   public void onAction(String name, boolean isPressed, float tpf) {
     
       if (name.equals("Rotate")){
                player.getControl(Player.class).rotate = isPressed;
            } 
       if (name.equals("Mouse_Up")){
                player.getControl(Player.class).up_mause = isPressed;
            } 
       if (name.equals("Mouse_Down")){
                player.getControl(Player.class).down_mause = isPressed;
            } 
       if (name.equals("Mouse_Left")){
                player.getControl(Player.class).left_mause = isPressed;
            } 
       if (name.equals("Mouse_Right")){
                player.getControl(Player.class).right_mause = isPressed;
            } 
       if (name.equals("Up")) {
                player.getControl(Player.class).up_key = isPressed;
            } 
       if (name.equals("Down")) {
                player.getControl(Player.class).down_key =isPressed;
            }
       if (name.equals("Left")) {
                player.getControl(Player.class).left_key = isPressed;
            } if (name.equals("Right")) {
                player.getControl(Player.class).right_key = isPressed;
            } 
    }
   public void onAnalog(String name, float value, float tpf) {
       
   }
   
   
   private void initCamera(){    
        cam.setLocation(new Vector3f(0.0f, 0.0f, 0.5f));
        getFlyByCamera().setEnabled(false);
        setDisplayStatView(false);
        setDisplayFps(false);
        cam.setParallelProjection(true);
   }
};
