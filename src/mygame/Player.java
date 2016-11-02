package mygame;

import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sun.istack.internal.NotNull;

public class Player extends AbstractControl {

    public boolean up_key,
            down_key,
            left_key,
            right_key;
    public boolean move_mouse;
    public boolean rotate;
    
    public boolean click_mouse;
    
    public Float speed = 100f,
            speed_mouse = 10f;
    public Vector2f mouse_position;
    public float trans;

    private int widht;
    private int height;
    
    public Player(int w, int h) {
        widht = w;
        height = h;
        mouse_position = new Vector2f(0, 0);

        trans = FastMath.PI/2;
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
            v.x = mouse_position.x - pos.x;
            v.y = mouse_position.y - pos.y;

            float len = v.length();

            float angel = FastMath.asin(v.y / len);


            spatial.rotate(0, 0, FastMath.sign(v.x) * (angel - trans));
            trans = angel;

        }

        if (left_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x > 30)
                spatial.move(-tpf * speed, 0, 0);
        }
        if (right_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x < widht - 30)
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
        
        if (click_mouse){
            Node cur_node = (Node)spatial;
            Spatial child = cur_node.getChild("whip");
            child.rotate(0, 0, -10);

        }
        
    }

    public void applyGravity(Vector3f gravity) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void reset() {
        up_key = false;
        down_key = false;
        left_key = false;
        right_key = false;

        move_mouse = false;

        rotate = false;
    }

}
