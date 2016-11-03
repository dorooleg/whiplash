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
import com.sun.java.swing.plaf.windows.WindowsBorders;
import de.lessvoid.nifty.Nifty;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;

public class MainMenu extends SimpleApplication implements ActionListener,
        AnalogListener {

    private StartScreen startScreen;
    private ArrayList<Callable> listEvents;
    public Spatial player;
    public Spatial player2;
    private PlayerControl player_control;
    private PlayerControl player_control2;
    private Node[] node_whip1;
    private Node[] node_whip2;
    private float time_simple_update = 0f;
    private static final float WHIP_WIDTH = 60f;
    private static final float WHIP_HEIGHT = 60f;
    private static final float PLAYER_BODY_WIDTH = 30f;
    private static final float PLAYER_BODY_HEIGHT = 30f;
    private ColorRGBA colorPlayer;
    private ColorRGBA colorPlayer2;

    public MainMenu() {
        listEvents = new ArrayList<Callable>();
        colorPlayer = ColorRGBA.Red;
        colorPlayer2 = ColorRGBA.Green;
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        setPauseOnLostFocus(false);

        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("Escape", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this, "Escape");

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

        if (name.equals("Escape")) {
            startScreen.clean();
            startScreen.clearPlayers();
            startScreen.start("start");
        }

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

            if (startScreen.client != null) {
                startScreen.client.send(getProtocolMessage());
            }

            if (startScreen.server != null) {
                startScreen.server.broadcast(getProtocolMessage());
            }

        }
    }

    public void initPlayer(String name_player) {
        if (startScreen.server != null) {
            colorPlayer = ColorRGBA.Red;
            colorPlayer2 = ColorRGBA.Green;
        } else {
            colorPlayer2 = ColorRGBA.Red;
            colorPlayer = ColorRGBA.Green;
        }
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


        player = getSpatial("player1");

        player.setLocalTranslation(settings.getWidth() / 2,
                settings.getHeight() / 2, 0f);
        player_control = new PlayerControl(settings.getWidth(),
                settings.getHeight(),
                (int) PLAYER_BODY_WIDTH, (int) PLAYER_BODY_HEIGHT,
                this);
        player.addControl(player_control);

        player2 = getSpatial("player2");
        player2.setLocalTranslation(settings.getWidth() / 2,
                settings.getHeight() / 2, 0f);

        player_control2 = new PlayerControl(settings.getWidth(),
                settings.getHeight(),
                (int) PLAYER_BODY_WIDTH,
                (int) PLAYER_BODY_HEIGHT,
                this);

        player2.addControl(player_control2);

        node_whip1 = getNodeWhip();
        node_whip2 = getNodeWhip();


        WindowsBorders.ProgressBarBorder b = new WindowsBorders.ProgressBarBorder(Color.CYAN, Color.ORANGE);
//       guiNode.attachChild(b);

//        Display.

        guiNode.attachChild(player);
        guiNode.attachChild(player2);
    }

    void clearPlayers() {
        guiNode.detachAllChildren();
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (player != null) {
            if (name.equals("Right")
                    || name.equals("Left")
                    || name.equals("Down")
                    || name.equals("Up")
                    || name.equals("MouseMove")) {
                player.getControl(PlayerControl.class).mouse_position =
                        inputManager.getCursorPosition();
                player.getControl(PlayerControl.class).move_mouse = true;

                if (startScreen.client != null) {
                    startScreen.client.send(getProtocolMessage());
                }

                if (startScreen.server != null) {
                    startScreen.server.broadcast(getProtocolMessage());
                }
            }
        }

    }

    public void updateState(@NotNull final ProtocolMessage message) {

        synchronized (this) {
            listEvents.add(new Callable() {
                public Object call() throws Exception {
                    if (message.entries.isEmpty()) {
                        throw new Exception("Invalid protocol message size");
                    }
                    System.out.println("Callable = " + message);
                    player2.setLocalTranslation(message.entries.get(0).x,
                            message.entries.get(0).y, 0);
                    player2.setLocalRotation(message.entries.get(0).rotation);

                    player2.getControl(PlayerControl.class).mouse_pressed =
                            message.entries.get(0).whip_heat;
                    colorPlayer2 = message.entries.get(0).color;

                    return null;
                }
            });
        }

    }

    @Override
    public void simpleUpdate(float tpf) {
        synchronized (this) {
            if (!listEvents.isEmpty()) {
                try {
                    listEvents.get(listEvents.size() - 1).call();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                listEvents.clear();
            }
        }

        time_simple_update += tpf;

        if (player == null || player2 == null) {
            return;
        }

        if (player.getControl(PlayerControl.class).draw_flag == 1 && time_simple_update > 0.15f) {

            draw_Node("player1", 1, 2, false);

            player.getControl(PlayerControl.class).draw_flag = 2;
            time_simple_update = 0;
            return;
        }

        if (player.getControl(PlayerControl.class).draw_flag == 2 && time_simple_update > 0.15f) {

            draw_Node("player1", 2, 3, false);

            time_simple_update = 0;
            player.getControl(PlayerControl.class).draw_flag = 3;
            return;
        }

        if (player.getControl(PlayerControl.class).draw_flag == 3 && time_simple_update > 0.1f) {
            draw_Node("player1", 3, 0, true);

            player.getControl(PlayerControl.class).draw_flag = 0;
            time_simple_update = 0;
        }


        // player 2 draw whip

        if (player2.getControl(PlayerControl.class).draw_flag == 1 && time_simple_update > 0.15f) {
            draw_Node("player2", 1, 2, false);
            player2.getControl(PlayerControl.class).draw_flag = 2;
            time_simple_update = 0;
            return;
        }

        if (player2.getControl(PlayerControl.class).draw_flag == 2 && time_simple_update > 0.15f) {
            draw_Node("player2", 2, 3, false);
            time_simple_update = 0;
            player2.getControl(PlayerControl.class).draw_flag = 3;
            return;
        }

        if (player2.getControl(PlayerControl.class).draw_flag == 3 && time_simple_update > 0.1f) {
            draw_Node("player2", 3, 0, true);
            player2.getControl(PlayerControl.class).draw_flag = 0;
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
        if (name.equals("player1") || name.equals("player")) {
            Node node = new Node(name);

            Material mat_body = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat_body.setColor("Color", colorPlayer);
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
            Texture2D tex = (Texture2D) assetManager.loadTexture(
                    "Textures/spiral.png");
            whip_spin_pic.setTexture(assetManager, tex, true);
            whip_spin_pic.setWidth(WHIP_WIDTH);
            whip_spin_pic.setHeight(WHIP_HEIGHT);
            whip_spin_pic.setLocalTranslation(PLAYER_BODY_WIDTH, -PLAYER_BODY_HEIGHT, 0);
            whip_spin_pic.rotate(0, 0, FastMath.HALF_PI);


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
        } else if (name.equals("player2")) {
            Node node = new Node(name);

            Material mat_body = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat_body.setColor("Color", colorPlayer2);
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
            Texture2D tex = (Texture2D) assetManager.loadTexture(
                    "Textures/spiral.png");
            whip_spin_pic.setTexture(assetManager, tex, true);
            whip_spin_pic.setWidth(WHIP_WIDTH);
            whip_spin_pic.setHeight(WHIP_HEIGHT);
            whip_spin_pic.setLocalTranslation(PLAYER_BODY_WIDTH, -PLAYER_BODY_HEIGHT, 0);
            whip_spin_pic.rotate(0, 0, FastMath.HALF_PI);


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
        entry.whip_heat = player.getControl(PlayerControl.class).mouse_pressed;
        entry.color = colorPlayer;
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
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/spiral.png");
        whip_hit_pic.setTexture(assetManager, tex, true);
        whip_hit_pic.setWidth(WHIP_WIDTH);
        whip_hit_pic.setHeight(WHIP_HEIGHT);
        whip_hit_pic.rotate(0, 0, FastMath.HALF_PI);
        whip_hit[0].setLocalTranslation(PLAYER_BODY_WIDTH, -PLAYER_BODY_HEIGHT, 0);
        whip_hit[0].attachChild(whip_hit_pic);

        Picture whip_hit_pic1 = new Picture("whip_hit_state1");
        tex = (Texture2D) assetManager.loadTexture("Textures/whip_hit1.png");
        whip_hit_pic1.setTexture(assetManager, tex, true);
        whip_hit_pic1.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic1.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic1.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic1.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[1].attachChild(whip_hit_pic1);

        Picture whip_hit_pic2 = new Picture("whip_hit_state2");
        tex = (Texture2D) assetManager.loadTexture("Textures/whip_hit3.png");
        whip_hit_pic2.setTexture(assetManager, tex, true);
        whip_hit_pic2.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic2.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic2.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic2.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[2].attachChild(whip_hit_pic2);

        Picture whip_hit_pic3 = new Picture("whip_hit_state2");
        tex = (Texture2D) assetManager.loadTexture("Textures/whip_hit2.png");
        whip_hit_pic3.setTexture(assetManager, tex, true);
        whip_hit_pic3.setWidth(WHIP_WIDTH * 4f);
        whip_hit_pic3.setHeight(WHIP_HEIGHT * 4f);
        whip_hit_pic3.move(0, WHIP_HEIGHT * 2f, 0);
        whip_hit_pic3.rotate(0, 0, -FastMath.HALF_PI);
        whip_hit[3].attachChild(whip_hit_pic3);

        return whip_hit;
    }

    public void drawWhip(String name_player) {
        draw_Node(name_player, 0, 1, false);
    }

    private void draw_Node(String name_player, int node_num_detach,
            int node_num_attac, boolean rotation) {
        Node playerNode = null;
        Node[] node = null;
        if (name_player.equals("player1")) {
            playerNode = (Node) player;
            node = node_whip1;
        }
        if (name_player.equals("player2")) {
            playerNode = (Node) player2;
            node = node_whip2;
        }
        if (playerNode == null || node == null) {
            return;
        }

        playerNode.detachChildNamed(node[node_num_detach].getName());
        playerNode.attachChild(node[node_num_attac]);
        if (rotation) {
            node[node_num_attac].setLocalRotation(Matrix3f.IDENTITY);
        }
    }
}
