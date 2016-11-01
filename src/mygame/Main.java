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

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.texture.Texture2D;

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
        


        inputManager.addListener(this, "Left", "Right", "Rotate", "Up", "Down", "MouseMoved");

//    inputManager.setMouseCursor( (JmeCursor) assetManager.loadAsset());
//    inputManager.setCursorVisible(false);

        player = getSpatial("Player");
        player.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0f);
        player.addControl(new Player());


        guiNode.attachChild(player);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

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
    }

    public void onAnalog(String name, float value, float tpf) {
    }

    private void initCamera() {
        cam.setLocation(new Vector3f(0.0f, 0.0f, 0.5f));
        getFlyByCamera().setEnabled(false);
        setDisplayStatView(false);
        setDisplayFps(false);
        inputManager.setCursorVisible(false);
        cam.setParallelProjection(true);
//        cam.set
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

         
            Picture pic1;
            pic1 = new Picture(name);
            Texture2D tex = (Texture2D)assetManager.loadTexture(
                    "/Textures/spiral.png");
            pic1.setTexture(assetManager, tex, true);

            pic1.setWidth(width);
            pic1.setHeight(height);
            pic1.move(-width/2f - 50, -height/2f, 0);
            
            node.attachChild(pic1);
         
            node.attachChild(pic);

            node.attachChild(blue);

            return node;
        }
        return null;
    }
};
