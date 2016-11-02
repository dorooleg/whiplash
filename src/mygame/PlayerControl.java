package mygame;

import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class PlayerControl extends AbstractControl {

    public boolean up_key,
            down_key,
            left_key,
            right_key;
    public boolean move_mouse;
    public boolean rotate;
    
    public boolean mouse_pressed;
    public boolean mouse_was_pressed;
    
    
    public Float speed = 100f,
            speed_mouse = 10f;
    public Vector2f mouse_position;
    public float trans;

    private Node[] whipStates;
    
    private int width;
    private int height;
    
    private int width_body ;
    private int height_body ;
    
    
    private MainMenu owner;
    
    public PlayerControl(int w, int h,int width_body_box, int height_body_box,MainMenu owner) {
        width = w;
        height = h;
        
        width_body = width_body_box;
        height_body = height_body_box;
        
        mouse_position = new Vector2f(width/2 +1  , height/2+1);

        trans = FastMath.PI/2;
        this.owner = owner;
    }
    
    public void setWhipStates(Node[] whipStates) {
        this.whipStates = whipStates;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (rotate) {       
            spatial.rotate(0, 0, FastMath.PI);
            rotate = false;

        }

        if (move_mouse) {
            Vector3f pos = spatial.getLocalTranslation();
            Vector2f v = new Vector2f();
            v.x = mouse_position.x - pos.x - width_body /  2f;
            v.y = mouse_position.y - pos.y - height_body / 2f;
            
            float len = v.length();

            float angle = FastMath.asin(v.y / len);
            if (v.x < 0) {
                angle = FastMath.PI - angle;
            }

            //spatial.rotate(0, 0, FastMath.sign(v.x) * (angle - trans));
            
            float[] angles = {0, 0, angle};
            Quaternion q = new Quaternion();
            q.fromAngles(angles);
            spatial.setLocalRotation(q);

        }

        if (left_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x > 30)
                spatial.move(-tpf * speed, 0, 0);
        }
        if (right_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x < width - 30)
                spatial.move(tpf * speed, 0, 0);
        }
        if (up_key) {
            
            Vector3f local_position = spatial.getLocalTranslation();
            System.out.println(local_position);
            if (local_position.y  < height - 30)
                spatial.move(0, tpf * speed, 0);
        }
        if (down_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.y >30)
                spatial.move(0, -tpf * speed, 0);

        }
        
        if (mouse_pressed){
            Node cur_node = (Node)spatial;
            Spatial child = cur_node.getChild("whip_spin_state");
            child.rotate(0, 0, -10);
            mouse_was_pressed = true;
        }
        if (mouse_was_pressed && !mouse_pressed){
//            System.err.println("sha bang");
//            Node cur_node = (Node)spatial;
//            cur_node.detachChildNamed("whip_spin_state");
//            if (whipStates!=null){
//                cur_node.attachChild(whipStates[1]);
//            }
//            cur_node.detachChildNamed(whipStates[1].getName());
//            cur_node.attachChild(whipStates[0]);
            
            owner.drawWhip();
            mouse_was_pressed = false;
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
