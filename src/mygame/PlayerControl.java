package mygame;

import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.math.FastMath;
//import com.jme3.math.
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
    private int width;
    private int height;
    private int width_body;
    private int height_body;
    private float health_status;
    private float whip_status;
    private final float speed_whip = 0.03f;
    private static final float WHIP_WIDTH = 30f;
    private static final float WHIP_HEIGHT = 30f;
    private static final float PLAYER_BODY_WIDTH = 30f;
    private static final float PLAYER_BODY_HEIGHT = 30f;
    private static final float RADIUS = 2f * FastMath.sqr(WHIP_HEIGHT);
    private static final float WHIP_RADIUS = 10f * RADIUS;
    private static final float WHIP_ANGLE = FastMath.PI / 12f;
    public int draw_flag;
    private MainMenu owner;

    public float getHealth() {
        return health_status;
    }

    public void setHealth(float health) {
        health_status = health;
    }
    public void decreaseHealth() {
        health_status = Math.max(health_status - 0.1f, 0f);
    }

    public float getWhipStatus() {
        return whip_status;
    }

    public void setWhipStatus(float value) {
        whip_status = value;
    }

    public PlayerControl(int w, int h, int width_body_box, int height_body_box, MainMenu owner) {
        width = w;
        height = h;

        width_body = width_body_box;
        height_body = height_body_box;

        mouse_position = new Vector2f(width / 2 + 1, height / 2 + 1);

        draw_flag = 0;

        health_status = 1f;
        whip_status = 0f;

        this.owner = owner;
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
            v.x = mouse_position.x - pos.x - width_body / 2f;
            v.y = mouse_position.y - pos.y - height_body / 2f;

            float len = v.length();

            float angle = FastMath.asin(v.y / len);
            if (v.x < 0) {
                angle = FastMath.PI - angle;
            }
            float[] angles = {0, 0, angle};
            Quaternion q = new Quaternion();
            q.fromAngles(angles);
            spatial.setLocalRotation(q);

        }

        if (left_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x > 30) {
                spatial.move(-tpf * speed, 0, 0);
            }
        }
        if (right_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.x < width - 30) {
                spatial.move(tpf * speed, 0, 0);
            }
        }
        if (up_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.y < height - 30) {
                spatial.move(0, tpf * speed, 0);
            }
        }
        if (down_key) {
            Vector3f local_position = spatial.getLocalTranslation();
            if (local_position.y > 30) {
                spatial.move(0, -tpf * speed, 0);
            }
        }

        if (mouse_pressed) {
            Node cur_node = (Node) spatial;
            Spatial child = cur_node.getChild("whip_spin_state");
            if (child != null) {
                child.rotate(0, 0, -10);
            }
            whip_status = Math.min(whip_status + speed_whip, 1f);
            mouse_was_pressed = true;
        }
        if (mouse_was_pressed && !mouse_pressed) {
            if (whip_status == 1.f) {
                draw_flag = 1;
                owner.drawWhip(spatial.getName());
            }
            Node cur = (Node) spatial;
            Spatial whip = cur.getChild("whip_spin_state");
            if (whip != null) {
                whip.setLocalRotation(Matrix3f.IDENTITY);
            }
            mouse_was_pressed = false;
            whip_status = 0f;
        }
    }

    public boolean inRadiusWhipHit(Vector3f enemyPos) {
        Vector3f position_player = spatial.getLocalTranslation();

        float p = FastMath.sqr(enemyPos.x - position_player.x)
                + FastMath.sqr(enemyPos.y - position_player.y);

        Vector2f v = new Vector2f(enemyPos.x - position_player.x,
                enemyPos.y - position_player.y);
        float len = v.length();

        if (p < WHIP_RADIUS) {

            float[] angle = {0, 0, 0};
            spatial.getWorldRotation().toAngles(angle);

            float playerAngle = angle[2];

            if (angle[0] != 0) {
                playerAngle = angle[0] - angle[2];
            } else {
                playerAngle = angle[2];
            }


            Vector2f v1 = new Vector2f(FastMath.cos(playerAngle - WHIP_ANGLE),
                    FastMath.sin(playerAngle - WHIP_ANGLE));
            Vector2f v2 = new Vector2f(FastMath.cos(playerAngle + WHIP_ANGLE),
                    FastMath.sin(playerAngle + WHIP_ANGLE));

            if (v1.x * v.y - v.x * v1.y > 0
                    && v2.x * v.y - v.x * v2.y < 0) {
                return true;
            }
        }
        return false;

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
