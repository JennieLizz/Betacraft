package jlengine.components.graphics.camera;

import jlengine.components.math.Transform;
import jlengine.graphics.RenderManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera {
    public Transform perspective = new Transform();
    public Transform view = new Transform();
    Vector3f m_pos;
    double m_fov = 90.0;
    float m_aspect = 16.0f / 9.0f;
    float m_zNear = 0.1f;
    float m_zFar = 100.0f;

    public Camera() {
        perspective.SetTransform(new Matrix4f()
                .perspective((float) Math.toRadians(m_fov), m_aspect, m_zNear, m_zFar)
        );

        view.SetTransform(new Matrix4f()
                .lookAt(
                        0.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 3.0f,
                        0.0f, 1.0f, 0.0f
                ));

        Use();
    }

    public void Use() {
        RenderManager.SetUseCamera(this);
    }

    /*public void SetPosition(Vector3f position) {
        view.SetTransform(new Matrix4f()
                .lookAt(
                        position.x, position.y, position.z,
                        0.0f, 0.0f, 3.0f,
                        0.0f, 1.0f, 0.0f
                ));
    }

    public Vector3f GetPosition() {
        return m_transform.getTranslation(new Vector3f());
    }

    public void SetRotation(Quaternionf rotation) {
        m_transform.rotate(rotation);
    }

    public Quaternionf GetRotation() {
        return m_transform.getNormalizedRotation(new Quaternionf());
    }*/
}
