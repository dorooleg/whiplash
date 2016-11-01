package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import com.jme3.math.FastMath;

public class Player  extends AbstractControl{
    
    
   
    public boolean up_key, 
            down_key, 
            left_key, 
            right_key;
    
    public boolean up_mause,
            down_mause,
            left_mause,
            right_mause;
    
    public boolean rotate;
    
    public Float  speed = 100f,
            speed_mouse = 10f;
    // speed of the player
    // lastRotation of the player
    private float lastRotation;

    public Player() {
        
    }
    public Player(int width, int height) {
//        this.screenWidth  = width;
//        this.screenHeight = height;
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        if (rotate) {
//            Vector3f v = spatial.getLocalTranslation();
            
            spatial.rotate(0, 0 , tpf*speed_mouse);
//            reset();
        }
        if (left_mause) {
            spatial.rotate(0, 0, -tpf*speed_mouse);
            left_mause = false;
        }
        if (right_mause){
            spatial.rotate(0, 0, tpf*speed_mouse); 
            right_mause = false;
        }

        if (left_key) {
//                  Vector3f v = spatial.getLocalTranslation();
//                  spatial.setLocalTranslation(v.x - .01f, v.y, v.z);
            spatial.move(-tpf * speed, 0, 0);
//            reset();
        } 
        if (right_key) {
//                  Vector3f v = spatial.getLocalTranslation();
//                  spatial.setLocalTranslation(v.x - .01f, v.y, v.z);
            spatial.move(tpf * speed, 0, 0);
//            reset();
        }
        if (up_key){
//                  Vector3f v = spatial.getLocalTranslation();
//                  spatial.setLocalTranslation(v.x, v.y + .01f, v.z);
            spatial.move(0, tpf * speed, 0);
//            reset();
        }
        if (down_key){
//                  Vector3f v = spatial.getLocalTranslation();
//                  spatial.setLocalTranslation(v.x, v.y - .01f, v.z);
            spatial.move(0, -tpf * speed, 0);
//            reset();
        }
    }


    public void applyGravity(Vector3f gravity) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}

    // reset the moving values (i.e. for spawning)
    public void reset() {
        up_key = false;
        down_key = false;
        left_key = false;
        right_key = false;
        
        up_mause = false;
        down_mause = false;
        left_mause = false;
        right_mause = false;
        
        rotate = false;
    }

   
   
//    Player(Geometry player, InputManager inpu){
//        geom = player;
//        inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_MEMORY );
//        inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
//
//        inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_A),
//                                          new KeyTrigger(KeyInput.KEY_LEFT));
//
//        inputManager.addMapping("Up",   new KeyTrigger(KeyInput.KEY_W),
//                                        new KeyTrigger(KeyInput.KEY_UP));
//
//        inputManager.addMapping("Down",   new KeyTrigger(KeyInput.KEY_S),
//                                          new KeyTrigger(KeyInput.KEY_DOWN));
//
//        inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_D),
//                                          new KeyTrigger(KeyInput.KEY_RIGHT));
//
//
//        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
//                                          new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
//
//        inputManager.addMapping("Mouse_Up",   new MouseAxisTrigger(MouseInput.AXIS_Y,true));
//        inputManager.addMapping("Mouse_Down", new MouseAxisTrigger(MouseInput.AXIS_Y,false));
//
//        inputManager.addMapping("Mouse_Right", new MouseAxisTrigger(MouseInput.AXIS_X,true));
//        inputManager.addMapping("Mouse_Left",  new MouseAxisTrigger(MouseInput.AXIS_X,false));
//
//
//
//        inputManager.addListener(new AnalogListener() {
//            @Override
//            public void onAnalog(String name, float value, float tpf) {
//                if (name.equals("Rotate")) {
//                  geom.rotate(0, 0, .1f);
//                }else  if (name.equals("Mouse_Up")) {
//                  geom.rotate(0, 0, 10);
//                }else  if (name.equals("Mouse_Down")) {
//                  geom.rotate(0, 0, -10);
//                }else  if (name.equals("Mouse_Left")) {
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x - value , v.y, v.z);
//                }else  if (name.equals("Mouse_Right")) {
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x , v.y, v.z);
//                }else  if (name.equals("Left")) {
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x - .01f, v.y, v.z);
//                } else if (name.equals("Right")) {
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x - .01f, v.y, v.z);
//                }else  if (name.equals("Up")){
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x, v.y + .01f, v.z);
//                }else  if (name.equals("Down")){
//                  Vector3f v = geom.getLocalTranslation();
//                  geom.setLocalTranslation(v.x, v.y - .01f, v.z);
//                }
//            }
//        }, "Left", "Right", "Rotate", "Up","Down","Mouse_Up",
//            "Mouse_Down","Mouse_Right","Mouse_Left");
//
//    }
    
}
