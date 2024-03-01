package jlengine.components.graphics.camera;

import jlengine.components.math.Vector;
import jlengine.engine.Engine;
import jlengine.graphics.RenderManager;
import jlengine.utils.JLFrames;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class EditorCamera extends Camera {
    Vector3f m_position;
    Vector3f m_target;
    Vector2f m_deltaMouse;
    Vector2f m_prevMouse;
    float m_distance;
    float m_pitch;
    float m_yaw;
    float m_sensitivity;

    long m_frame;

    public EditorCamera() {
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        m_frame = Engine.GetDisplay().GetFrame();

        m_position = new Vector3f();
        m_target = new Vector3f();
        m_deltaMouse = new Vector2f();
        m_prevMouse = new Vector2f();
        m_distance = 4.0f;
        m_pitch = 0.0f;
        m_yaw = 0.0f;
        m_sensitivity = 0.05f;

        m_aspect = Engine.GetGui().GetSceneWidth() / Engine.GetGui().GetSceneHeight();

        perspective.SetTransform(new Matrix4f()
                .perspective((float) Math.toRadians(m_fov), m_aspect, m_zNear, m_zFar)
        );

        view.SetTransform(new Matrix4f()
                .lookAt(
                        m_position,
                        m_target,
                        Vector.Up()
                ));

        glfwSetCursorPosCallback(m_frame, (window, xPos, yPos) -> {
            if (glfwGetKey(m_frame, GLFW_KEY_B) == GLFW_PRESS) {
                glfwSetInputMode(m_frame, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

                Vector2f curPos = new Vector2f((float) xPos, (float) yPos);

                m_deltaMouse.x = curPos.x - m_prevMouse.x;
                m_deltaMouse.y = curPos.y - m_prevMouse.y;
                m_prevMouse = curPos;

                m_pitch += m_deltaMouse.y * m_sensitivity * JLFrames.GetDeltaTimeF();
                m_yaw -= m_deltaMouse.x * m_sensitivity * JLFrames.GetDeltaTimeF();

                UpdatePosition();
            } else {
                glfwSetInputMode(m_frame, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        });

        Use();
    }

    void UpdatePosition() {
        m_position.x = (float) (m_target.x + m_distance * Math.sin(m_yaw) * Math.cos(m_pitch));
        m_position.y = (float) (m_target.y + m_distance * Math.sin(m_pitch));
        m_position.z = (float) (m_target.z + m_distance * Math.cos(m_yaw) * Math.cos(m_pitch));
        view.SetTransform(new Matrix4f()
                .lookAt(
                        m_position,
                        m_target,
                        Vector.Up()
                ));
    }

    public void Use() {
        RenderManager.SetEditorCamera(this);
    }
}
