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
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializer;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.sun.istack.internal.NotNull;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenu extends SimpleApplication implements ActionListener,
        AnalogListener{


    private StartScreen startScreen;
    private ArrayList<Callable> listEvents;
    public Spatial player;
    public Spatial player2;
    
    private PlayerControl player_control;
    private int draw_flag ;
    private Node [] node_whip;
    private float time_simple_update = 0f;
    private static final float WHIP_WIDTH = 60f;
    private static final float WHIP_HEIGHT = 60f;
    private static final float PLAYER_BODY_WIDTH = 30f;
    private static final float PLAYER_BODY_HEIGHT = 30f;

    public MainMenu() {
        listEvents = new ArrayList<Callable>();
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        setPauseOnLostFocus(false);

        startScreen = new StartScreen(this);
        stateManager.attach(startScreen);

        inputManager.addListener(this, "MouseMoved");
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager,
                audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/tutorial/screen3.xml", "start", startScreen);

        flyCam.setDragToRotate(true);

        //Serializing
        Serializer.registerClass(ProtocolMessage.class);
        Serializer.registerClass(ProtocolMessage.Entry.class);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals("Rotate")) {
            player.getControl(PlayerControl.class).rotate = isPressed;
        }

        if (name.equals("MouseMoved")) {
            player.getControl(PlayerControl.class).mouse_position = inputManager.getCursorPosition();
            player.getControl(PlayerControl.class).move_mouse = isPressed;
        }

        if (name.equals("Up")) {
            player.getControl(PlayerControl.class).up_key = isPressed;
        }

        if (name.equals("Down")) {
            player.getControl(PlayerControl.class).down_key = isPressed;
        }

        if (name.equals("Left")) {
            player.getControl(PlayerControl.class).left_key = isPressed;
        }

        if (name.equals("Right")) {
            player.getControl(PlayerControl.class).right_key = isPressed;
        }

        if (name.equals("Mouse_Click_Left")) {
            player.getControl(PlayerControl.class).mouse_pressed = isPressed;
        }
    }

    public void initPlayer() {
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

        inputManager.addMapping("MouseMoved", new MouseAxisTrigger(
                MouseInput.AXIS_Y, true),
                new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new MouseAxisTrigger(MouseInput.AXIS_X, false));

        inputManager.addMapping("Mouse_Click_Left",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addListener(this, "Left", "Right", "Rotate", "Up", "Down",
                "MouseMoved", "Mouse_Click_Left");

        
       player = getSpatial("Player");

       player.setLocalTranslation(settings.getWidth() / 2,
                                  settings.getHeight() / 2, 0f);
       player_control = new PlayerControl(settings.getWidth(),
                                          settings.getHeight(),30,30, this);
       player.addControl(player_control);
       player_control.setWhipStates(getNodeWhip());
       
       node_whip = getNodeWhip();
       guiNode.attachChild(player);
       guiNode.attachChild(player2);

        player2 = getSpatial("PlayerControl");
        player2.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0f);


       inputManager.addListener(this, "Left", "Right", "Rotate", "Up", "Down", 
               "MouseMoved","Mouse_Click_Left");

        startScreen = new StartScreen(this);
        stateManager.attach(startScreen);
        
        
        inputManager.addListener(this, "MouseMoved");
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/tutorial/screen3.xml", "start", startScreen);

        flyCam.setDragToRotate(true);  
    }
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (player!=null){
            if (name.equals("MouseMoved")) {
                player.getControl(PlayerControl.class).mouse_position =
                        inputManager.getCursorPosition();
                player.getControl(PlayerControl.class).move_mouse = true;
            }
        }
        
        if (startScreen.client != null) {
            startScreen.client.send(getProtocolMessage());
        }

        if (startScreen.server != null) {
            startScreen.server.broadcast(getProtocolMessage());
        }
    }
   

    @Override
    public void simpleUpdate(float tpf) {
        synchronized(this) {
            if (!listEvents.isEmpty()) {
                try {
                    listEvents.listIterator().next().call();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                listEvents.clear();
            }
        }
        
        time_simple_update += tpf;

        if (draw_flag == 1 && time_simple_update > 0.15f){
            Node cur = (Node)player;
            cur.detachChildNamed(node_whip[1].getName());
            cur.attachChild(node_whip[2]);
            draw_flag = 2;
            time_simple_update = 0;
            return;
        }

        if (draw_flag == 2 && time_simple_update > 0.15f) {
            Node playerNode = (Node)player;
            playerNode.detachChildNamed(node_whip[2].getName());
            playerNode.attachChild(node_whip[3]);
            time_simple_update = 0;
            draw_flag = 3;
            return;
        }
        
        if (draw_flag == 3 && time_simple_update > 0.1f) {
            Node playerNode = (Node)player;
            playerNode.detachChildNamed(node_whip[3].getName());
            node_whip[0].setLocalRotation(Matrix3f.IDENTITY);
            playerNode.attachChild(node_whip[0]);
            time_simple_update = 0;
        }
    }

    private void initCamera() {
        cam.setLocation(new Vector3f(0.0f, 0.0f, 0.5f));
        getFlyByCamera().setEnabled(false);
        setDisplayStatView(false);
        setDisplayFps(false);
        viewPort.setBackgroundColor(ColorRGBA.White);
        cam.setParallelProjection(true);
    }
     
     
     
     private Spatial getSpatial(String name) {
        if (name.equals("Player")){
            Node node = new Node("Player");
            
            Material mat_body = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat_body.setColor("Color", ColorRGBA.Blue);
            Material mat_head = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat_head.setColor("Color", ColorRGBA.Red);
            
            node.setUserData("width", PLAYER_BODY_WIDTH);
            node.setUserData("height", PLAYER_BODY_HEIGHT);
            
            Geometry body = new Geometry("body", 
                                         new Box(PLAYER_BODY_WIDTH, 
                                                 PLAYER_BODY_HEIGHT, 0));
            body.setMaterial(mat_body);

            Geometry head = new Geometry("head",
                            new Box(PLAYER_BODY_WIDTH / 4f, 
                                    PLAYER_BODY_HEIGHT / 4f, 0));
            head.setMaterial(mat_head);
            head.setLocalTranslation(PLAYER_BODY_WIDTH, 0, 0);

            Picture whip_spin_pic = new Picture("whip_spin_state");
            Texture2D tex = (Texture2D)assetManager.loadTexture(
                    "Textures/spiral.png");
            whip_spin_pic.setTexture(assetManager, tex, true);
            whip_spin_pic.setLocalTranslation(PLAYER_BODY_WIDTH, -PLAYER_BODY_HEIGHT, 0);
            whip_spin_pic.rotate(0, 0, FastMath.HALF_PI);
            whip_spin_pic.setWidth(WHIP_WIDTH);
            whip_spin_pic.setHeight(WHIP_HEIGHT);

            whip_spin_pic.move(WHIP_WIDTH, WHIP_HEIGHT, 0);
            
            BillboardControl billboard = new BillboardControl();
            Geometry healthbar = new Geometry("healthbar", new Quad(4f, 0.2f));
            Material mathb = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mathb.setColor("Color", ColorRGBA.Red);
            healthbar.setMaterial(mathb);
            healthbar.center();
            healthbar.move(0, 7, 2);
            healthbar.addControl(billboard);

            node.attachChild(healthbar);
            
            node.attachChild(body);
            node.attachChild(head);
            node.attachChild(whip_spin_pic);

            return node;
        }
        return null;
    }




    private ProtocolMessage getProtocolMessage() {
        ProtocolMessage protocolMessage = new ProtocolMessage();
        ProtocolMessage.Entry entry = new ProtocolMessage.Entry();
        entry.rotation = player.getLocalRotation();
        entry.x = player.getLocalTranslation().x;
        entry.y = player.getLocalTranslation().y;
        protocolMessage.addEntry(entry);
        return protocolMessage;
    }

     
     
     
     private Node[] getNodeWhip() {
        Node[] whip_hit = new Node[4];
        whip_hit[0] = new Node("whip_spin_state");
        whip_hit[1] = new Node("whip_hit_state1");
        whip_hit[2] = new Node("whip_hit_state2");
        whip_hit[3] = new Node("whip_hit_state3");

        Picture whip_hit_pic = new Picture("whip_spin_state");
        Texture2D tex = (Texture2D)assetManager.loadTexture("Textures/spiral.png");
        whip_hit_pic.setTexture(assetManager, tex, true);
        whip_hit_pic.setWidth(WHIP_WIDTH);
        whip_hit_pic.setHeight(WHIP_HEIGHT);
        whip_hit_pic.rotate(0, 0, FastMath.HALF_PI);
        whip_hit[0].setLocalTranslation(PLAYER_BODY_WIDTH, -PLAYER_BODY_HEIGHT, 0);
        whip_hit[0].attachChild(whip_hit_pic);

        Picture whip_hit_pic1 = new Picture("whip_hit_state1");
        tex = (Texture2D)assetManager.loadTexture("Textures/whip_hit1.png");
        whip_hit_pic1.setTexture(assetManager, tex, true);
        whip_hit_pic1.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic1.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic1.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic1.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[1].attachChild(whip_hit_pic1);

        Picture whip_hit_pic2 = new Picture("whip_hit_state2");
        tex = (Texture2D)assetManager.loadTexture("Textures/whip_hit3.png");
        whip_hit_pic2.setTexture(assetManager, tex, true);
        whip_hit_pic2.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic2.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic2.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic2.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[2].attachChild(whip_hit_pic2);
        
        Picture whip_hit_pic3 = new Picture("whip_hit_state2");
        tex = (Texture2D)assetManager.loadTexture("Textures/whip_hit2.png");
        whip_hit_pic3.setTexture(assetManager, tex, true);
        whip_hit_pic3.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic3.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic3.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic3.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[3].attachChild(whip_hit_pic3);

        return whip_hit;
    }

    public void drawWhip() {
        draw_flag = 1;
        Node playerNode = (Node)player;
        playerNode.detachChildNamed("whip_spin_state");
        playerNode.attachChild(node_whip[1]);
    }

}
