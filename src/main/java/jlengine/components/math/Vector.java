package jlengine.components.math;

import org.joml.Vector3f;

public class Vector {
    public static Vector3f Up() {
        return new Vector3f(0, 1, 0);
    }

    public static Vector3f Front() {
        return new Vector3f(0, 0, -1);
    }

    public static float[] ToFloatArray(Vector3f[] vectors) {
        float[] floats = new float[vectors.length * 3];

        for (int i = 0; i < vectors.length; i++) {
            Vector3f vector = vectors[i];
            floats[i * 3] = vector.x;
            floats[i * 3 + 1] = vector.y;
            floats[i * 3 + 2] = vector.z;
        }

        return floats;
    }
}
