package jlengine.components.graphics.camera;

import jlengine.components.ComponentBase;
import jlengine.components.math.Transform;
import jlengine.engine.Engine;
import jlengine.graphics.RenderManager;
import jlengine.utils.JLLog;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera implements ComponentBase {
    static JLLog jl = new JLLog();
    public Transform perspective = new Transform();
    public Transform view = new Transform();
    Vector3f m_pos;
    double m_fov = 90.0;
    float m_aspect;
    boolean disableAutoAspect;
    float m_zNear = 0.1f;
    float m_zFar = 100.0f;

    public Camera() {
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        m_aspect = (float) Engine.GetDisplay().GetWidth() / Engine.GetDisplay().GetHeight();

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

    public void SetAspectRatio(float width, float height) {
        m_aspect = width / height;
    }

    public void DisableAutoAspectRatio() {
        disableAutoAspect = !disableAutoAspect;
    }

    @Override
    public void OnWindowResize() {
        SetAspectRatio(Engine.GetDisplay().GetWidth(), Engine.GetDisplay().GetHeight());
    }

    public void Use() {
        RenderManager.SetUseCamera(this);
    }
}
