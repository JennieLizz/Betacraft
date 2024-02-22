package jlengine.components.graphics.camera;

import jlengine.components.JLComponentBase;
import org.joml.Matrix4f;

public class Camera implements JLComponentBase {
    Matrix4f m_view;
    float m_fov;

    public Camera() {
        m_view = new Matrix4f().perspective()
    }

    @Override
    public void Init() {

    }

    @Override
    public void Update() {

    }
}
