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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;

public class Player extends AbstractControl {

    public boolean up_key,
            down_key,
            left_key,
            right_key;
    public boolean move_mouse;
    public boolean rotate;
    public Float speed = 100f,
            speed_mouse = 10f;
    public Vector2f mouse_position;
    public float trans;

    public Player() {
        mouse_position = new Vector2f(0, 0);

        trans = 0f;
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
            spatial.move(-tpf * speed, 0, 0);
        }
        if (right_key) {
            spatial.move(tpf * speed, 0, 0);
        }
        if (up_key) {
            spatial.move(0, tpf * speed, 0);
        }
        if (down_key) {;
            spatial.move(0, -tpf * speed, 0);

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
