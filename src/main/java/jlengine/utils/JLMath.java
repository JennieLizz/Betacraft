package jlengine.utils;

import org.joml.Vector3f;

public class JLMath {
    public static class Vertices {
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
}