package jlengine.components.math;

import jlengine.components.ComponentBase;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends ComponentBase {
    Matrix4f m_transform = new Matrix4f();

    public void SetPosition(Vector3f position) {
        m_transform.identity().translate(position);
    }

    public Vector3f GetPosition() {
        return m_transform.getTranslation(new Vector3f());
    }

    public void SetRotation(Quaternionf rotation) {
        m_transform.rotate(rotation);
    }

    public Quaternionf GetRotation() {
        return m_transform.getNormalizedRotation(new Quaternionf());
    }

    public void SetScale(Vector3f scale) {
        m_transform.scale(scale);
    }

    public Vector3f GetScale() {
        return m_transform.getScale(new Vector3f());
    }

    public void Translate(Vector3f translation) {
        m_transform.translate(translation);
    }

    public void SetTransform(Matrix4f transform) {
        m_transform.set(transform);
    }

    public Matrix4f GetTransform() {
        return m_transform;
    }
}